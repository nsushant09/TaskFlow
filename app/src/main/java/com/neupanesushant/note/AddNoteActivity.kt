package com.neupanesushant.note

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.note.databinding.ActivityAddNoteBinding
import com.neupanesushant.note.model.NoteDetails
import java.time.LocalDate

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private var isOpenedFromNoteLayout : Boolean = false
    var idForCurrentNote = -1
    lateinit var viewModel: AddNoteViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("AddNote", "On Create is called")
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AddNoteViewModel::class.java)

        val titleForCurrentNote : String
        val descriptionForCurrentNote : String
        isOpenedFromNoteLayout = intent.getBooleanExtra("isOpenedFromNoteLayout", false)
        if (isOpenedFromNoteLayout) {
            idForCurrentNote = intent.getIntExtra("idForCurrentNote", 0)
            titleForCurrentNote = intent.getStringExtra("titleForCurrentNote").toString()
            descriptionForCurrentNote = intent.getStringExtra("descriptionForCurrentNote").toString()

            binding.etTitle.setText(titleForCurrentNote)
            binding.etDescription.setText(descriptionForCurrentNote)
            setupInputStart(binding.etDescription)
        }else{
            setupInputStart(binding.etTitle)
        }

        binding.etDescription.setOnClickListener {
            if (binding.etTitle.isFocused) {
                binding.etTitle.clearFocus()
            }
            setupInputStart(binding.etDescription)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        setupOptionsMenu()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupInputStart(editText: EditText) {
        editText.requestFocus()
        val imm: InputMethodManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
        editText.setTextCursorDrawable(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrUpdateNote(){
        var title = binding.etTitle.text.toString()
        if(title.isEmpty()){
            title = "Text Note"
        }
        val description = binding.etDescription.text.toString()
        val date = LocalDate.now().toString()
        if(isOpenedFromNoteLayout){
            viewModel.updateNoteDetails(NoteDetails(idForCurrentNote, title, description, date))
        }else{
            if(!description.isEmpty()){
                viewModel.addNoteDetails(NoteDetails(0, title, description, date))
            }
        }
    }


    fun setupOptionsMenu(){
        binding.btnMenu.setOnClickListener {
            val popUpMenu = PopupMenu(this, it)
            popUpMenu.menuInflater.inflate(R.menu.add_note_options_menu,popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.addNoteOptionsShare -> onShareClick()
                    R.id.addNoteOptionsDelete -> onDeleteClick()
                }
                true
            }
            popUpMenu.show()
        }
    }

    fun onShareClick(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, binding.etTitle.text.toString())
            putExtra(Intent.EXTRA_TEXT, binding.etDescription.text.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun onDeleteClick(){
        if(isOpenedFromNoteLayout){
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().toString()
            } else {
                ""
            }
            viewModel.deleteNoteDetails(NoteDetails(idForCurrentNote, title, description, date))
            finish()
        }else{
            finish()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        addOrUpdateNote()
    }

}