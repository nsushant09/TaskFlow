package com.neupanesushant.note.fragments.note

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.Genres
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.ActivityAddNoteBinding
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback
import com.neupanesushant.note.extras.Utils
import com.neupanesushant.note.extras.Utils.showText
import com.neupanesushant.note.fragments.TranslateFragment
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
            currentNoteObject = intent.getParcelableExtra("currentNoteObject")!!
            binding.etTitle.setText(currentNoteObject.title)
            binding.etDescription.setText(currentNoteObject.description)
            Utils.showKeyboard(this, binding.etDescription)
        } else {
            Utils.showKeyboard(this, binding.etTitle)
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
            Utils.showKeyboard(this, binding.etDescription)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrUpdateNote() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        val date = LocalDate.now().toString()
        if (isOpenedFromNoteLayout) {
            currentNoteObject.let {
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
        binding.btnMenu.setOnClickListener { menu ->
            val popUpMenu = PopupMenu(this, menu)
            popUpMenu.menuInflater.inflate(R.menu.add_note_options_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.addNoteOptionsShare -> onShareClick()
                    R.id.addNoteOptionsDelete -> onDeleteClick()
                    R.id.addNoteOptionsTranslate -> onTranslateClick()
                }
                true
            }
            popUpMenu.show()
        }
    }

    private fun onTranslateClick(){
        val fragment = TranslateFragment(binding.etDescription.text.toString(),object : GenericCallback<String>{
            override fun callback(data: String, action: CallbackAction) {
                if(action == CallbackAction.SUCCESS){
                    binding.etDescription.setText(data)
                }
                if(action == CallbackAction.FAILURE){
                    this@AddNoteActivity.showText(data)
                }
            }
        })

        Utils.hideKeyboard(this, binding.root)
        fragment.show(supportFragmentManager, fragment.javaClass.name)
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
            viewModel.deleteNoteDetails(
                currentNoteObject
            )
        }

        finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        addOrUpdateNote()
    }

}