package com.neupanesushant.note

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.note.databinding.ActivityAddNoteBinding
import com.neupanesushant.note.model.NoteDetails
import java.time.LocalDate

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    lateinit var viewModel: AddNoteViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AddNoteViewModel::class.java)

        var idForCurrentNote : Int = -1
        val titleForCurrentNote : String
        val descriptionForCurrentNote : String
        val isOpenedFromNoteLayout = intent.getBooleanExtra("isOpenedFromNoteLayout", false)
        if (isOpenedFromNoteLayout) {
            idForCurrentNote = intent.getIntExtra("idForCurrentNote", 0)
            titleForCurrentNote = intent.getStringExtra("titleForCurrentNote").toString()
            descriptionForCurrentNote = intent.getStringExtra("descriptionForCurrentNote").toString()

            binding.etTitle.setText(titleForCurrentNote)
            binding.etDescription.setText(descriptionForCurrentNote)
        }

        binding.etDescription.setOnClickListener {
            if (binding.etTitle.isFocused) {
                binding.etTitle.clearFocus()
            }
            setupInputStart(binding.etDescription)
        }
        binding.btnBack.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val date = LocalDate.now().toString()
            if(isOpenedFromNoteLayout){
                viewModel.updateNoteDetails(NoteDetails(idForCurrentNote, title, description, date))
            }else{
                viewModel.addNoteDetails(NoteDetails(0, title, description, date))
            }
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupInputStart(editText: EditText) {
        editText.requestFocus()
        val imm: InputMethodManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
        editText.setTextCursorDrawable(0)
    }
}