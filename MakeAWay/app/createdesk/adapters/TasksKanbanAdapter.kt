package com.delybills.makeaway.flows.main.createdesk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.delybills.makeaway.R
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban

class TasksKanbanAdapter(
    private val onClick: ((TaskKanban) -> Unit)? = null
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
        }
        viewHolder.checkBox.setOnClickListener { onClick?.invoke(data[position]) }
    }

    inner class ProjectsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkBox: CheckBox

        init {
            checkBox = view.findViewById(R.id.checkbox)
        }
    }
//    private fun getDeadlineFormatted(deadline: Date): String {
//        val currentTime = Calendar.getInstance().time
//        if (deadline < currentTime) {
//            return "Срок истек"
//        }
//        val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
//        return sdf.format(deadline)
//    }

    override fun getItemCount() = data.size
}