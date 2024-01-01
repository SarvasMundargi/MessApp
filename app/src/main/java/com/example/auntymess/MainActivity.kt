package com.example.auntymess

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.auntymess.databinding.ActivityMainBinding
import com.example.auntymess.fragments.attendance_frag
import com.example.auntymess.fragments.home_frag
import com.example.auntymess.fragments.profile_frag

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val students_list = home_frag()
        loadFragment(students_list)

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.attendance_info -> {
                    // Load AttendanceInfoFragment
                    val attendanceInfoFragment = attendance_frag()
                    loadFragment(attendanceInfoFragment)
                }
                R.id.student_list -> {
                    // Load Fragment2
                    //startActivity(Intent(this,MainActivity::class.java))
                    val studentinfo = home_frag()
                    loadFragment(studentinfo)
                }
                R.id.student_info -> {
                    // Load Fragment3
                    val fragment3 = profile_frag()
                    loadFragment(fragment3)
                }
                // Add more cases for other menu items
            }
            true
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // Replace the current fragment with the new one
        transaction.replace(R.id.container, fragment)

        // Add the transaction to the back stack
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }
}