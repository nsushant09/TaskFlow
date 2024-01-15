package com.neupanesushant.note.fragments.note

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentNoteBinding
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import org.koin.android.ext.android.inject


class NoteFragment : Fragment() {

    private lateinit var _binding: FragmentNoteBinding
    private val binding get() = _binding
    private var adapter: NoteAdapter? = null
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

        val anim = Utils.getRawFiles(requireContext(), "lottie_empty_list")
        binding.layoutEmptyMessage.emptyAnimationView.setAnimation(anim)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {

        viewModel.notes.observe(viewLifecycleOwner) {

            setNotesVisibility(!it.isNullOrEmpty())

            if (adapter == null) {
                setupNoteAdapter(it)
            } else {
                refreshNoteAdapter(it)
            }
        }
    }

    private fun setNotesVisibility(isVisible: Boolean) {
        binding.rvAllNotes.isVisible = isVisible
        binding.layoutEmptyMessage.layout.isVisible = !isVisible
    }

    private fun setupNoteAdapter(list: List<NoteDetails>) {
        adapter = NoteAdapter(
            requireContext(),
            list,
            object : GenericCallback<NoteDetails> {
                override fun callback(data: NoteDetails, action: CallbackAction) {
                    if (action == CallbackAction.CLICK) {
                        onNoteLayoutClick(data)
                    }
                }
            })
        binding.rvAllNotes.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshNoteAdapter(list: List<NoteDetails>) {
        adapter?.list = list
        adapter?.notifyDataSetChanged()
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
            viewModel.notes.value?.let { notes ->
                var filtered = notes
                if (!it.isNullOrEmpty()) {
                    filtered = filtered.filter { note ->
                        Utils.isTargetInString(note.title, it.toString())
                    }
                }

                adapter?.let { adapter ->
                    if (adapter.list != filtered) {
                        refreshNoteAdapter(notes)
                    }
                }
            }
        }
    }
}