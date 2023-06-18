package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
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
import com.neupanesushant.note.databinding.FragmentAddTaskBinding
import com.neupanesushant.note.extras.Utils.showText
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

class AddTaskFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentAddTaskBinding
    private val binding get() = _binding

    private var groupId = -1;
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
    private fun setupEventListener() {
        binding.etDate.setOnClickListener {
            showCalenderPicker()
        }

        binding.btnSaveTask.setOnClickListener {

            if (binding.etTaskName.text == null || binding.etTaskName.text!!.isEmpty()) {
                binding.etTaskName.error = "Enter task name"
            } else {
                viewModel.addTask(
                    binding.etTaskName.text.toString(),
                    binding.etTaskDetails.text.toString(),
                    binding.etDate.text.toString()
                )

                requireContext().showText("Task added")
                this.dismissAllowingStateLoss()
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

    companion object {
        fun getInstance(): AddTaskFragment {
            return AddTaskFragment()
        }
    }
}