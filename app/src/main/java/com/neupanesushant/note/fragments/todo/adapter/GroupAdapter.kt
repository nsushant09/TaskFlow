package com.neupanesushant.note.fragments.todo.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.databinding.ItemTodoGroupBinding
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.dpToPx

class GroupAdapter(
    private val context: Context,
    var list: List<TaskGroupWithAllTasks>,
    private val callback: GenericCallback<TaskGroupWithAllTasks>
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTodoGroupBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTodoGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.root.layoutParams.width =
            ((Resources.getSystem().displayMetrics.widthPixels / 2) - dpToPx(
                context,
                20f
            )).toInt()

        val completed = item.tasks.filter { it.isCompleted }.size

        holder.binding.apply {
            tvGroupName.text = item.name
            tvTotalTaskValue.text = "${item.tasks.size} tasks "
            tvCompletedTaskValue.text = " $completed completed"
            if (completed < 1)
                lpiTaskProgress.setProgress(0, false)
            else {
                val percentage = (completed.toFloat() / item.tasks.size) * 100
                lpiTaskProgress.setProgress(percentage.toInt(), false)
            }
        }

        holder.binding.root.setOnClickListener {
            callback.callback(item, CallbackAction.CLICK)
        }
        holder.binding.root.setOnLongClickListener {
            callback.callback(item, CallbackAction.LONG_CLICK)
            true
        }
    }
}