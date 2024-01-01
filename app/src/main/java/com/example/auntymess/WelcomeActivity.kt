package com.example.auntymess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.auntymess.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        binding.buttonLogin.setOnClickListener {
            val intent=Intent(this,SignIn_RegisterActivity::class.java)
            intent.putExtra("action","login")
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent=Intent(this,SignIn_RegisterActivity::class.java)
            intent.putExtra("action","register")
            startActivity(intent)
        }

        binding.admin.setOnClickListener{
            startActivity(Intent(this,AdminActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}