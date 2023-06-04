package com.neupanesushant.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.neupanesushant.note.databinding.ActivityMainBinding
import com.neupanesushant.note.fragments.note.NoteFragment
import com.neupanesushant.note.fragments.quote.QuoteFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val noteFragment = NoteFragment()
    private val quoteFragment = QuoteFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        replaceFragment(noteFragment)
        binding.bottomnavigationbar.setItemSelected(R.id.note, true)
        currentFragmentListener(binding.bottomnavigationbar)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }


    fun currentFragmentListener(navigationBarView: ChipNavigationBar) {
        navigationBarView.setOnItemSelectedListener {
            when (it) {
                R.id.note -> {
                    replaceFragment(noteFragment)
                }
                R.id.quote -> {
                    replaceFragment(quoteFragment)
                }
            }
        }
    }
}