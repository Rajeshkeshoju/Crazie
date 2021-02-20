package com.crazie.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.crazie.android.fragment.FollowersListFragment
import com.crazie.android.fragment.FollowingListFragment

class FollowingFollowersAdapter(fm: FragmentManager, private var tabCount: Int, private val uId: String) : FragmentStatePagerAdapter(fm,tabCount) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> { FollowersListFragment(uId)}
            1-> { FollowingListFragment(uId)}
            else ->{FollowersListFragment(uId)
            }
        }

    }

    override fun getCount(): Int = tabCount
}