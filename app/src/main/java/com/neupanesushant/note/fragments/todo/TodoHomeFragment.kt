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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.note.R
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.databinding.FragmentTodoHomeBinding
import com.neupanesushant.note.databinding.ItemTodoGroupBinding
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import org.koin.android.ext.android.inject

class TodoHomeFragment : Fragment() {

    private lateinit var _binding: FragmentTodoHomeBinding
    private val binding get() = _binding
    private val viewModel: TodoHomeViewModel by inject()

    private val onGroupClick: (id: Int) -> Unit = { id ->

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    private fun setupView() {
        binding.ListsTitle.animation = AnimationUtils.loadAnimation(
            requireContext(),
            androidx.appcompat.R.anim.abc_slide_in_top
        )

        binding.rvAllGroupLists.animation = AnimationUtils.loadAnimation(
            context,
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )

        binding.rvAllGroupLists.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        binding.btnAddGroup.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        searchButtonListener()

        binding.btnAddGroup.setOnClickListener {
            val addGroupFragment = AddGroupFragment.getInstance()
            addGroupFragment.show(parentFragmentManager, addGroupFragment::class.java.name)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        viewModel.allGroup.observe(viewLifecycleOwner) {
            binding.rvAllGroupLists.adapter = GenericRecyclerAdapter(
                it,
                ItemTodoGroupBinding::class.java
            ) { binding: ItemTodoGroupBinding, item: TaskGroup, _: List<TaskGroup> ->

                val completed = item.tasks.filter { it.isCompleted }.size

                binding.apply {
                    tvGroupName.text = item.name
                    tvTotalTaskValue.text = "${item.tasks.size} tasks"
                    tvCompletedTaskValue.text = "$completed completed"
                    lpiTaskProgress.setProgress((completed / item.tasks.size) * 100, true)
                }

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
}