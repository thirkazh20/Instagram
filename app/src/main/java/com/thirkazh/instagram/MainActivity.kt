package com.thirkazh.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thirkazh.instagram.fragment.HomeFragment
import com.thirkazh.instagram.fragment.NotificationFragment
import com.thirkazh.instagram.fragment.ProfileFragment
import com.thirkazh.instagram.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    //Untuk mengaktifkan bottom navigation di Activity
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.nav_home ->{
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_search ->{
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_add_post ->{
                item.isChecked = false
                startActivity(Intent(this@MainActivity,AddPostActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_notification ->{
                moveToFragment(NotificationFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_profile ->{
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // untuk membuild bottom navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    // Function untuk pindah antar fragment
    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }

}