package com.neupanesushant.note.extras.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericRecyclerAdapter<T : Any, VM : ViewBinding>(
    private val list: List<T>,
    private val bindingClass: Class<VM>,
    private val bindingInterface: RecyclerCallback<VM, T>
) : RecyclerView.Adapter<GenericRecyclerAdapter<T, VM>.BindingHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
            .invoke(null, inflater, parent, false) as VM
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = list[holder.adapterPosition]
        bindingInterface.bindData(holder.binding, item, list, position)
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(refreshedListItems: List<T>) {
        (list as MutableList<T>).clear()
        (list as MutableList<T>).addAll(refreshedListItems)
        notifyDataSetChanged()
    }
    inner class BindingHolder(val binding: VM) : RecyclerView.ViewHolder(binding.root)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}