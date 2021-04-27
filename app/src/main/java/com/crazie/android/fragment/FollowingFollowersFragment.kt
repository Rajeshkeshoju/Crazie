package com.crazie.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.crazie.android.R
import com.crazie.android.adapter.FollowingFollowersAdapter
import com.crazie.android.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowingFollowersFragment(private var tab: Int, private val uId: String) : Fragment() {

    private lateinit var tabLayout:TabLayout
    private lateinit var viewpager:ViewPager

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarBack:ImageView
    private lateinit var toolbarTitle:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_following_followers, container, false)

        bindViews(v)
        init()
        clickListeners()
        return v

    }

    private fun clickListeners() {
        toolbarBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tabLayout.addTab(tabLayout.newTab().setText("Followers"))
        tabLayout.addTab(tabLayout.newTab().setText("Following"))

        val adapter = FollowingFollowersAdapter(childFragmentManager,tabLayout.tabCount,uId)

        viewpager.adapter = adapter

        viewpager.currentItem = tab


    }

    private fun init() {

        FirebaseDatabase.getInstance().getReference("users")
                .child(uId)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        toolbarTitle.text = snapshot.child("userName").value.toString()
                        if (snapshot.child("accountVerified").value as Boolean){
                            toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(
                                    0,0,R.drawable.ic_verified,0)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view!!.context.toast("Something went wrong")
                    }

                })

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager.currentItem  = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun bindViews(v:View) {
        tabLayout = v.findViewById(R.id.tab_layout)
        viewpager = v.findViewById(R.id.viewpager)

        toolbar = v.findViewById(R.id.toolbar_following_followers)
        toolbarBack = toolbar.findViewById(R.id.arrow_back)
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title)
    }

}