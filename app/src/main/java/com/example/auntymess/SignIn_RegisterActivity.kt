package com.example.auntymess

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.auntymess.databinding.ActivitySigninregisterBinding
import com.example.auntymess.models.Userdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignIn_RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninregisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private val PICK_IMAGE_REQEST=1
    private var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySigninregisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        val action=intent.getStringExtra("action")

        if(action=="login"){
            binding.loginEmail.visibility= View.VISIBLE
            binding.loginPassword.visibility= View.VISIBLE
            binding.tvNewhere.visibility= View.INVISIBLE
            binding.buttonRegister.visibility= View.INVISIBLE
            binding.registerEmail.visibility= View.GONE
            binding.registerName.visibility= View.GONE
            binding.registerPassword.visibility= View.GONE
            binding.cardView.visibility= View.GONE
            binding.putPhoto.visibility=View.GONE

            //Handling login

            binding.buttonLogin.setOnClickListener {
                val email=binding.loginEmail.text.toString()
                val password=binding.loginPassword.text.toString()

                if(email.isBlank() || password.isBlank()){
                    Toast.makeText(this,"Please fill all the Credentials", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                        if(it.isSuccessful){
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

        }

        else if(action=="register"){
            binding.buttonLogin.isEnabled=false
            binding.buttonLogin.alpha=0.5f

            binding.buttonRegister.setOnClickListener {
                val email=binding.registerEmail.text.toString()
                val password=binding.registerPassword.text.toString()
                val name=binding.registerName.text.toString()

                if(email.isBlank() || password.isBlank() || name.isBlank()){
                    Toast.makeText(this,"Please fill all the Credentials", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){

                            //Adding the user Database
                            addUserData(name,email,auth.currentUser!!.uid)

                            Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Error Connecting User",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.cardView.setOnClickListener{
            val intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(
                Intent.createChooser(intent,"select image"),
                PICK_IMAGE_REQEST
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQEST && resultCode== RESULT_OK && data!=null && data.data!=null)
            imageUri=data.data
        Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform())
            .into(binding.registerImage)
    }

    private fun addUserData(name: String, email: String, uid: String) {
        database= FirebaseDatabase.getInstance().getReference()
        val userdata=Userdata(
            name,
            email,
        )

        database.child("users").child(uid).setValue(userdata)
        //For storage
        storage= FirebaseStorage.getInstance().getReference()
//        storage.child("profile_img/$uid.jpg").putFile(imageUri!!)
        storage.child("profile_img/$uid.jpg").putFile(imageUri!!).addOnCompleteListener{task->
            storage.child("profile_img/$uid.jpg").downloadUrl.addOnCompleteListener {imageUri->
                val imageurl=imageUri.result.toString()

                database.child("users").child(uid).child("profileimage").setValue(imageurl)
                Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform())
                    .into(binding.registerImage)
            }
        }
    }
}