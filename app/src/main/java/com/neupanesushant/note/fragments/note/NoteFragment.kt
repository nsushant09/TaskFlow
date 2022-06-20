package com.neupanesushant.note.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {

    private lateinit var _binding : FragmentNoteBinding
    private val binding get() = _binding

    lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllNotes.animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom)
        val allNotesAdapter = AllNotesAdapter(requireContext(),listOf())
        binding.rvAllNotes.layoutManager = LinearLayoutManager(context)
        binding.rvAllNotes.adapter = allNotesAdapter
    }
}