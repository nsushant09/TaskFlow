package com.neupanesushant.note.fragments.quote

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.databinding.FragmentQuoteBinding
import com.neupanesushant.note.domain.model.UIState
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.fragments.quote.adapter.QuotesAdapter
import org.koin.android.ext.android.inject


class QuoteFragment : Fragment() {

    private lateinit var _binding: FragmentQuoteBinding
    private val binding get() = _binding
    private val viewModel: QuoteViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.QuoteTitle.animation = AnimationUtils.loadAnimation(
            requireContext(),
            androidx.appcompat.R.anim.abc_slide_in_top
        )
        binding.rvAllQuotes.layoutManager = LinearLayoutManager(requireContext())

        val errorAnim = Utils.getRawFiles(requireContext(), "lottie_error")
        binding.errorMessageLayout.emptyAnimationView.setAnimation(errorAnim)
        binding.errorMessageLayout.tvEmptyMessage.text = "An error has occured !!!"
    }

    private fun setupEventListener() {}

    private fun setupObserver() {
        observeListOfQuotes()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvAllQuotes.visibility = View.GONE
        binding.errorMessageLayout.layout.visibility = View.GONE
    }

    private fun showData() {
        binding.rvAllQuotes.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.errorMessageLayout.layout.visibility = View.GONE
    }

    private fun showError() {
        binding.errorMessageLayout.layout.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.rvAllQuotes.visibility = View.GONE
    }


    private fun observeListOfQuotes() {
        viewModel.listOfQuotes.observe(viewLifecycleOwner) { uiState ->


            if (uiState is UIState.Success) {
                showData()
                val data = uiState.data
                if (data.isEmpty())
                    viewModel.getContentData()

                val adapter = QuotesAdapter(requireContext(), data)
                binding.rvAllQuotes.adapter = adapter
            }

            if (uiState is UIState.Loading) {
                showProgressBar()
            }

            if (uiState is UIState.Error) {
                showError()
            }
        }
    }

}