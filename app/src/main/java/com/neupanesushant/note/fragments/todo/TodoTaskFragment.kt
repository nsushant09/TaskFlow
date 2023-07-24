package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTodoTaskBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.fragments.todo.adapter.TaskRecyclerAdapter
import org.koin.android.ext.android.inject

class TodoTaskFragment : Fragment() {

    private lateinit var _binding: FragmentTodoTaskBinding
    private val binding get() = _binding

    private lateinit var groupName: String
    private var groupId = -1

    private val viewModel: TodoTaskViewModel by inject()
    private var allTaskAdapter: TaskRecyclerAdapter? = null

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

        val anim = Utils.getRawFiles(requireContext(), "lottie_empty_list")
        binding.layoutEmptyMessage.emptyAnimationView.setAnimation(anim)
        binding.layoutEmptyMessage.tvEmptyMessage.text =
            "There are no tasks.\nClick on the Add Button below to add new tasks"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        binding.btnAddTask.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("groupId", groupId)
            routeToAddUpdateFragment(bundle)
        }

        binding.etSearch.addTextChangedListener {
            if (it == null || it.isEmpty()) {
                viewModel.refreshTasksToDisplay()
            } else {
                viewModel.searchTasksWithTarget(it.toString())
            }
        }

        searchButtonListener()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setupObserver() {

        viewModel.tasksToDisplay.observe(this) {

            it?.let { list ->
                binding.tvTotalTaskValue.text = "${list.size} tasks "
                binding.tvCompletedTaskValue.text =
                    " ${list.filter { it.isCompleted }.size} completed"
            }

            if (it == null || it.isEmpty()) {
                binding.rvAllTasks.visibility = View.GONE
                binding.layoutEmptyMessage.layout.visibility = View.VISIBLE
                return@observe
            } else {
                binding.rvAllTasks.visibility = View.VISIBLE
                binding.layoutEmptyMessage.layout.visibility = View.GONE
            }

            if (allTaskAdapter == null) {
                allTaskAdapter =
                    TaskRecyclerAdapter(requireContext(), it, object : GenericCallback<Task> {
                        override fun callback(data: Task, action: CallbackAction) {
                            if (action == CallbackAction.TOGGLE) {
                                viewModel.updateTask(data)
                            }
                            if (action == CallbackAction.CLICK) {
                                val bundle = Bundle()
                                bundle.putParcelable("task", data)
                                bundle.putInt("groupId", groupId)
                                routeToAddUpdateFragment(bundle)
                            }
                        }
                    })
            } else {
                allTaskAdapter?.let { taskRecyclerAdapter ->
                    taskRecyclerAdapter.list = it
                    taskRecyclerAdapter.notifyDataSetChanged()
                }
            }
            binding.rvAllTasks.adapter = allTaskAdapter
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

    private fun routeToAddUpdateFragment(bundle: Bundle) {
        val crudTaskFragment =
            CrudTaskFragment.getInstance(callback = object : GenericCallback<Task> {
                override fun callback(data: Task, action: CallbackAction) {
                    if (action == CallbackAction.DELETE) {
                        Utils.showSnackBar(
                            requireContext(),
                            binding.root,
                            "Accidentally deleted?",
                            "UNDO",
                            Snackbar.LENGTH_LONG
                        ) {
                            viewModel.undoDelete(task = data)
                        }
                    }
                }
            })
        crudTaskFragment.arguments = bundle
        crudTaskFragment.show(parentFragmentManager, crudTaskFragment::class.java.name)
    }

    companion object {
        fun getInstance() = TodoTaskFragment()
    }
}