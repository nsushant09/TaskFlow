package com.neupanesushant.note.fragments.note

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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.AddNoteActivity
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentNoteBinding
import com.neupanesushant.note.model.NoteDetails
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel


class NoteFragment : Fragment() {

    private lateinit var _binding: FragmentNoteBinding
    private val binding get() = _binding

    lateinit var adapter: AllNotesAdapter
    lateinit var viewModel: NoteViewModel;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        viewModel = getViewModel<NoteViewModel>()
        return binding.root
    }

    val onNoteLayoutClick: (NoteDetails) -> Unit = { noteDetails ->
        val intent = Intent(requireContext(), AddNoteActivity::class.java)
        intent.putExtra("isOpenedFromNoteLayout", true)
        intent.putExtra("currentNoteObject", noteDetails)
        startActivity(intent)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllNotes.animation = AnimationUtils.loadAnimation(
            context,
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )
        binding.rvAllNotes.layoutManager = LinearLayoutManager(context)
        binding.NotesTitle.animation = AnimationUtils.loadAnimation(requireContext(), androidx.appcompat.R.anim.abc_slide_in_top)
        binding.btnAddNote.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_slide_in_right)

        viewModel.readAllNote.observe(viewLifecycleOwner, Observer {
            adapter = AllNotesAdapter(requireContext(), it, onNoteLayoutClick)
            binding.rvAllNotes.adapter = adapter
        })

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            intent.putExtra("isOpenedFromNoteLayout", false)
            startActivity(intent)
        }

        searchButtonListener()
        searchBarChangeListener()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun searchButtonListener() {
        binding.btnSearch.setOnClickListener {
            if (viewModel.isSearchFieldVisible.value!!) {
                binding.etSearch.clearFocus()
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
                binding.etSearch.visibility = View.GONE
                setupInputStop(binding.etSearch)
                viewModel.setSearchFieldVisibility(false)
            } else {
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
                setupInputStart(binding.etSearch)
                viewModel.setSearchFieldVisibility(true)
            }
        }

    }

    fun searchBarChangeListener() {
        if(binding.etSearch.isVisible){
            binding.etSearch.addTextChangedListener {
                if(it == null || it.length == 0){
                    adapter = AllNotesAdapter(requireContext(), viewModel.readAllNote.value!!, onNoteLayoutClick)
                    binding.rvAllNotes.adapter = adapter
                }else{
                    viewModel.searchNoteWithString(it.toString())
                    adapter = AllNotesAdapter(requireContext(), viewModel.searchedNoteList.value!!, onNoteLayoutClick)
                    binding.rvAllNotes.adapter = adapter
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupInputStart(editText: EditText) {
        editText.requestFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
        editText.setTextCursorDrawable(null)
    }

    fun setupInputStop(editText: EditText) {
        editText.clearFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}