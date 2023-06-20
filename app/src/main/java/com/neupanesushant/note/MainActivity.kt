package com.neupanesushant.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.neupanesushant.note.databinding.ActivityMainBinding
import com.neupanesushant.note.fragments.note.NoteFragment
import com.neupanesushant.note.fragments.quote.QuoteFragment
import com.neupanesushant.note.fragments.quote.QuoteViewModel
import com.neupanesushant.note.fragments.todo.TodoHomeFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val noteFragment = NoteFragment()
    private val quoteFragment = QuoteFragment()
    private val todoHomeFragment = TodoHomeFragment()
    private val quoteViewModel: QuoteViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quoteViewModel.getContentData()
        replaceFragment(todoHomeFragment)
        binding.bottomnavigationbar.setItemSelected(R.id.todo, true)
        currentFragmentListener(binding.bottomnavigationbar)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


    private fun currentFragmentListener(navigationBarView: ChipNavigationBar) {
        navigationBarView.setOnItemSelectedListener {
            when (it) {
                R.id.note -> {
                    replaceFragment(noteFragment)
                }
                R.id.quote -> {
                    replaceFragment(quoteFragment)
                }
                R.id.todo -> {
                    replaceFragment(todoHomeFragment)
                }
            }
        }
    }
}