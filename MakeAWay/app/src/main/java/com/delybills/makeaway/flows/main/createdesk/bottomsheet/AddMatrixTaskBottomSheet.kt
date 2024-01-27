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
import com.delybills.makeaway.databinding.FragmentAddMatrixTaskBottomSheetBinding
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMatrixTaskBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddMatrixTaskBottomSheetBinding
    private lateinit var taskMatrixViewModel: TaskMatrixViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = requireParentFragment()
        taskMatrixViewModel = ViewModelProvider(fragment).get(TaskMatrixViewModel::class.java)
        setSpinnerView()
        binding.saveMatrixTaskButton.setOnClickListener {
            saveTaskName()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMatrixTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveTaskName() {
        if(binding.name.text.toString().isBlank() || taskMatrixViewModel.category == TaskMatrixCategoryType.Unknown){
            Toast.makeText(requireContext(), "Некорректное заполнение полей", Toast.LENGTH_SHORT).show()
        } else {
            taskMatrixViewModel.name.value = binding.name.text.toString()
            binding.name.setText("")
            dismiss()
        }
    }

    private fun setSpinnerView() {
        val spinner = binding.categorySpinner

        val typeOfMatrixTasks = TaskMatrixCategoryType.values()
        val typeOfMatrixTasksString = typeOfMatrixTasks.map { it.title }

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            requireContext(), R.layout.simple_spinner_item, typeOfMatrixTasksString
        )

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.prompt = "Категория"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                taskMatrixViewModel.category = typeOfMatrixTasks[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }
}

