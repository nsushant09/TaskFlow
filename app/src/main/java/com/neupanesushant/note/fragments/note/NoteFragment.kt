package com.neupanesushant.note.fragments.note

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentNoteBinding
import com.neupanesushant.note.databinding.ItemAllNoteBinding
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.adapter.GenericRecyclerAdapter
import org.koin.android.ext.android.inject


class NoteFragment : Fragment() {

    private lateinit var _binding: FragmentNoteBinding
    private val binding get() = _binding
    private lateinit var adapter: GenericRecyclerAdapter<NoteDetails, ItemAllNoteBinding>
    private val viewModel: NoteViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    private val onNoteLayoutClick: (NoteDetails) -> Unit = { noteDetails ->
        val intent = Intent(requireContext(), AddNoteActivity::class.java)
        intent.putExtra("isOpenedFromNoteLayout", true)
        intent.putExtra("currentNoteObject", noteDetails)
        startActivity(intent)

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
        binding.rvAllNotes.animation = AnimationUtils.loadAnimation(
            context, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )
        binding.rvAllNotes.layoutManager = LinearLayoutManager(context)

        binding.NotesTitle.animation = AnimationUtils.loadAnimation(
            requireContext(), androidx.appcompat.R.anim.abc_slide_in_top
        )
        binding.layoutEmptyMessage.tvEmptyMessage.animation =
            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)

        binding.btnAddNote.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)

        binding.layoutEmptyMessage.tvEmptyMessage.text =
            "There are no notes.\nClick on the Add Button below to add new notes"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        binding.btnAddNote.setOnClickListener {
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            intent.putExtra("isOpenedFromNoteLayout", false)
            startActivity(intent)
        }

        searchButtonListener()
        searchBarChangeListener()
    }

    private fun setupObserver() {
        viewModel.notesToDisplay.observe(viewLifecycleOwner) {

            if (it == null || it.isEmpty()) {
                binding.rvAllNotes.visibility = View.GONE
                binding.layoutEmptyMessage.tvEmptyMessage.visibility = View.VISIBLE
                return@observe
            } else {
                binding.rvAllNotes.visibility = View.VISIBLE
                binding.layoutEmptyMessage.tvEmptyMessage.visibility = View.GONE
            }

            it.let {
                if (binding.rvAllNotes.adapter == null) {
                    adapter = GenericRecyclerAdapter(
                        it, ItemAllNoteBinding::class.java
                    ) { binding: ItemAllNoteBinding, item: NoteDetails, _: List<NoteDetails>, position: Int ->
                        when (position % 5) {
                            1 -> binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightblue)
                            2 -> binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightcreame)
                            3 -> binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightgreen)
                            4 -> binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightpink)
                            0 -> binding.allNoteLinearLayout.setBackgroundResource(R.drawable.all_note_bg_lightorange)
                        }
                        if (position % 2 == 0) {
                            binding.root.animation =
                                AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
                        } else {
                            binding.root.animation =
                                AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        }

                        binding.tvTitle.text = item.title
                        binding.tvDescription.text = item.description
                        binding.tvDate.text = item.date
                        binding.root.setOnClickListener {
                            onNoteLayoutClick(item)
                        }
                    }
                    binding.rvAllNotes.adapter = adapter
                } else {
                    adapter.refreshData(it)
                }
            }
        }
    }

    private fun setupNotesToDisplay() {

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

    private fun searchBarChangeListener() {
        binding.etSearch.addTextChangedListener {
            if (it == null || it.isEmpty()) {
                viewModel.refreshNotesToDisplay()
            } else {
//                viewModel.searchNoteWithString(it.toString())

            }
        }
    }
}