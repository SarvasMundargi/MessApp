package com.example.auntymess

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.auntymess.databinding.ActivityForAdminBinding

class ForAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
