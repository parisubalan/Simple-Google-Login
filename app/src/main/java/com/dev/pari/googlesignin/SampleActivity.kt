package com.dev.pari.googlesignin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dev.pari.googlelogin.GoogleSignInActivity
import com.dev.pari.googlesignin.databinding.ActivityMainBinding

class SampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialization()
    }

    private fun initialization() {
        binding.signInButton.setOnClickListener {
            if (!GoogleSignInActivity(this).ifAlreadyLogin())
                GoogleSignInActivity(this).login()
        }

        binding.signOutBtn.setOnClickListener {
            if (GoogleSignInActivity(this).ifAlreadyLogin())
                GoogleSignInActivity(this).signOutCurrentLogin().observe(this) {
                    if (it) {
                        binding.profileLay.visibility = ViewGroup.GONE
                        binding.signInButton.visibility = ViewGroup.VISIBLE
                        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(this, "Logout Failure", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        if (GoogleSignInActivity(this).ifAlreadyLogin()) {
            GoogleSignInActivity(this).checkAlreadyLoginOrNot().observe(this) {
                binding.profileLay.visibility = ViewGroup.VISIBLE
                binding.signInButton.visibility = ViewGroup.GONE
                binding.tvName.text = it!!.displayName.toString()
                binding.tvEmail.text = it.email
                Glide.with(this).load(it.photoUrl).into(binding.ivProfile)
            }
        } else {
            binding.signInButton.visibility = ViewGroup.VISIBLE
            binding.profileLay.visibility = ViewGroup.GONE
        }
    }
}