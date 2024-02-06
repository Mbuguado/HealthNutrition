package com.example.healthnutrition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthnutrition.databinding.ActivityMainBinding
import com.example.healthnutrition.fragments.HomeFragment
import com.example.healthnutrition.fragments.NewsBlogFragment
import com.example.healthnutrition.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment()
        val newsBlogFragment = NewsBlogFragment()
        val profileFragment = ProfileFragment()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }

                R.id.blog -> {
                    setCurrentFragment(newsBlogFragment)
                    Toast.makeText(this, "News Blog", Toast.LENGTH_SHORT).show()
                }
                R.id.profile -> {
                    setCurrentFragment(profileFragment)
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.flFragment, fragment)
                commit()
            }
    }

    fun selectedBottomNavigationItem(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

}