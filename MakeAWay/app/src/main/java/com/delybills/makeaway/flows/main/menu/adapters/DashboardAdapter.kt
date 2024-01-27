package com.delybills.makeaway.flows.main.menu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.delybills.makeaway.R
import com.delybills.makeaway.domain.model.desk.Desk
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardAdapter(
    private val onClick: ((Desk) -> Unit)? = null
) : RecyclerView.Adapter<DashboardAdapter.ProjectsViewHolder>() {

    var data: List<Desk> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_dashboard, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ProjectsViewHolder, position: Int) {
        with(viewHolder) {
            name.text = data[position].name
            deadline.text = getDeadlineFormatted(data[position].deadline)
        }

        viewHolder.card.setOnClickListener { onClick?.invoke(data[position]) }
    }

    inner class ProjectsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var deadline: TextView
        var card: CardView

        init {
            name = view.findViewById(R.id.deskName)
            deadline = view.findViewById(R.id.deskDeadline)
            card = view.findViewById(R.id.rootCard)
        }
    }

    private fun getDeadlineFormatted(deadline: Date?): String =
        deadline?.let {
            val currentTime = Calendar.getInstance().time
            if (deadline < currentTime) {
                "Срок истек"
            } else {
                val locale = Locale("ru")
                val sdf = SimpleDateFormat("dd MMM", locale)
                sdf.format(deadline)
            }
        } ?: ""

    override fun getItemCount() = data.size
}