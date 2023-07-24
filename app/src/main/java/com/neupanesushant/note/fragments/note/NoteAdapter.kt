package com.neupanesushant.note.fragments.note

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.ItemAllNoteBinding
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback

class NoteAdapter(
    private val context: Context,
    var list: List<NoteDetails>,
    private val callback: GenericCallback<NoteDetails>
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemAllNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAllNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        when (position % 5) {
            1 -> holder.binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightblue)
            2 -> holder.binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightcreame)
            3 -> holder.binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightgreen)
            4 -> holder.binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightpink)
            0 -> holder.binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightorange)
        }
        if (position % 2 == 0) {
            holder.binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        } else {
            holder.binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        }

        holder.binding.tvTitle.text = item.title
        holder.binding.tvDescription.text = item.description
        holder.binding.tvDate.text = item.date
        holder.binding.root.setOnClickListener {
            callback.callback(item, CallbackAction.CLICK)
        }
    }
}