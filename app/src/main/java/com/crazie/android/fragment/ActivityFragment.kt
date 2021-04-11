package com.crazie.android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazie.android.R
import com.crazie.android.adapter.UserActivityAdapter
import com.crazie.android.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityFragment : Fragment() {

    private lateinit var userActivityRecyclerView: RecyclerView
    private lateinit var userActivityAdapter: UserActivityAdapter

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle:TextView

    private  var userActivityList = ArrayList<Post>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_activity, container, false)

        toolbar = v.findViewById(R.id.toolbar_activity)
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Activity"

        userActivityRecyclerView = v.findViewById(R.id.rv_user_activity)
        userActivityRecyclerView.layoutManager =
                LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        userActivityRecyclerView.setHasFixedSize(true)
        userActivityAdapter = UserActivityAdapter(v.context,userActivityList)

        userActivityRecyclerView.adapter = userActivityAdapter

        fetchActivity()

        return v
    }

    private fun fetchActivity() {
//        val dialog = ProgressDialog.progressDialog(this.context!!,activity!!)
//        dialog.setTitle("Loading...")
//        dialog.show()

        FirebaseDatabase.getInstance().getReference("likes")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userActivityList.clear()
                        for (ss in snapshot.children){
                            if (ss.child(FirebaseAuth.getInstance().currentUser!!.uid).exists()){
                                FirebaseDatabase.getInstance().getReference("posts")
                                        .child(ss.key.toString())
                                        .addValueEventListener(object :ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val activityInfo = snapshot.getValue(Post::class.java)
                                                if (activityInfo != null) {
                                                    userActivityList.add(activityInfo)
                                                }

                                                //dialog.dismiss()
                                                userActivityAdapter.notifyDataSetChanged()
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }
                                        })
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }
}