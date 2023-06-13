package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.databinding.FragmentTodoHomeBinding
import com.neupanesushant.note.databinding.ItemTodoGroupBinding
import com.neupanesushant.note.domain.model.TaskGroup

class TodoGroupAdapter(
    val context: Context,
    val list: List<TaskGroup>,
    val onClick: (id: Int) -> Unit
) :
    RecyclerView.Adapter<TodoGroupAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTodoGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvGroupName
        val progress = binding.lpiTaskProgress
        val totalTasks = binding.tvTotalTaskValue
        val completedTasks = binding.tvCompletedTaskValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTodoGroupBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]

        holder.name.text = obj.name
        holder.totalTasks.text = "${obj.tasks.size} tasks"
        val completed = obj.tasks.filter { it.isCompleted }.size
        holder.completedTasks.text = "$completed completed"
        val percentage = (completed / obj.tasks.size) * 100
        holder.progress.setProgress(percentage, true)

        holder.itemView.setOnClickListener {
            onClick(obj.id)
        }
    }

}