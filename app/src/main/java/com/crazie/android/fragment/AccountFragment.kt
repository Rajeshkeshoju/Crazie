package com.crazie.android.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazie.android.EditProfileActivity
import com.crazie.android.LoginActivity
import com.crazie.android.R
import com.crazie.android.adapter.AccountPostAdapter
import com.crazie.android.model.Post
import com.crazie.android.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.abs


class AccountFragment(private val uId: String) : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: DatabaseReference

    private lateinit var accountToolbar: Toolbar
    private lateinit var arrowBack: ImageView
    private lateinit var toolBarTitle: TextView

    private lateinit var userFullName: TextView
    private lateinit var userBio: TextView
    private lateinit var userDP: CircleImageView

    private lateinit var countFollowing:TextView
    private lateinit var countFollowers:TextView
    private lateinit var countPosts:TextView

    private lateinit var accountPosts:RecyclerView
    private lateinit var postAdapter:AccountPostAdapter

    private lateinit var readData:ValueEventListener

    private lateinit var followButton : Button

    private var posts = ArrayList<Post>()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_account, container, false)

        bindView(v)
        init()

        clickListener()

        accountPosts.layoutManager = GridLayoutManager(context,3)
        accountPosts.setHasFixedSize(true)

        postAdapter = AccountPostAdapter(posts)
        accountPosts.adapter = postAdapter

        return v
    }


    private fun init() {

        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(uId)

        setHasOptionsMenu(true)
        val overflowDrawable = ContextCompat.getDrawable(
                activity!!.applicationContext,R.drawable.ic_more)
        accountToolbar.overflowIcon = overflowDrawable
        (activity as AppCompatActivity).setSupportActionBar(accountToolbar)
        accountToolbar.title = ""


        readData = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        setDataToViews(user)
                    }else{
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                }


            override fun onCancelled(error: DatabaseError) {
                Log.w("Error",error.toException())
            }
        }

        dataBase.addValueEventListener(readData)
    }

    private fun setDataToViews(user:User) {
        toolBarTitle.text = user.userName
        if (user.isAccountVerified){
            toolBarTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,0,R.drawable.ic_verified,0)
        }

        userFullName.text = user.fullName
        userBio.text = user.bio
        Glide.with(this@AccountFragment).load(user.photoUrl).into(userDP)

        if (uId != FirebaseAuth.getInstance().currentUser?.uid){
            followButton.visibility = View.VISIBLE
            checkFollowing()
        }


        showPostCount()

        user.uId?.let {
            FirebaseDatabase.getInstance()
                .getReference("follow")
                .child(it)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                            val following = snapshot.child("following").childrenCount.toInt()
                            val followers =  snapshot.child("followers").childrenCount.toInt()

                            formatCount(followers,following)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                    }

                })
        }


    }

    private fun checkFollowing() {

        FirebaseDatabase.getInstance().getReference("follow")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("following")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (uId.let { snapshot.child(it).exists() }){
                            followButton.text = "Following"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun showPostCount() {
        FirebaseDatabase.getInstance().getReference("posts")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var postCount = 0
                        posts.clear()
                        for (ss in snapshot.children){
                            if (ss.child("publisher").value!! == uId){
                                postCount++
                                val post  = ss.getValue(Post::class.java)
                                if (post != null) {
                                   posts.add(post)
                                }

                            }
                        }
                        postAdapter.notifyDataSetChanged()
                        countPosts.text = postCount.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun formatCount(followers: Int, following: Int) {
        when {
            abs(followers/1000) > 1 -> {
                ((followers/1000).toString() + "K").also { countFollowers.text = it }
            }
            abs(followers/1000000) > 1 -> {
                ((followers/1000000).toString() + "M").also { countFollowers.text = it }
            }
            else -> {
                countFollowers.text = followers.toString()
            }
        }

        when {
            abs(following/1000) > 1 -> {
                ((following/1000).toString() + "K").also { countFollowing.text = it }
            }
            abs(following/1000000) > 1 -> {
                ((following/1000000).toString() + "M").also { countFollowing.text = it }
            }
            else -> {
                countFollowing.text = following.toString()
            }
        }

    }

    private fun clickListener() {
        var moreEnable = true

        arrowBack.setOnClickListener{
            activity?.onBackPressed()
        }

        userBio.setOnClickListener {
            if (moreEnable){
                userBio.maxLines = 5
                userBio.ellipsize = null
            }else{
                userBio.maxLines = 3
                userBio.ellipsize = TextUtils.TruncateAt.END
            }
            moreEnable = !moreEnable

        }

        countFollowing.setOnClickListener {
            loadFragment(1,uId)
        }

        countFollowers.setOnClickListener {
            loadFragment(0,uId)
        }

        followButton.setOnClickListener {


            if (followButton.text == "Follow"){
                FirebaseDatabase.getInstance().reference
                        .child("follow")
                        .child(uId)
                        .child("followers")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(true)
                FirebaseDatabase.getInstance().reference
                        .child("follow")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("following")
                        .child(uId)
                        .setValue(true)

                followButton.text = getString(R.string.following)
            }else{
                FirebaseDatabase.getInstance().reference
                        .child("follow")
                        .child(uId)
                        .child("followers")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .removeValue()
                FirebaseDatabase.getInstance().reference
                        .child("follow")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("following")
                        .child(uId)
                        .removeValue()
                followButton.text = getString(R.string.follow)
            }

        }
    }

    private fun loadFragment(tab:Int,uid: String){

        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,FollowingFollowersFragment(tab,uid))
            .addToBackStack(null)
            .commit()
    }

    private fun bindView(v:View) {
        accountToolbar = v.findViewById(R.id.toolbar_account)
        toolBarTitle = accountToolbar.findViewById(R.id.toolbar_title)
        arrowBack = accountToolbar.findViewById(R.id.arrow_back)

        userFullName = v.findViewById(R.id.tv_user_full_name)
        userBio = v.findViewById(R.id.tv_user_bio)
        userDP = v.findViewById(R.id.iv_user_dp)

        countFollowing = v.findViewById(R.id.profile_following_count)
        countFollowers = v.findViewById(R.id.profile_followers_count)
        countPosts = v.findViewById(R.id.profile_posts_count)

        accountPosts = v.findViewById(R.id.rv_account_posts)

        followButton = v.findViewById(R.id.button_fragment_account_follow)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (uId == FirebaseAuth.getInstance().currentUser?.uid){
            inflater.inflate(R.menu.menu_account,menu)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_edit_profile -> {
                startActivity(Intent(activity,EditProfileActivity::class.java))
            }

            R.id.menu_logout -> {
                try {
                    auth.signOut()
                    startActivity(Intent(activity,LoginActivity::class.java))
                    activity?.finish()
                }catch (e:Exception){
                    Log.e("Firebase Signout",e.toString())
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        dataBase.removeEventListener(readData)
        super.onDestroyView()
    }

}