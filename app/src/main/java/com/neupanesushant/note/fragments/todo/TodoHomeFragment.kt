package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTodoHomeBinding
import com.neupanesushant.note.databinding.ItemAllTaskBinding
import com.neupanesushant.note.databinding.ItemTodoGroupBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import com.neupanesushant.note.extras.dpToPx
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

        viewModel.refreshData()
        setupView()
        setupEventListener()
        setupObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {

        binding.GroupsTitle.animation = AnimationUtils.loadAnimation(
            requireContext(), androidx.appcompat.R.anim.abc_slide_in_top
        )

        binding.todayTitle.animation = AnimationUtils.loadAnimation(
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



        binding.layoutEmptyMessageGroup.emptyAnimationView.visibility = View.GONE
        binding.layoutEmptyMessageGroup.tvEmptyMessage.text =
            "There are no task groups\nClick on add button to add a new group"

        binding.layoutEmptyMessageTodayTask.emptyAnimationView.animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        val todayAnim = Utils.getRawFiles(requireContext(), "lottie_checklist")
        binding.layoutEmptyMessageTodayTask.emptyAnimationView.setAnimation(todayAnim)
        binding.layoutEmptyMessageTodayTask.tvEmptyMessage.text =
            "All tasks for today completed"
    }

    private fun setupEventListener() {

        binding.btnAddGroup.setOnClickListener {
            val crudGroupFragment = CrudGroupFragment.getInstance() {
                viewModel.refreshData()
            }
            crudGroupFragment.show(parentFragmentManager, crudGroupFragment::class.java.name)
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
                binding.layoutEmptyMessageGroup.layout.visibility = View.VISIBLE
            } else {
                binding.rvAllGroupLists.visibility = View.VISIBLE
                binding.layoutEmptyMessageGroup.layout.visibility = View.GONE
            }

            if (binding.rvAllGroupLists.adapter == null) {
                allGroupsAdapter = GenericRecyclerAdapter(
                    it,
                    ItemTodoGroupBinding::class.java
                ) { binding: ItemTodoGroupBinding, item: TaskGroupWithAllTasks, _: List<TaskGroupWithAllTasks>, _: Int ->

                    binding.root.layoutParams.width =
                        ((Resources.getSystem().displayMetrics.widthPixels / 2) - dpToPx(
                            requireContext(),
                            20f
                        )).toInt()

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

                    binding.root.setOnLongClickListener {
                        val bundle = Bundle()
                        bundle.putParcelable("group", TaskGroup(item.id, item.name))
                        val crudGroupFragment = CrudGroupFragment.getInstance() {
                            viewModel.refreshData()
                        }
                        crudGroupFragment.arguments = bundle
                        crudGroupFragment.show(
                            parentFragmentManager,
                            crudGroupFragment::class.java.name
                        )

                        true
                    }
                }
                binding.rvAllGroupLists.adapter = allGroupsAdapter
            } else {
                allGroupsAdapter.refreshData(it)
            }

        }
    }

    private fun setupTodayTasksList() {

        viewModel.todayEndingTasks.observe(this) { it ->

            if (it == null || it.isEmpty()) {
                binding.rvTodayTask.visibility = View.GONE
                binding.layoutEmptyMessageTodayTask.layout.visibility = View.VISIBLE
            } else {
                binding.rvTodayTask.visibility = View.VISIBLE
                binding.layoutEmptyMessageTodayTask.layout.visibility = View.GONE
            }

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
                        viewModel.refreshData()
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

        }
    }

    private fun routeToAddUpdateFragment(bundle: Bundle) {
        val crudTaskFragment = CrudTaskFragment.getInstance(callback = object :
            GenericCallback<Task> {
            override fun callback(data: Task) {
                Utils.showSnackBar(
                    requireContext(),
                    binding.root,
                    "Accidentally deleted?",
                    "UNDO",
                    Snackbar.LENGTH_LONG
                ) {
                    todoTaskViewModel.undoDelete(task = data)
                }
            }
        })
        crudTaskFragment.arguments = bundle
        crudTaskFragment.show(parentFragmentManager, crudTaskFragment::class.java.name)
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        val fragmentTransaction = this@TodoHomeFragment.parentFragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
    }
}