package com.crazie.android.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.crazie.android.R
import com.crazie.android.bottomsheet.PostBottomsheet
import com.crazie.android.fragment.AccountFragment
import com.crazie.android.fragment.ActivityFragment
import com.crazie.android.fragment.ExploreFragment
import com.crazie.android.fragment.HomeFragment
import com.crazie.android.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fabPost: FloatingActionButton

    private var tapCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())

        bindViews()

        clickListeners()

    }

    private fun clickListeners() {
        fabPost.setOnClickListener(this)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> { loadFragment(HomeFragment()) }
                R.id.action_explore -> { loadFragment(ExploreFragment()) }
                R.id.action_activity -> { loadFragment(ActivityFragment()) }
                R.id.action_account -> { loadFragment(AccountFragment(FirebaseAuth.getInstance().currentUser!!.uid)) }
            }
            true
        }
        bottomNavigationView.setOnNavigationItemReselectedListener {}
    }

    private fun bindViews() {
        bottomNavigationView = findViewById(R.id.bottomNav)
        fabPost = findViewById(R.id.fab_post)
    }

    override fun onClick(v:View){
        when(v.id){
            R.id.fab_post -> {
                PostBottomsheet().show(supportFragmentManager,"PostBottomSheet")}
        }
    }


    private fun loadFragment(fragment: Fragment):Boolean{

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_out,
                        R.anim.fade_out,
                        R.anim.slide_in,
                        R.anim.fade_in
                )
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit()
        return true
    }

    override fun onBackPressed() {
        if (R.id.action_home != bottomNavigationView.selectedItemId){
            setHomeItem(this)
        }else {
            if (tapCount < 1) {
                tapCount++
                toast("Tap again to exit")
                Handler(Looper.getMainLooper()).postDelayed({
                    tapCount = 0
                }, 1000)
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun setHomeItem(mainActivity: MainActivity) {
        mainActivity.bottomNavigationView.selectedItemId = R.id.action_home
    }



}