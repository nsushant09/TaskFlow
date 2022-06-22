package com.neupanesushant.note.fragments.quote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentQuoteBinding


class QuoteFragment : Fragment() {

    private lateinit var _binding : FragmentQuoteBinding
    private val binding get() = _binding
    private lateinit var viewModel: QuoteViewModel
    lateinit var adapter : AllQuotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuoteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(QuoteViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.QuoteTitle.animation = AnimationUtils.loadAnimation(requireContext(), androidx.appcompat.R.anim.abc_slide_in_top)
        binding.rvAllQuotes.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listofQuotes.observe(viewLifecycleOwner, Observer{
            adapter = AllQuotesAdapter(requireContext(), it)
            binding.rvAllQuotes.adapter = adapter
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer{
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.rvAllQuotes.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.rvAllQuotes.visibility = View.VISIBLE
            }
        })
    }

}