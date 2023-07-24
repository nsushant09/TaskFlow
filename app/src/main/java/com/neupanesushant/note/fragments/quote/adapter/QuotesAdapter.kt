package com.neupanesushant.note.fragments.quote.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.ItemAllQuoteBinding
import com.neupanesushant.note.domain.model.Quote

class QuotesAdapter(private val context: Context, private val list: List<Quote>) :
    RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAllQuoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAllQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position % 5) {
            0 -> holder.binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkblue)
            1 -> holder.binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkcreame)
            2 -> holder.binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkpink)
            3 -> holder.binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkgreen)
            4 -> holder.binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkyellow)
        }

        if (position % 2 == 0) {
            holder.binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        } else {
            holder.binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        }
        holder.binding.tvQuoteContent.text = list[position].body
    }
}
