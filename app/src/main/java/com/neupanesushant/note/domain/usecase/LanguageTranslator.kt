package com.neupanesushant.note.domain.usecase

import android.content.Context
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.neupanesushant.note.R
import com.neupanesushant.note.extras.CallbackAction
import com.neupanesushant.note.extras.GenericCallback

class LanguageTranslator(private val context: Context) {

    companion object{
        val countryCodes = linkedMapOf<String, String>()
    }

    init{
        getCountriesData()
    }
    private fun getTranslateClient(sourceCode : String, targetCode : String): Translator {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceCode)
            .setTargetLanguage(targetCode)
            .build()

        return Translation.getClient(options)
    }
    private fun modelDownloadConditions(): DownloadConditions {
        return DownloadConditions.Builder()
            .requireWifi()
            .build()
    }
    private fun translateExecution(text: String, sourceCode : String, targetCode : String, callback : GenericCallback<String>) {
        getTranslateClient(sourceCode, targetCode) .translate(text)
            .addOnSuccessListener {
                callback.callback(it.toString(), CallbackAction.SUCCESS)
            }
            .addOnFailureListener {
                if(it.localizedMessage == null){
                    callback.callback("Could not translate.", CallbackAction.FAILURE)
                }else{
                    callback.callback(it.localizedMessage!!, CallbackAction.FAILURE)
                }
            }
    }

    fun translate(text: String, sourceCode : String, targetCode : String, callback : GenericCallback<String>){
        getTranslateClient(sourceCode, targetCode).downloadModelIfNeeded(modelDownloadConditions())
            .addOnSuccessListener{ translateExecution(text, sourceCode, targetCode, callback) }
            .addOnFailureListener { callback.callback("Check your internet connection", CallbackAction.FAILURE)}
    }

    private fun getCountriesData() {
        if(countryCodes.isNotEmpty())
            return

        val jsonReader = JsonReader(context)
        jsonReader.readFromRawFile(R.raw.ml_countries){jsonObject ->
            countryCodes[jsonObject.getString("language_name")] = jsonObject.getString("language_code")
        }
    }
}