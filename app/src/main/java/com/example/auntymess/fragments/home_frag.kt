package com.example.auntymess.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auntymess.ListAdapter
import com.example.auntymess.databinding.FragmentListstudentsBinding
import com.example.auntymess.models.Userdata
import com.example.auntymess.models.userlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList


class home_frag : Fragment() {
    private lateinit var binding: FragmentListstudentsBinding
    private lateinit var stulist: ArrayList<userlist>
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListstudentsBinding.inflate(inflater, container, false)

        auth=FirebaseAuth.getInstance()
        stulist= ArrayList()

        mDbRef=FirebaseDatabase.getInstance().getReference()
        mAdapter= ListAdapter(this,stulist)

        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.recyclerView.adapter=mAdapter

        mDbRef.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                stulist.clear()

                for(stusnapshot in snapshot.children){
                    val currentuser=stusnapshot.getValue(Userdata::class.java)

                    if (currentuser!!.name != "Rajeshwari") {
                        val name = currentuser!!.name
                        val img = currentuser!!.profileimage

                        val list = userlist(name, img)
                        stulist.add(list)
                    }
                }

                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return binding.root
    }
}