package com.dev.pari.googlesignin

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dev.pari.googlesignin.databinding.ActivityProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var googleSignInOpt: GoogleSignInOptions
    private lateinit var googleSignClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialization()
    }

    private fun initialization() {
        // Initialize Google SignIn Opt and Client
        googleSignInOpt =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail()
                .build()
        googleSignClient = GoogleSignIn.getClient(this, googleSignInOpt)

        // Fetch signed account details here
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            binding.tvName.text = account.displayName.toString()
            binding.tvEmail.text = account.email
            Glide.with(this).load(account.photoUrl).into(binding.ivProfile)
        }

        binding.signOutBtn.apply {
            setOnClickListener {
                // Signout here
                googleSignClient.signOut()
                startActivity(Intent(this@ProfileActivity,LoginActivity::class.java))
                finish()
            }
        }
    }

}