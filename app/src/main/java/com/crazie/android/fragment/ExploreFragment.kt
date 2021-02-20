package com.crazie.android.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazie.android.R
import com.crazie.android.adapter.SearchUserAdapter
import com.crazie.android.model.SearchUser
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExploreFragment : Fragment() {

    private lateinit var recyclerViewSearchResults:RecyclerView
    private lateinit var userAdapter: SearchUserAdapter
    private var mUsers = ArrayList<SearchUser>()

    private lateinit var searchBar :TextInputLayout

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_explore, container, false)

        recyclerViewSearchResults = v.findViewById(R.id.search_results)
        searchBar = v.findViewById(R.id.et_search)

        recyclerViewSearchResults.setHasFixedSize(true)
        recyclerViewSearchResults.layoutManager = LinearLayoutManager(context)


        readUsers()
        
        searchBar.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        userAdapter = SearchUserAdapter(context, mUsers)
        recyclerViewSearchResults.adapter = userAdapter


        return v
    }

    private fun searchUser(s:String){
        val query = FirebaseDatabase.getInstance().reference
                .child("users")
                .orderByChild("userName")
                .startAt(s)
                .endAt(s+"\uf0ff")
        
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsers.clear()
                for (ss in snapshot.children){
                    val user = ss.getValue(SearchUser::class.java)
                    if (user != null) {
                        mUsers.add(user)
                    }
                }

                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context
                        ,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun readUsers(){
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (searchBar.editText?.text?.isEmpty() == true){
                    mUsers.clear()

                    for (ss in snapshot.children){
                        val user = ss.getValue(SearchUser::class.java)
                        user?.let { mUsers.add(it) }
                    }
                    userAdapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context
                        ,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })
    }
    
}