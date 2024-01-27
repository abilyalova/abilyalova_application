package com.delybills.makeaway.flows.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.delybills.makeaway.R
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban

class TasksKanbanAdapter(
    private val onClick: ((TaskKanban, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<TasksKanbanAdapter.ProjectsViewHolder>() {

    var data: List<TaskKanban> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_todo_task, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ProjectsViewHolder, position: Int) {
        with(viewHolder) {
            checkBox.text = data[position].title
            checkBox.isChecked = data[position].isCompleted
        }
        viewHolder.checkBox.setOnClickListener { onClick?.invoke(data[position], viewHolder.checkBox.isChecked) }
    }

    inner class ProjectsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkBox: CheckBox

        init {
            checkBox = view.findViewById(R.id.checkbox)
        }
    }

    override fun getItemCount() = data.size
}