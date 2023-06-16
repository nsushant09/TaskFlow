package com.neupanesushant.note.extras.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericRecyclerAdapter<T : Any, VM : ViewBinding>(
    private var listItems: List<T>,
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
        val item = listItems[holder.adapterPosition]
        bindingInterface.bindData(holder.binding, item, listItems)
    }

    override fun getItemCount(): Int {
        return try {
            listItems.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(refreshedListItems: List<T>) {
        differ.submitList(refreshedListItems)
    }

    inner class BindingHolder(val binding: VM) : RecyclerView.ViewHolder(binding.root)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private val differCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem === newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

}