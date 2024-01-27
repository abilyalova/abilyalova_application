package com.delybills.makeaway.flows.main.createdesk

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.delybills.makeaway.App
import com.delybills.makeaway.R
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentCreateDeskBinding
import com.delybills.makeaway.domain.model.desk.DeskType
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban
import com.delybills.makeaway.domain.model.task.kanban.TaskKanbanStatusType
import com.delybills.makeaway.domain.model.task.kanban.TasksKanban
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrix
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType
import com.delybills.makeaway.domain.model.task.matrix.TasksMatrix
import com.delybills.makeaway.domain.model.task.todo.TaskToDo
import com.delybills.makeaway.domain.model.task.todo.TasksToDo
import com.delybills.makeaway.flows.main.adapters.TasksKanbanAdapter
import com.delybills.makeaway.flows.main.adapters.TasksMatrixAdapter
import com.delybills.makeaway.flows.main.adapters.TasksToDoAdapter
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.AddKanbanTaskBottomSheet
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.AddMatrixTaskBottomSheet
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.AddToDoTaskBottomSheet
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.TaskKanbanViewModel
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.TaskMatrixViewModel
import com.delybills.makeaway.flows.main.createdesk.bottomsheet.TaskToDoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class CreateDeskFragment :
    BaseFragment<CreateDeskViewModel, CreateDeskRepository, FragmentCreateDeskBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val calendar: Calendar = Calendar.getInstance()

    private var navController: NavController? = null

    private lateinit var toDoTasksAdapter: TasksToDoAdapter

    private lateinit var kanbanToDoAdapter: TasksKanbanAdapter
    private lateinit var kanbanInProgressAdapter: TasksKanbanAdapter
    private lateinit var kanbanDoneAdapter: TasksKanbanAdapter

    private lateinit var matrixUIAdapter: TasksMatrixAdapter
    private lateinit var matrixUNIAdapter: TasksMatrixAdapter
    private lateinit var matrixNUIAdapter: TasksMatrixAdapter
    private lateinit var matrixNUNIAdapter: TasksMatrixAdapter

    private lateinit var toDoTasksLayoutManager: LinearLayoutManager

    private lateinit var kanbanToDoLayoutManager: LinearLayoutManager
    private lateinit var kanbanInProgressLayoutManager: LinearLayoutManager
    private lateinit var kanbanDoneLayoutManager: LinearLayoutManager

    private lateinit var matrixUILayoutManager: LinearLayoutManager
    private lateinit var matrixUNILayoutManager: LinearLayoutManager
    private lateinit var matrixNUILayoutManager: LinearLayoutManager
    private lateinit var matrixNUNILayoutManager: LinearLayoutManager

    private lateinit var taskToDoViewModel: TaskToDoViewModel
    private lateinit var taskKanbanViewModel: TaskKanbanViewModel
    private lateinit var taskMatrixViewModel: TaskMatrixViewModel


    override fun provideBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentCreateDeskBinding = FragmentCreateDeskBinding.inflate(inflater, container, false)

    override fun provideViewModel(): CreateDeskViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[CreateDeskViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)
        taskToDoViewModel = ViewModelProvider(this).get(TaskToDoViewModel::class.java)
        taskKanbanViewModel = ViewModelProvider(this).get(TaskKanbanViewModel::class.java)
        taskMatrixViewModel = ViewModelProvider(this).get(TaskMatrixViewModel::class.java)
        setUpToDoRecyclerView()
        setSpinnerView()
        setUpButtons()
        collectCreateDeskDataResponse()
        observeErrorResponse()
        setUpKanbanRecyclerViews()
        setUpMatrixRecyclerViews()
        collectToDoTaskData()
        collectKanbanTaskData()
        collectMatrixTaskData()
    }

    private fun setUpButtons() {
        binding.Date.setOnClickListener {
            showDateTimePickerDialog()
        }
        binding.saveDeskButton.setOnClickListener {
            saveDeskData()
        }
        binding.toDoSection.addToDoTaskButton.setOnClickListener {
            AddToDoTaskBottomSheet().show(childFragmentManager, "AddTaskTag")
        }

        binding.kanbanSection.addKanbanTaskButton.setOnClickListener {
            AddKanbanTaskBottomSheet().show(childFragmentManager, "AddTaskTag")
        }

        binding.matrixSection.addMatrixTaskButton.setOnClickListener {
            AddMatrixTaskBottomSheet().show(childFragmentManager, "AddTaskTag")
        }
    }

    private fun collectToDoTaskData() {
        taskToDoViewModel.name.observe(viewLifecycleOwner) { name ->
            addToDoTask(
                TaskToDo(
                    "",
                    name,
                    false,
                )
            )
        }
    }

    private fun collectKanbanTaskData() {
        taskKanbanViewModel.name.observe(viewLifecycleOwner) { name ->
            if (taskKanbanViewModel.status == TaskKanbanStatusType.ToDo) {
                kanbanToDoAddTask(
                    TaskKanban(
                        "",
                        name,
                        taskKanbanViewModel.status,
                        false,
                    )
                )
            } else if (taskKanbanViewModel.status == TaskKanbanStatusType.InProgress) {
                kanbanInProgressAddTask(
                    TaskKanban(
                        "",
                        name,
                        taskKanbanViewModel.status,
                        false,
                    )
                )
            } else if (taskKanbanViewModel.status == TaskKanbanStatusType.Done) {
                kanbanDoneAddTask(
                    TaskKanban(
                        "",
                        name,
                        taskKanbanViewModel.status,
                        false,
                    )
                )
            }
        }
    }

    private fun collectMatrixTaskData() {
        taskMatrixViewModel.name.observe(viewLifecycleOwner) { name ->
            if (taskMatrixViewModel.category == TaskMatrixCategoryType.UI) {
                matrixUIAddTask(
                    TaskMatrix(
                        "",
                        name,
                        taskMatrixViewModel.category,
                        false,
                    )
                )
            } else if (taskMatrixViewModel.category == TaskMatrixCategoryType.NUI) {
                matrixNUIAddTask(
                    TaskMatrix(
                        "",
                        name,
                        taskMatrixViewModel.category,
                        false,
                    )
                )
            } else if (taskMatrixViewModel.category == TaskMatrixCategoryType.UNI) {
                matrixUNIAddTask(
                    TaskMatrix(
                        "",
                        name,
                        taskMatrixViewModel.category,
                        false,
                    )
                )
            } else if (taskMatrixViewModel.category == TaskMatrixCategoryType.NUNI) {
                matrixNUNIAddTask(
                    TaskMatrix(
                        "",
                        name,
                        taskMatrixViewModel.category,
                        false,
                    )
                )
            }
        }
    }

    private fun saveDeskData() {
        if (binding.nameED.text.toString().isBlank()) {
            showToast("Название не может быть пустым")
        } else {
            val name = binding.nameED.text.toString()
            val type = if (binding.typeSpinner.selectedItemPosition == 0) {
                showToast("Выберите тип доски")
                return
            } else {
                when (binding.typeSpinner.selectedItem.toString()) {
                    DeskType.TODO.title -> DeskType.TODO
                    DeskType.Kanban.title -> DeskType.Kanban
                    DeskType.Matrix.title -> DeskType.Matrix

                    else -> {
                        Log.d("Error", "неправильный тип доски")
                        return
                    }
                }
            }

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val deadline: Date?
            if (binding.Date.text.toString().isNotEmpty()) {
                deadline = sdf.parse(binding.Date.text.toString())
            } else {
                deadline = null
            }
            val description = binding.descriptionEditText.text.toString()
            val toDoTasks = TasksToDo(toDoTasksAdapter.data)
            val kanbanTasks =
                TasksKanban(kanbanToDoAdapter.data + kanbanInProgressAdapter.data + kanbanDoneAdapter.data)
            val matrixTasks =
                TasksMatrix(matrixUIAdapter.data + matrixUNIAdapter.data + matrixNUIAdapter.data + matrixNUNIAdapter.data)


            if (type == DeskType.TODO) {
                viewModel.saveToDoDeskData(name, type, deadline, description, toDoTasks)
            } else if (type == DeskType.Kanban) {
                viewModel.saveKanbanDeskData(name, type, deadline, description, kanbanTasks)
            } else {
                viewModel.saveMatrixDeskData(name, type, deadline, description, matrixTasks)
            }
        }
    }

    private fun addToDoTask(task: TaskToDo) {
        val mList = toDoTasksAdapter.data.toMutableList()
        mList.add(task)
        toDoTasksAdapter.data = mList
    }

    private fun kanbanToDoAddTask(task: TaskKanban) {
        val mList = kanbanToDoAdapter.data.toMutableList()
        mList.add(task)
        kanbanToDoAdapter.data = mList
    }

    private fun kanbanInProgressAddTask(task: TaskKanban) {
        val mList = kanbanInProgressAdapter.data.toMutableList()
        mList.add(task)
        kanbanInProgressAdapter.data = mList
    }

    private fun kanbanDoneAddTask(task: TaskKanban) {
        val mList = kanbanDoneAdapter.data.toMutableList()
        mList.add(task)
        kanbanDoneAdapter.data = mList
    }

    private fun matrixUIAddTask(task: TaskMatrix) {
        val mList = matrixUIAdapter.data.toMutableList()
        mList.add(task)
        matrixUIAdapter.data = mList
    }

    private fun matrixNUIAddTask(task: TaskMatrix) {
        val mList = matrixNUIAdapter.data.toMutableList()
        mList.add(task)
        matrixNUIAdapter.data = mList
    }

    private fun matrixUNIAddTask(task: TaskMatrix) {
        val mList = matrixUNIAdapter.data.toMutableList()
        mList.add(task)
        matrixUNIAdapter.data = mList
    }

    private fun matrixNUNIAddTask(task: TaskMatrix) {
        val mList = matrixNUNIAdapter.data.toMutableList()
        mList.add(task)
        matrixNUNIAdapter.data = mList
    }


    private fun setUpToDoRecyclerView() {
        toDoTasksAdapter = TasksToDoAdapter()
        toDoTasksLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.toDoSection.toDoSectionRecyclerView.adapter = toDoTasksAdapter
        binding.toDoSection.toDoSectionRecyclerView.layoutManager = toDoTasksLayoutManager
    }

    private fun setUpKanbanRecyclerViews() {

        kanbanToDoAdapter = TasksKanbanAdapter()
        kanbanInProgressAdapter = TasksKanbanAdapter()
        kanbanDoneAdapter = TasksKanbanAdapter()

        kanbanToDoLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        kanbanInProgressLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        kanbanDoneLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.kanbanSection.recyclerViewToDo.adapter = kanbanToDoAdapter
        binding.kanbanSection.recyclerViewToDo.layoutManager = kanbanToDoLayoutManager

        binding.kanbanSection.recyclerViewInProgress.adapter = kanbanInProgressAdapter
        binding.kanbanSection.recyclerViewInProgress.layoutManager = kanbanInProgressLayoutManager

        binding.kanbanSection.recyclerViewDone.adapter = kanbanDoneAdapter
        binding.kanbanSection.recyclerViewDone.layoutManager = kanbanDoneLayoutManager

        binding.kanbanSection.textViewToDo.setOnClickListener {
            if (binding.kanbanSection.recyclerViewToDo.visibility == View.VISIBLE) {
                binding.kanbanSection.recyclerViewToDo.visibility = View.GONE
            } else {
                binding.kanbanSection.recyclerViewToDo.visibility = View.VISIBLE
            }
        }
        binding.kanbanSection.textViewInProgress.setOnClickListener {
            if (binding.kanbanSection.recyclerViewInProgress.visibility == View.VISIBLE) {
                binding.kanbanSection.recyclerViewInProgress.visibility = View.GONE
            } else {
                binding.kanbanSection.recyclerViewInProgress.visibility = View.VISIBLE
            }
        }
        binding.kanbanSection.textViewDone.setOnClickListener {
            if (binding.kanbanSection.recyclerViewDone.visibility == View.VISIBLE) {
                binding.kanbanSection.recyclerViewDone.visibility = View.GONE
            } else {
                binding.kanbanSection.recyclerViewDone.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpMatrixRecyclerViews() {

        matrixUIAdapter = TasksMatrixAdapter()
        matrixUILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixUNIAdapter = TasksMatrixAdapter()
        matrixUNILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixNUIAdapter = TasksMatrixAdapter()
        matrixNUILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixNUNIAdapter = TasksMatrixAdapter()
        matrixNUNILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.matrixSection.urgentImportantRecyclerView.adapter = matrixUIAdapter
        binding.matrixSection.urgentImportantRecyclerView.layoutManager = matrixUILayoutManager

        binding.matrixSection.urgentNotImportantRecyclerView.adapter = matrixUNIAdapter
        binding.matrixSection.urgentNotImportantRecyclerView.layoutManager = matrixUNILayoutManager

        binding.matrixSection.notUrgentImportantRecyclerView.adapter = matrixNUIAdapter
        binding.matrixSection.notUrgentImportantRecyclerView.layoutManager = matrixNUILayoutManager

        binding.matrixSection.notUrgentNotImportantRecyclerView.adapter = matrixNUNIAdapter
        binding.matrixSection.notUrgentNotImportantRecyclerView.layoutManager =
            matrixNUNILayoutManager
    }

    private fun collectCreateDeskDataResponse() {
        lifecycleScope.launch {
            viewModel.messageDataCreateDesk.collectLatest {
                navController?.navigate(R.id.action_createDeskFragment_to_menuFragment)
            }
        }
    }

    private fun observeErrorResponse() {
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            if (it.isNetworkFailure) {
                showToast("Проверьте подключение к интернету")
            } else {
                showToast(it.body?.string() ?: "Непредвиденная ошибка")
            }
        }
    }

    private fun setSpinnerView() {
        val spinner = binding.typeSpinner

        val typeOfDesks = DeskType.values()
        val typeOfDesksString = typeOfDesks.map { it.title }

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, typeOfDesksString
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.prompt = "Тип доски"

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                handleOnSpinnerItemSelected(typeOfDesks[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun handleOnSpinnerItemSelected(deskType: DeskType) {
        when (deskType) {
            DeskType.TODO -> setToDoView()
            DeskType.Kanban -> setKanbanView()
            DeskType.Matrix -> setMatrixView()
            DeskType.Unknown -> setUnknownView()
        }
    }

    private fun setToDoView() {
        binding.chooseDeskTypePlaceholder.visibility = View.GONE
        binding.matrixSection.root.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.GONE
        binding.toDoSection.root.visibility = View.VISIBLE
    }

    private fun setKanbanView() {
        binding.chooseDeskTypePlaceholder.visibility = View.GONE
        binding.toDoSection.root.visibility = View.GONE
        binding.matrixSection.root.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.VISIBLE
    }

    private fun setMatrixView() {
        binding.chooseDeskTypePlaceholder.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.GONE
        binding.toDoSection.root.visibility = View.GONE
        binding.matrixSection.root.visibility = View.VISIBLE
    }

    private fun setUnknownView() {
        binding.toDoSection.root.visibility = View.GONE
        binding.matrixSection.root.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.GONE
        binding.chooseDeskTypePlaceholder.visibility = View.VISIBLE
    }

    private fun showDateTimePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                showTimePickerDialog()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog() {
        TimePickerDialog(
            requireContext(), { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(calendar.time)

                binding.Date.setText(formattedDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}