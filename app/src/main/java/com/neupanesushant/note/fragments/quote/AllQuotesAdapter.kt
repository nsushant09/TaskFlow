package com.neupanesushant.note.fragments.quote

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.AllQuoteRecyclerViewLayoutBinding
import com.neupanesushant.note.fragments.quote.model.Quote
import kotlin.random.Random

class AllQuotesAdapter(val context : Context, val list : List<Quote>) : RecyclerView.Adapter<AllQuotesAdapter.ViewHolder>(){

    inner class ViewHolder(binding : AllQuoteRecyclerViewLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val layout = binding.allQuoteLinearLayout
        val quoteContent = binding.tvQuoteContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AllQuoteRecyclerViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val random = Random.nextInt(1, 6)
        when (random) {
            1 -> holder.layout.setBackgroundResource(R.drawable.all_quote_bg_darkblue)
            2 -> holder.layout.setBackgroundResource(R.drawable.all_quote_bg_darkcreame)
            3 -> holder.layout.setBackgroundResource(R.drawable.all_quote_bg_darkpink)
            4 -> holder.layout.setBackgroundResource(R.drawable.all_quote_bg_darkgreen)
            5 -> holder.layout.setBackgroundResource(R.drawable.all_quote_bg_darkyellow)
        }
        if (position % 2 == 0) {
            holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        } else {
            holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        }
        holder.quoteContent.text = list.get(position).body
    }

    override fun getItemCount(): Int {
        return list.size
    }

}