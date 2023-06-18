package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTodoTaskBinding
import com.neupanesushant.note.databinding.ItemAllTaskBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.Utils.showText
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import org.koin.android.ext.android.inject

class TodoTaskFragment() : Fragment() {

    private lateinit var _binding: FragmentTodoTaskBinding
    private val binding get() = _binding

    private lateinit var groupName: String
    private var groupId = -1;
    private val viewModel: TodoTaskViewModel by inject()

    private lateinit var allTasksAdapter: GenericRecyclerAdapter<Task, ItemAllTaskBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null)
            activity?.onBackPressed()

        if (arguments?.getInt("groupId") == null || arguments?.getString("groupName") == null)
            activity?.onBackPressed()

        groupName = arguments!!.getString("groupName").toString()
        groupId = arguments!!.getInt("groupId")
        viewModel.setGroupId(groupId)
        viewModel.fetchAllTasks()

        setupView()
        setupEventListener()
        setupObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {

        binding.tvGroupName.text = groupName

        binding.rvAllTasks.animation = AnimationUtils.loadAnimation(
            context, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )
        binding.rvAllTasks.layoutManager = LinearLayoutManager(context)
        binding.tvGroupName.animation = AnimationUtils.loadAnimation(
            requireContext(), androidx.appcompat.R.anim.abc_slide_in_top
        )

        binding.layoutEmptyMessage.tvEmptyMessage.animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        binding.btnAddTask.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)

        binding.layoutEmptyMessage.tvEmptyMessage.text =
            "There are no tasks.\nClick on the Add Button below to add new tasks"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        binding.btnAddTask.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("groupId", groupId)

            val addTaskFragment = AddTaskFragment.getInstance()
            addTaskFragment.arguments = bundle
            addTaskFragment.show(parentFragmentManager, addTaskFragment::class.java.name)
        }

        binding.etSearch.addTextChangedListener {
            if (it == null || it.isEmpty()) {
                viewModel.refreshTasks()
            } else {
                viewModel.searchTaskWithString(it.toString())
            }
        }

        searchButtonListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        viewModel.tasksToDisplay.observe(this) {

            it?.let {
                binding.tvTotalTaskValue.text = "${it.size} tasks "
                binding.tvCompletedTaskValue.text = " ${it.filter { it.isCompleted }.size} completed"
            }

            if (it.isEmpty()) {
                binding.rvAllTasks.visibility = View.GONE
                binding.layoutEmptyMessage.tvEmptyMessage.visibility = View.VISIBLE
                return@observe
            } else {
                binding.rvAllTasks.visibility = View.VISIBLE
                binding.layoutEmptyMessage.tvEmptyMessage.visibility = View.GONE
            }

            if (binding.rvAllTasks.adapter == null) {
                allTasksAdapter = GenericRecyclerAdapter(
                    it,
                    ItemAllTaskBinding::class.java
                ) { binding: ItemAllTaskBinding, item: Task, _: List<Task>, position: Int ->
                    binding.tvTaskName.text = item.title
                    binding.tvTaskDetails.text = item.description
                    binding.tvDate.text = item.date

                    if(item.isCompleted)
                        binding.btnToggleCompleted.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_tick_filled))
                    else
                        binding.btnToggleCompleted.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bullseye))

                    binding.btnToggleCompleted.setOnClickListener {
                        viewModel.updateTask(item.copy(isCompleted = !item.isCompleted))
                    }

                    if (item.description.isEmpty())
                        binding.tvTaskDetails.visibility = View.GONE

                    if (item.date.isEmpty())
                        binding.tvDate.visibility = View.GONE
                }
                binding.rvAllTasks.adapter = allTasksAdapter
            } else {
                allTasksAdapter.refreshData(it)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun searchButtonListener() {
        binding.btnSearch.setOnClickListener {
            if (viewModel.isSearchFieldVisible.value!!) {
                binding.etSearch.clearFocus()
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
                binding.etSearch.visibility = View.GONE
                Utils.showKeyboard(activity, binding.etSearch)
                viewModel.setSearchFieldVisibility(false)
            } else {
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
                Utils.hideKeyboard(activity, binding.etSearch)
                viewModel.setSearchFieldVisibility(true)
            }
        }
    }

    companion object {
        fun getInstance() = TodoTaskFragment()
    }
}