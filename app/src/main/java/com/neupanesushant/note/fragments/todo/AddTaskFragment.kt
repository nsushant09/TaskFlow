package com.neupanesushant.note.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.note.databinding.FragmentAddTaskBinding
import org.koin.android.ext.android.inject

class AddTaskFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentAddTaskBinding
    private val binding get() = _binding

    private var groupId = -1;
    private val viewModel: TodoTaskViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null)
            activity?.onBackPressed()

        if (arguments?.getInt("groupId") == null)
            activity?.onBackPressed()

        groupId = arguments!!.getInt("groupId")
        viewModel.setGroupId(groupId)

        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {}
    private fun setupEventListener() {}
    private fun setupObserver() {}

    companion object {
        fun getInstance(): AddTaskFragment {
            return AddTaskFragment()
        }
    }
}