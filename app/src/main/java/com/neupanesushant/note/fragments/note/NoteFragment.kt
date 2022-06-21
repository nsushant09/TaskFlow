package com.neupanesushant.note.fragments.note

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.AddNoteActivity
import com.neupanesushant.note.AddNoteViewModel
import com.neupanesushant.note.databinding.FragmentNoteBinding
import com.neupanesushant.note.model.NoteDetails



class NoteFragment : Fragment() {

    private lateinit var _binding : FragmentNoteBinding
    private val binding get() = _binding

    lateinit var adapter: AllNotesAdapter
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

    val onNoteLayoutClick: (NoteDetails) -> Unit = {noteDetails ->
//        viewModel.deleteNoteDetails(noteDetails)
        val intent = Intent(requireContext(), AddNoteActivity::class.java)
        intent.putExtra("isOpenedFromNoteLayout", true)
        intent.putExtra("idForCurrentNote", noteDetails.id)
        intent.putExtra("titleForCurrentNote", noteDetails.title)
        intent.putExtra("descriptionForCurrentNote", noteDetails.description)
        startActivity(intent)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllNotes.animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom)
        binding.rvAllNotes.layoutManager = LinearLayoutManager(context)
        viewModel.readAllNote.observe(viewLifecycleOwner, Observer {
            adapter = AllNotesAdapter(requireContext(), it, onNoteLayoutClick)
            binding.rvAllNotes.adapter = adapter
        })
        binding.btnAddNote.setOnClickListener{
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            intent.putExtra("isOpenedFromNoteLayout", false)
            startActivity(intent)
        }


    }
}