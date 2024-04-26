package com.and102.cinemania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.and102.cinemania.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize with FoodListFragment
        replaceFragment(HomePageFragment())

        // Set the bottom navigation listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav-> replaceFragment(HomePageFragment())
                R.id.search_nav-> replaceFragment(SearchFragment())
                R.id.history_nav-> replaceFragment(HistoryFragment())
                R.id.profile_nav-> replaceFragment(UserFragment())
            }
            true
        }

    }

    // Helper method to perform the fragment replacement
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
