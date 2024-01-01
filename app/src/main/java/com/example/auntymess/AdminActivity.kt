package com.example.auntymess

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.auntymess.databinding.ActivityAdminBinding
import com.example.auntymess.models.Userdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private val PICK_IMAGE_REQEST=1
    private var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        binding.buttonAdmin.setOnClickListener {
            val email= binding.adminEmail.editText?.text.toString()
            val password=binding.adminPassword.editText?.text.toString()
            val name="Rajeshwari"

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all the Credentials", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword("auntymess@gmail.com","abc123").addOnCompleteListener {
                    if(it.isSuccessful){
                        addUserData(name,email,auth.currentUser!!.uid)

                        Toast.makeText(this,"Logged In", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Not proper credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.adminCardview.setOnClickListener {
            val intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent,"select image"),
                PICK_IMAGE_REQEST
            )
        }
    }

    private fun addUserData(name: String, email: String, uid: String) {
        database=FirebaseDatabase.getInstance().getReference()

        val userdata=Userdata(
            name,
            email
        )

        database.child("users").child(uid).setValue(userdata)

        storage= FirebaseStorage.getInstance().getReference()
        storage.child("profile_img/$uid.jpg").putFile(imageUri!!).addOnCompleteListener{task->
            storage.child("profile_img/$uid.jpg").downloadUrl.addOnCompleteListener {imageUri->
                val imageurl=imageUri.result.toString()

                database.child("users").child(uid).child("profileimage").setValue(imageurl)
                Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform())
                    .into(binding.adminPhoto)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQEST && resultCode== RESULT_OK && data!=null && data.data!=null)
            imageUri=data.data
        Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform())
            .into(binding.adminPhoto)
    }
}