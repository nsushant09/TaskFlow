package com.neupanesushant.note.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.note.databinding.FragmentAddGroupBinding

class AddGroupFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentAddGroupBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {
    }

    private fun setupEventListener() {}

    private fun setupObserver() {}

    companion object {
        fun getInstance(): AddGroupFragment {
            return AddGroupFragment()
        }
    }
}