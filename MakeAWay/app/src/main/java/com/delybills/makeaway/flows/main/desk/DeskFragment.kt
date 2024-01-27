package com.delybills.makeaway.flows.main.desk

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.delybills.makeaway.App
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentDeskBinding
import com.delybills.makeaway.domain.model.desk.DeskType
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban
import com.delybills.makeaway.domain.model.task.kanban.TaskKanbanStatusType
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrix
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType
import com.delybills.makeaway.domain.model.task.todo.TaskToDo
import com.delybills.makeaway.flows.main.adapters.TasksKanbanAdapter
import com.delybills.makeaway.flows.main.adapters.TasksMatrixAdapter
import com.delybills.makeaway.flows.main.adapters.TasksToDoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@SuppressLint("UseRequireInsteadOfGet")
class DeskFragment : BaseFragment<DeskViewModel, DeskRepository, FragmentDeskBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val args: DeskFragmentArgs by navArgs()

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


    override fun provideViewModel(): DeskViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[DeskViewModel::class.java]
    }

    override fun provideBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDeskBinding = FragmentDeskBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deskName.text = args.deskData.name
        binding.deskType.text = args.deskData.deskType.title
        if (args.deskData.deadline.toString() == "null") {
            binding.deskDeadline.visibility = View.GONE
        } else {
            val locale = Locale("ru")
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
            binding.deskDeadline.text = args.deskData.deadline?.let { sdf.format(it) }
        }
        binding.deskDescription.text = args.deskData.description


        getTasks()
        observeToDoTasks()
        observeKanbanTasks()
        observeMatrixTasks()
        setUpToDoRecyclerView()
        setUpKanbanRecyclerViews()
        setUpMatrixRecyclerViews()
        handleDeskType()
    }

    private fun getTasks() {
        when (args.deskData.deskType) {
            DeskType.TODO -> viewModel.getToDoTasks(args.deskData.id)
            DeskType.Kanban -> viewModel.getKanbanTasks(args.deskData.id)
            DeskType.Matrix -> viewModel.getMatrixTasks(args.deskData.id)
            DeskType.Unknown -> {
                Log.d("Error", "Неправильный тип доски")
                DeskType.Unknown
            }
        }
    }

    private fun handleDeskType() {
        when (args.deskData.deskType) {
            DeskType.TODO -> setToDoView()
            DeskType.Kanban -> setKanbanView()
            DeskType.Matrix -> setMatrixView()
            DeskType.Unknown -> {
                Log.d("Error", "Неправильный тип доски")
                DeskType.Unknown
            }
        }
    }

    private fun setToDoView() {
        binding.matrixSection.root.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.GONE
        binding.toDoSection.root.visibility = View.VISIBLE
        binding.toDoSection.addToDoTaskButton.visibility = View.GONE
    }

    private fun setKanbanView() {
        binding.toDoSection.root.visibility = View.GONE
        binding.matrixSection.root.visibility = View.GONE
        binding.kanbanSection.root.visibility = View.VISIBLE
        binding.kanbanSection.addKanbanTaskButton.visibility = View.GONE
    }

    private fun setMatrixView() {
        binding.kanbanSection.root.visibility = View.GONE
        binding.toDoSection.root.visibility = View.GONE
        binding.matrixSection.root.visibility = View.VISIBLE
        binding.matrixSection.addMatrixTaskButton.visibility = View.GONE
    }

    private fun setUpToDoRecyclerView() {
        toDoTasksAdapter = TasksToDoAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        toDoTasksLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.toDoSection.toDoSectionRecyclerView.adapter = toDoTasksAdapter
        binding.toDoSection.toDoSectionRecyclerView.layoutManager = toDoTasksLayoutManager
    }

    private fun setUpKanbanRecyclerViews() {

        kanbanToDoAdapter = TasksKanbanAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        kanbanInProgressAdapter = TasksKanbanAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        kanbanDoneAdapter = TasksKanbanAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }

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

        matrixUIAdapter = TasksMatrixAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        matrixUILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixUNIAdapter = TasksMatrixAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        matrixUNILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixNUIAdapter = TasksMatrixAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
        matrixNUILayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        matrixNUNIAdapter = TasksMatrixAdapter { task, isChecked ->
            viewModel.setTaskCompletion(task.id, isChecked)
        }
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


    private fun observeToDoTasks() {
        lifecycleScope.launch {
            viewModel.todoTasks.collectLatest {
                setUpToDoTasks(it)
            }
        }
    }

    private fun observeKanbanTasks() {
        lifecycleScope.launch {
            viewModel.kanbanTasks.collectLatest {
                filterKanbanTasks(it)
            }
        }
    }

    private fun observeMatrixTasks() {
        lifecycleScope.launch {
            viewModel.matrixTasks.collectLatest {
                filterMatrixTasks(it)
            }
        }
    }

    private fun setUpToDoTasks(tasks: List<TaskToDo>) {
        toDoTasksAdapter.data = tasks
    }

    private fun filterKanbanTasks(tasks: List<TaskKanban>) {
        val toDoTasks = tasks.filter { it.status == TaskKanbanStatusType.ToDo }
        val inProgressTasks = tasks.filter { it.status == TaskKanbanStatusType.InProgress }
        val doneTasks = tasks.filter { it.status == TaskKanbanStatusType.Done }
        kanbanToDoAdapter.data = toDoTasks
        kanbanInProgressAdapter.data = inProgressTasks
        kanbanDoneAdapter.data = doneTasks

    }

    private fun filterMatrixTasks(tasks: List<TaskMatrix>) {
        val UITasks = tasks.filter { it.category == TaskMatrixCategoryType.UI }
        val NUITasks = tasks.filter { it.category == TaskMatrixCategoryType.NUI }
        val UNITasks = tasks.filter { it.category == TaskMatrixCategoryType.UNI }
        val NUNITasks = tasks.filter { it.category == TaskMatrixCategoryType.NUNI }
        matrixUIAdapter.data = UITasks
        matrixNUIAdapter.data = NUITasks
        matrixUNIAdapter.data = UNITasks
        matrixNUNIAdapter.data = NUNITasks
    }
}