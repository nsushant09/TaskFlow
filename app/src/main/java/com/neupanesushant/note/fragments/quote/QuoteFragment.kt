package com.neupanesushant.note.fragments.quote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.AllQuoteRecyclerViewLayoutBinding
import com.neupanesushant.note.databinding.FragmentQuoteBinding
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import com.neupanesushant.note.domain.model.Quote
import org.koin.android.ext.android.inject
import kotlin.random.Random


class QuoteFragment : Fragment() {

    private lateinit var _binding: FragmentQuoteBinding
    private val binding get() = _binding
    private lateinit var adapter: AllQuotesAdapter
    private val viewModel: QuoteViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {
        binding.QuoteTitle.animation = AnimationUtils.loadAnimation(
            requireContext(),
            androidx.appcompat.R.anim.abc_slide_in_top
        )
        binding.rvAllQuotes.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObserver() {
        viewModel.listofQuotes.observe(viewLifecycleOwner, Observer {
            var position = 0;
            binding.rvAllQuotes.adapter = GenericRecyclerAdapter(
                it, AllQuoteRecyclerViewLayoutBinding::class.java
            ) { binding: AllQuoteRecyclerViewLayoutBinding, item: Quote, list: List<Quote> ->
                when (Random.nextInt(1, 6)) {
                    1 -> binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkblue)
                    2 -> binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkcreame)
                    3 -> binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkpink)
                    4 -> binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkgreen)
                    5 -> binding.root.setBackgroundResource(R.drawable.all_quote_bg_darkyellow)
                }

                if (position % 2 == 0) {
                    binding.root.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
                } else {
                    binding.root.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                }
                binding.tvQuoteContent.text = item.body
                position++;
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvAllQuotes.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvAllQuotes.visibility = View.VISIBLE
            }
        })
    }

    private fun setupEventListener() {}

}