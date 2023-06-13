package com.neupanesushant.note.fragments.note

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.AllNoteRecyclerViewLayoutBinding
import com.neupanesushant.note.domain.model.NoteDetails
import kotlin.random.Random

class AllNotesAdapter(val context: Context, val list: List<NoteDetails>, val onNoteLayoutClick : (NoteDetails) -> Unit) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>() {

    inner class ViewHolder(binding: AllNoteRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val layout = binding.allNoteLinearLayout
        val title = binding.tvTitle
        val description = binding.tvDescription
        val date = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AllNoteRecyclerViewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObject = list.get(position)
        val random = Random.nextInt(1, 6)
        when (random) {
            1 -> holder.layout.setBackgroundResource(R.drawable.all_note_bg_lightblue)
            2 -> holder.layout.setBackgroundResource(R.drawable.all_note_bg_lightcreame)
            3 -> holder.layout.setBackgroundResource(R.drawable.all_note_bg_lightgreen)
            4 -> holder.layout.setBackgroundResource(R.drawable.all_note_bg_lightpink)
            5 -> holder.layout.setBackgroundResource(R.drawable.all_note_bg_lightorange)
        }
        if (position % 2 == 0) {
            holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        } else {
            holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        }
        holder.title.text = currentObject.title
        holder.description.text = currentObject.description
        holder.date.text = currentObject.date
        holder.itemView.setOnClickListener{
            onNoteLayoutClick(currentObject)
        }
    }

    fun filterString(searchTitle : String){

    }
    override fun getItemCount(): Int {
        return list.size
    }
}