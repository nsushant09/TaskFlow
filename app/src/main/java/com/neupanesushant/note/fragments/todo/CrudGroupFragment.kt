package com.neupanesushant.note.fragments.todo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentCrudGroupBinding
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.Utils.hideKeyboard
import org.koin.android.ext.android.inject

class CrudGroupFragment(private val onSuccessListener: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentCrudGroupBinding
    private val binding get() = _binding

    private var group: TaskGroup? = null
    private val viewModel: TodoHomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrudGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            group = it.getParcelable("group")
        }

        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Utils.showKeyboard(requireActivity(), binding.etAddGroupName)
        }
        group?.let {
            binding.etAddGroupName.setText(it.name.toString())
            binding.btnDeleteGroup.visibility = View.VISIBLE
        }
    }

    private fun setupEventListener() {
        binding.btnSaveGroup.setOnClickListener {
            if (isValidGroupName()) {
                if (group == null) {
                    viewModel.addNewGroup(binding.etAddGroupName.text.toString()) {
                        onSuccessListener()
                    }
                } else {
                    group!!.name = binding.etAddGroupName.text.toString()
                    viewModel.updateGroup(group!!) {
                        onSuccessListener()
                    }
                }
                this.dismiss()
            }
        }

        binding.btnDeleteGroup.setOnClickListener {
            group?.let {
                viewModel.deleteGroup(it) {
                    onSuccessListener()
                }
                this.dismiss()
            }
        }
    }

    private fun setupObserver() {

    }

    private fun isValidGroupName(): Boolean {
        if (binding.etAddGroupName.text.toString().isEmpty()) {
            binding.tilAddGroupName.error = "Enter a group name"
            return false;
        }
        return true;
    }

    companion object {
        fun getInstance(onSuccessListener: () -> Unit): CrudGroupFragment {
            return CrudGroupFragment(onSuccessListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().hideKeyboard()
    }
}