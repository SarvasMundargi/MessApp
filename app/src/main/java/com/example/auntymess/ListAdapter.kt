package com.example.auntymess

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.auntymess.fragments.home_frag
import com.example.auntymess.models.Userdata
import com.example.auntymess.models.userlist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ListAdapter(private val context: home_frag, private val stulist: ArrayList<userlist>): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView=itemView.findViewById(R.id.profile_pic)
        val prof_name: TextView=itemView.findViewById(R.id.profile_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stulist.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.prof_name.text=stulist[position].name
        Glide.with(holder.image.context).load(stulist[position].img).apply(RequestOptions.circleCropTransform())
            .into(holder.image)


        val auth=Firebase.auth
        val currentuser=auth.currentUser!!.email
        if(currentuser=="auntymess@gmail.com"){
            holder.itemView.setOnClickListener {
                val intent= Intent(context.requireContext(),ForAdminActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}