package com.neupanesushant.note.fragments.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTodoTaskBinding
import com.neupanesushant.note.extras.Utils.showText
import org.koin.android.ext.android.inject

class TodoTaskFragment() : Fragment() {

    private lateinit var _binding: FragmentTodoTaskBinding
    private val binding get() = _binding

    private var groupId = -1;
    private val viewModel: TodoTaskViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoTaskBinding.inflate(layoutInflater)
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

    private fun setupView() {
        binding.rvAllTasks.animation = AnimationUtils.loadAnimation(
            context, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )
        binding.rvAllTasks.layoutManager = LinearLayoutManager(context)
        binding.tasksTitle.animation = AnimationUtils.loadAnimation(
            requireContext(), androidx.appcompat.R.anim.abc_slide_in_top
        )
        binding.btnAddTask.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)

        //TODO : Set Group Name on Top
        //TODO : Set Total Task Value
        //TODO : Set Completed Task Value

    }

    private fun setupEventListener() {
        binding.btnAddTask.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("groupId", groupId)

            val addTaskFragment = AddTaskFragment.getInstance()
            addTaskFragment.arguments = bundle
            addTaskFragment.show(parentFragmentManager, addTaskFragment::class.java.name)
        }

        binding.etSearch.addTextChangedListener {
            //TODO : Filter task on their title
        }
    }

    private fun setupObserver() {
        viewModel.tasksToDisplay.observe(this) {
            //TODO : Set adapter to recyclerview
        }
    }

    companion object {
        fun getInstance() = TodoTaskFragment()
    }
}