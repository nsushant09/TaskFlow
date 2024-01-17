package com.neupanesushant.note.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.note.R
import com.neupanesushant.note.databinding.FragmentTranslateBinding
import com.neupanesushant.note.domain.usecase.LanguageTranslator
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback

class TranslateFragment(private val text: String, private val callback: GenericCallback<String>) :
    BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentTranslateBinding
    private val binding get() = _binding

    private lateinit var languageTranslator: LanguageTranslator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageTranslator = LanguageTranslator(requireContext())

        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupEventListener() {
        binding.btnTranslate.setOnClickListener {
            val sourceCode =
                LanguageTranslator.countryCodes[binding.translateFromTv.text.toString()]
            val targetCode = LanguageTranslator.countryCodes[binding.translateToTv.text.toString()]
            if (sourceCode == null || targetCode == null) {
                callback.callback("Could not translate text", CallbackAction.FAILURE)
                this.dismissAllowingStateLoss()
                return@setOnClickListener
            }
            languageTranslator.translate(text, sourceCode, targetCode, callback)
            this.dismissAllowingStateLoss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        val countries: List<String> = LanguageTranslator.countryCodes.map { codes ->
            codes.key
        }
        val fromArrayAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.autocompleteview_textview, countries)
        val toArrayAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.autocompleteview_textview, countries)
        binding.translateFromTv.setAdapter(fromArrayAdapter)
        binding.translateToTv.setAdapter(toArrayAdapter)
        binding.translateFromTv.setText("English", false) // Set default for translateFromTv
        binding.translateToTv.setText("Spanish", false)
    }

    private fun setupObserver() {

    }
}