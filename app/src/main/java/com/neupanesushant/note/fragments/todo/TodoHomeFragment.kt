package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.content.res.Resources
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.databinding.FragmentTodoHomeBinding
import com.neupanesushant.note.databinding.ItemAllTaskBinding
import com.neupanesushant.note.databinding.ItemTodoGroupBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.dpToPx
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import org.koin.android.ext.android.inject

class TodoHomeFragment : Fragment() {

    private lateinit var _binding: FragmentTodoHomeBinding
    private val binding get() = _binding
    private val viewModel: TodoHomeViewModel by inject()
    private val todoTaskViewModel: TodoTaskViewModel by inject()

    private lateinit var allGroupsAdapter: GenericRecyclerAdapter<TaskGroupWithAllTasks, ItemTodoGroupBinding>
    private lateinit var todayTasksAdapter: GenericRecyclerAdapter<Task, ItemAllTaskBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupEventListener()
        setupObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {

        binding.GroupsTitle.animation = AnimationUtils.loadAnimation(
            requireContext(), androidx.appcompat.R.anim.abc_slide_in_top
        )

        binding.rvAllGroupLists.animation = AnimationUtils.loadAnimation(
            context,
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )

        binding.rvAllGroupLists.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        binding.rvTodayTask.layoutManager = LinearLayoutManager(requireContext())

        binding.layoutEmptyMessageGroup.tvEmptyMessage.animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)

        binding.layoutEmptyMessageTodayTask.tvEmptyMessage.animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)

        binding.btnAddGroup.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)

        binding.layoutEmptyMessageTodayTask.tvEmptyMessage.text =
            "Finally Some Rest!!\nThere are no tasks today"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        searchButtonListener()

        binding.btnAddGroup.setOnClickListener {
            val addGroupFragment = AddGroupFragment.getInstance()
            addGroupFragment.show(parentFragmentManager, addGroupFragment::class.java.name)
        }

        binding.etSearch.addTextChangedListener {
            if (it == null || it.isEmpty()) {
            } else {
            }
        }
    }

    private fun setupObserver() {
        setupAllGroupList()
        setupTodayTasksList()
    }

    @SuppressLint("SetTextI18n")
    private fun setupAllGroupList() {
        viewModel.allGroup.observe(viewLifecycleOwner) { it ->

            if (it == null || it.isEmpty()) {
                binding.rvAllGroupLists.visibility = View.GONE
                binding.layoutEmptyMessageGroup.tvEmptyMessage.visibility = View.VISIBLE
                return@observe
            } else {
                binding.rvAllGroupLists.visibility = View.VISIBLE
                binding.layoutEmptyMessageGroup.tvEmptyMessage.visibility = View.GONE
            }

            if (binding.rvAllGroupLists.adapter == null) {
                allGroupsAdapter = GenericRecyclerAdapter(
                    it,
                    ItemTodoGroupBinding::class.java
                ) { binding: ItemTodoGroupBinding, item: TaskGroupWithAllTasks, _: List<TaskGroupWithAllTasks>, position: Int ->

                    binding.root.layoutParams.width =
                        ((Resources.getSystem().displayMetrics.widthPixels / 2) - dpToPx(
                            requireContext(),
                            20f
                        )).toInt()

                    if (position % 2 == 0) {
                        binding.root.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
                    } else {
                        binding.root.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                    }

                    val completed = item.tasks.filter { it.isCompleted }.size

                    binding.apply {
                        tvGroupName.text = item.name
                        tvTotalTaskValue.text = "${item.tasks.size} tasks "
                        tvCompletedTaskValue.text = " $completed completed"
                        if (completed < 1)
                            lpiTaskProgress.setProgress(0, false)
                        else {
                            val percentage = (completed.toFloat() / item.tasks.size) * 100
                            lpiTaskProgress.setProgress(percentage.toInt(), false)
                        }
                    }

                    binding.root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("groupName", item.name)
                        bundle.putInt("groupId", item.id)
                        replaceFragment(TodoTaskFragment.getInstance(), bundle)
                    }
                }
                binding.rvAllGroupLists.adapter = allGroupsAdapter
            } else {
                allGroupsAdapter.refreshData(it)
            }

        }
    }

    private fun setupTodayTasksList() {
        viewModel.todayEndingTasks.observe(this) {

            if (it == null || it.isEmpty()) {
                binding.rvTodayTask.visibility = View.GONE
                binding.layoutEmptyMessageTodayTask.tvEmptyMessage.visibility = View.VISIBLE
                return@observe
            } else {
                binding.rvTodayTask.visibility = View.VISIBLE
                binding.layoutEmptyMessageTodayTask.tvEmptyMessage.visibility = View.GONE
            }

            if (binding.rvTodayTask.adapter == null) {
                todayTasksAdapter = GenericRecyclerAdapter(
                    it,
                    ItemAllTaskBinding::class.java
                ) { binding: ItemAllTaskBinding, item: Task, _: List<Task>, _: Int ->
                    binding.tvTaskName.text = item.title
                    binding.tvTaskDetails.text = item.description
                    binding.tvDate.text = item.date

                    if (item.isCompleted)
                        binding.btnToggleCompleted.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_tick_filled
                            )
                        )
                    else
                        binding.btnToggleCompleted.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_bullseye
                            )
                        )

                    if (item.description.isEmpty())
                        binding.tvTaskDetails.visibility = View.GONE

                    if (item.date.isEmpty())
                        binding.tvDate.visibility = View.GONE

                    binding.btnToggleCompleted.setOnClickListener {
                        item.isCompleted = !item.isCompleted
                        todoTaskViewModel.updateTask(item) {
                            viewModel.refreshGroupItem(item.groupId, item.id)
                        }
                    }

                    binding.root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putParcelable("task", item)
                        bundle.putInt("groupId", item.groupId)
                        routeToAddUpdateFragment(bundle)
                    }
                }
                binding.rvTodayTask.adapter = todayTasksAdapter
            } else {
                todayTasksAdapter.refreshData(it)
            }

        }
    }

    private fun routeToAddUpdateFragment(bundle: Bundle) {
        val addTaskFragment = AddTaskFragment.getInstance()
        addTaskFragment.arguments = bundle
        addTaskFragment.show(parentFragmentManager, addTaskFragment::class.java.name)
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        val fragmentTransaction = this@TodoHomeFragment.parentFragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun searchButtonListener() {
        binding.btnSearch.setOnClickListener {
            if (viewModel.isSearchFieldVisible.value!!) {
                binding.etSearch.clearFocus()
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
                binding.etSearch.visibility = View.GONE
                Utils.hideKeyboard(activity, binding.etSearch)
                viewModel.setSearchFieldVisibility(false)
            } else {
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
                Utils.showKeyboard(activity, binding.etSearch)
                viewModel.setSearchFieldVisibility(true)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }
}