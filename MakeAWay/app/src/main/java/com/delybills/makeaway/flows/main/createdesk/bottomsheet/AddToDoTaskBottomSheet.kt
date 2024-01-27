package com.delybills.makeaway.flows.main.createdesk.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.delybills.makeaway.databinding.FragmentAddToDoTaskBottomSheetBinding

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToDoTaskBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddToDoTaskBottomSheetBinding
    private lateinit var taskToDoViewModel: TaskToDoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = requireParentFragment()
        taskToDoViewModel = ViewModelProvider(fragment).get(TaskToDoViewModel::class.java)
        binding.saveToDoTaskButton.setOnClickListener {
            saveTask()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddToDoTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveTask() {
        if (binding.name.text.toString().isBlank()){
            Toast.makeText(requireContext(), "Неправильно заполнено название", Toast.LENGTH_SHORT).show()
        } else {
            taskToDoViewModel.name.value = binding.name.text.toString()
            binding.name.setText("")
            dismiss()
        }
    }
}
