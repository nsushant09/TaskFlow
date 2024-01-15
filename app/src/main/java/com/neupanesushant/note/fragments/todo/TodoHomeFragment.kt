package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTodoHomeBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.fragments.todo.adapter.GroupAdapter
import com.neupanesushant.note.fragments.todo.adapter.TaskRecyclerAdapter
import org.koin.android.ext.android.inject

class TodoHomeFragment : Fragment() {

    private lateinit var _binding: FragmentTodoHomeBinding
    private val binding get() = _binding
    private val viewModel: TodoHomeViewModel by inject()
    private val todoTaskViewModel: TodoTaskViewModel by inject()

    private var allGroupsAdapter: GroupAdapter? = null
    private var todayTaskAdapter: TaskRecyclerAdapter? = null

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
            val crudGroupFragment = CrudGroupFragment.getInstance {
                viewModel.refreshData()
            }
            crudGroupFragment.show(parentFragmentManager, crudGroupFragment::class.java.name)
        }
    }

    private fun setupObserver() {
        setupAllGroupList()
        setupTodayTasksList()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setupAllGroupList() {
        viewModel.allGroup.observe(viewLifecycleOwner) {
            setAddGroupVisibility(!it.isNullOrEmpty())
            if (allGroupsAdapter == null) {
                setupAllGroupAdapter(it)
            } else {
                refreshAllGroupAdapter(it)
            }
        }
    }

    private fun setAddGroupVisibility(isVisible: Boolean) {
        binding.rvAllGroupLists.isVisible = isVisible
        binding.layoutEmptyMessageGroup.layout.isVisible = !isVisible
    }

    private fun setupAllGroupAdapter(list: List<TaskGroupWithAllTasks>) {
        allGroupsAdapter = GroupAdapter(
            requireContext(),
            list,
            object : GenericCallback<TaskGroupWithAllTasks> {
                override fun callback(data: TaskGroupWithAllTasks, action: CallbackAction) {
                    if (action == CallbackAction.CLICK) {
                        groupClick(data)
                    }

                    if (action == CallbackAction.LONG_CLICK) {
                        groupLongClick(data)
                    }
                }
            })
        binding.rvAllGroupLists.adapter = allGroupsAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAllGroupAdapter(list: List<TaskGroupWithAllTasks>) {
        allGroupsAdapter?.list = list
        allGroupsAdapter?.notifyDataSetChanged()
    }


    private fun groupClick(item: TaskGroupWithAllTasks) {
        val bundle = Bundle()
        bundle.putString("groupName", item.name)
        bundle.putInt("groupId", item.id)
        replaceFragment(TodoTaskFragment.getInstance(), bundle)
    }

    private fun groupLongClick(item: TaskGroupWithAllTasks) {
        val bundle = Bundle()
        bundle.putParcelable("group", TaskGroup(item.id, item.name))
        val crudGroupFragment = CrudGroupFragment.getInstance {
            viewModel.refreshData()
        }
        crudGroupFragment.arguments = bundle
        crudGroupFragment.show(
            parentFragmentManager,
            crudGroupFragment::class.java.name
        )
    }

    private fun setupTodayTasksList() {
        viewModel.todayEndingTasks.observe(this) {
            setTodayTasksVisibility(!it.isNullOrEmpty())
            if (todayTaskAdapter == null) {
                setupTodayTaskAdapter(it)
            } else {
                refreshTodayTaskAdapter(it)
            }
        }
    }

    private fun setTodayTasksVisibility(isVisible: Boolean) {
        binding.rvTodayTask.isVisible = isVisible
        binding.layoutEmptyMessageTodayTask.layout.isVisible = !isVisible
    }

    private fun setupTodayTaskAdapter(list: List<Task>) {
        todayTaskAdapter =
            TaskRecyclerAdapter(requireContext(), list, object : GenericCallback<Task> {
                override fun callback(data: Task, action: CallbackAction) {
                    if (action == CallbackAction.TOGGLE) {
                        todoTaskViewModel.updateTask(data) {
                            viewModel.refreshData()
                        }
                    }
                    if (action == CallbackAction.CLICK) {
                        val bundle = Bundle()
                        bundle.putParcelable("task", data)
                        bundle.putInt("groupId", data.groupId)
                        routeToAddUpdateFragment(bundle)
                    }
                }
            })
        binding.rvTodayTask.adapter = todayTaskAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshTodayTaskAdapter(list: List<Task>) {
        todayTaskAdapter?.list = list
        todayTaskAdapter?.notifyDataSetChanged()
    }

    private fun routeToAddUpdateFragment(bundle: Bundle) {
        val crudTaskFragment = CrudTaskFragment.getInstance(callback = object :
            GenericCallback<Task> {
            override fun callback(data: Task, action: CallbackAction) {
                if (action == CallbackAction.DELETE) {
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

}