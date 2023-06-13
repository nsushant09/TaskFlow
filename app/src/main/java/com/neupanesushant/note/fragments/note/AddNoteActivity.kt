package com.neupanesushant.note.fragments.note

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.ActivityAddNoteBinding
import com.neupanesushant.note.domain.model.NoteDetails
import org.koin.android.ext.android.inject
import java.time.LocalDate

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private var isOpenedFromNoteLayout: Boolean = false
    private lateinit var currentNoteObject: NoteDetails
    private val viewModel: AddNoteViewModel by inject()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isOpenedFromNoteLayout = intent.getBooleanExtra("isOpenedFromNoteLayout", false)
        if (isOpenedFromNoteLayout) {
            currentNoteObject = intent.getParcelableExtra<NoteDetails>("currentNoteObject")!!
            binding.etTitle.setText(currentNoteObject.title)
            binding.etDescription.setText(currentNoteObject.description)
            setupInputStart(binding.etDescription)
        } else {
            setupInputStart(binding.etTitle)
        }

        setupView()
        setupEventListener()
        setupObserver()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView() {
        setupOptionsMenu()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEventListener() {
        binding.etDescription.setOnClickListener {
            if (binding.etTitle.isFocused) {
                binding.etTitle.clearFocus()
            }
            setupInputStart(binding.etDescription)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupInputStart(editText: EditText) {
        editText.requestFocus()
        val imm: InputMethodManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
        editText.textCursorDrawable = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrUpdateNote() {
        var title = binding.etTitle.text.toString()
        if (title.isEmpty()) {
            title = "Text Note"
        }
        val description = binding.etDescription.text.toString()
        val date = LocalDate.now().toString()
        if (isOpenedFromNoteLayout) {
            currentNoteObject?.let {
                if (it.description != description || it.title != title) {
                    viewModel.updateNoteDetails(
                        NoteDetails(
                            currentNoteObject.id,
                            title,
                            description,
                            date
                        )
                    )
                }
            }
        } else {
            if (description.isNotEmpty() && title.isNotEmpty()) {
                viewModel.addNoteDetails(NoteDetails(0, title, description, date))
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setupOptionsMenu() {
        binding.btnMenu.setOnClickListener {
            val popUpMenu = PopupMenu(this, it)
            popUpMenu.menuInflater.inflate(R.menu.add_note_options_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.addNoteOptionsShare -> onShareClick()
                    R.id.addNoteOptionsDelete -> onDeleteClick()
                }
                true
            }
            popUpMenu.show()
        }
    }

    private fun onShareClick() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, binding.etTitle.text.toString())
            putExtra(Intent.EXTRA_TEXT, binding.etDescription.text.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDeleteClick() {
        if (isOpenedFromNoteLayout) {
            Log.i("AddNote", "Inside here")
            viewModel.deleteNoteDetails(
                NoteDetails(
                    currentNoteObject.id,
                    currentNoteObject.title,
                    currentNoteObject.description,
                    currentNoteObject.date
                )
            )
            finish()
        } else {
            finish()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        addOrUpdateNote()
    }

}