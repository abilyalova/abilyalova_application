package com.delybills.makeaway.flows.main.createdesk.bottomsheet

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.delybills.makeaway.databinding.FragmentAddKanbanTaskBottomSheetBinding
import com.delybills.makeaway.domain.model.task.kanban.TaskKanbanStatusType
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddKanbanTaskBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddKanbanTaskBottomSheetBinding
    private lateinit var taskKanbanViewModel: TaskKanbanViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = requireParentFragment()
        taskKanbanViewModel = ViewModelProvider(fragment).get(TaskKanbanViewModel::class.java)
        setStatusSpinnerView()
        binding.saveKanbanTaskButton.setOnClickListener {
            saveTaskName()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddKanbanTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveTaskName() {
        if(binding.name.text.toString().isBlank() || taskKanbanViewModel.status == TaskKanbanStatusType.Unknown) {
            Toast.makeText(requireContext(), "Некорректное заполнение полей", Toast.LENGTH_SHORT)
                .show()
        } else {
            taskKanbanViewModel.name.value = binding.name.text.toString()
            binding.name.setText("")
            dismiss()
        }
    }

    private fun setStatusSpinnerView() {
        val spinner = binding.statusSpinner

        val typeOfKanbanTasks = TaskKanbanStatusType.values()
        val typeOfKanbanTasksString = typeOfKanbanTasks.map { it.title }

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            requireContext(), R.layout.simple_spinner_item, typeOfKanbanTasksString
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.prompt = "Статус"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                taskKanbanViewModel.status = typeOfKanbanTasks[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }
}

