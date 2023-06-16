package com.neupanesushant.note.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.note.databinding.FragmentAddGroupBinding
import org.koin.android.ext.android.inject

class AddGroupFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentAddGroupBinding
    private val binding get() = _binding

    private val viewModel: TodoHomeViewModel by inject()

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

    private fun setupEventListener() {
        binding.btnAddGroup.setOnClickListener {
            if (isValidGroupName()) {
                viewModel.addNewGroup(binding.etAddGroupName.text.toString())
                this.dismiss()
            }
        }
    }

    private fun setupObserver() {

    }

    private fun isValidGroupName(): Boolean {
        if (binding.etAddGroupName.text.toString().isEmpty()) {
            binding.tilAddGroupName.error = "Enter a group name"
            return false;
        }
        return true;
    }

    companion object {
        fun getInstance(): AddGroupFragment {
            return AddGroupFragment()
        }
    }
}