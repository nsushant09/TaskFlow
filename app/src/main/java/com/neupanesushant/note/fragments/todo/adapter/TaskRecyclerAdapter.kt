package com.neupanesushant.note.fragments.todo.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.ItemAllTaskBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback

class TaskRecyclerAdapter(private val context: Context, var list: List<Task>, private val genericCallback: GenericCallback<Task>) :
    RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAllTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.tvDate
        val name = binding.tvTaskName
        val description = binding.tvTaskDetails
        val btnToggleCompleted = binding.btnToggleCompleted
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAllTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list.get(position)

        if (item.description.isEmpty())
            holder.description.visibility = View.GONE

        if (item.date.isEmpty())
            holder.date.visibility = View.GONE

        holder.name.text = item.title
        holder.description.text = item.description
        holder.date.text = item.date

        if (item.isCompleted)
            holder.btnToggleCompleted.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_tick_filled
                )
            )
        else
            holder.btnToggleCompleted.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_bullseye
                )
            )

        holder.btnToggleCompleted.setOnClickListener {
            item.isCompleted = !item.isCompleted
            genericCallback.callback(item, CallbackAction.TOGGLE)
        }

        holder.binding.root.setOnClickListener {
            genericCallback.callback(item, CallbackAction.CLICK)
        }
    }
}