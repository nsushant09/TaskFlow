package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentCrudTaskBinding
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.Utils.hideKeyboard
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat

class CrudTaskFragment(private val callback: GenericCallback<Task>?) : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentCrudTaskBinding
    private val binding get() = _binding

    private var groupId = -1
    private var task: Task? = null
    private val viewModel: TodoTaskViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrudTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null)
            activity?.onBackPressed()

        if (arguments?.getInt("groupId") == null)
            activity?.onBackPressed()

        groupId = arguments!!.getInt("groupId")
        task = arguments!!.getParcelable("task")
        viewModel.setGroupId(groupId)

        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Utils.showKeyboard(requireActivity(), binding.etTaskName)
        }

        task?.let { task ->
            binding.etTaskName.setText(task.title)
            binding.etTaskDetails.setText(task.description)
            binding.etDate.setText(task.date)
            binding.btnDeleteTask.visibility = View.VISIBLE
        }
    }

    private fun setupEventListener() {
        binding.etDate.setOnClickListener {
            showCalenderPicker()
        }

        binding.btnDeleteTask.setOnClickListener {
            task?.let { task ->
                viewModel.deleteTask(task)
                callback?.callback(task, CallbackAction.DELETE)
                this.dismissAllowingStateLoss()
            }
        }


        binding.btnSaveTask.setOnClickListener {

            if (binding.etTaskName.text == null || binding.etTaskName.text!!.isEmpty()) {
                binding.etTaskName.error = "Enter task name"
                return@setOnClickListener
            }
            if (task == null) {
                addTask()
            } else {
                updateTask()
            }

            this.dismissAllowingStateLoss()

        }
    }

    private fun addTask() {
        viewModel.addTask(
            binding.etTaskName.text.toString(),
            binding.etTaskDetails.text.toString(),
            binding.etDate.text.toString()
        )
    }

    private fun updateTask() {
        task?.let { task ->
            if (
                task.title != binding.etTaskName.text.toString() ||
                task.description != binding.etTaskDetails.text.toString() ||
                task.date != binding.etDate.text.toString()
            ) {
                task.apply {
                    title = binding.etTaskName.text.toString()
                    description = binding.etTaskDetails.text.toString()
                    date = binding.etDate.text.toString()
                }
                viewModel.updateTask(task)
            }
        }
    }

    private fun setupObserver() {

    }

    @SuppressLint("SimpleDateFormat")
    fun showCalenderPicker() {
        val constraint =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()
        val calenderPicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.CustomMaterialCalendar)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setCalendarConstraints(constraint)
            .setTitleText("Set Date")
            .build()

        calenderPicker.show(parentFragmentManager, "")
        calenderPicker.addOnPositiveButtonClickListener {
            binding.etDate.setText(
                SimpleDateFormat("dd/MM/yyyy").format(calenderPicker.selection).toString()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().hideKeyboard()
    }

    companion object {
        fun getInstance(callback: GenericCallback<Task>): CrudTaskFragment {
            return CrudTaskFragment(callback)
        }

    }
}