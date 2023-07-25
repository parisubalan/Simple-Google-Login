package com.dev.pari.googlesignin

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

open class GoogleSignInActivity(context: Activity) : AppCompatActivity() {

    private val mContext = context
    private lateinit var googleSignInOpt: GoogleSignInOptions
    private lateinit var googleSignClient: GoogleSignInClient
    var responseCallBack = MediatorLiveData<GoogleSignInAccount>()

    // Initialize Google SignIn Opt and Client
    fun login() {
        googleSignInOpt =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build()
        googleSignClient = GoogleSignIn.getClient(mContext, googleSignInOpt)

        mContext.startActivityForResult(googleSignClient.signInIntent, 100)
    }

    // Check if already signed or not here then get last signed account details
    fun checkAlreadyLoginOrNot(): LiveData<GoogleSignInAccount?> {
        val data = MediatorLiveData<GoogleSignInAccount?>()
        val account = GoogleSignIn.getLastSignedInAccount(mContext)
        if (account != null)
            data.postValue(account)
        else
            data.postValue(null)
        return data
    }

    // Check if already signed or not
    fun ifAlreadyLogin(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(mContext)
        return account != null
    }

    // Current user sign out
    fun signOutCurrentLogin(): LiveData<Boolean> {
        val data = MediatorLiveData<Boolean>()
        googleSignInOpt =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build()
        googleSignClient = GoogleSignIn.getClient(mContext, googleSignInOpt)
        googleSignClient.signOut().addOnSuccessListener {
            data.postValue(true)
        }.addOnFailureListener {
            data.postValue(false)
        }
        return data
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val response = task.getResult(ApiException::class.java)
                responseCallBack.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getLoginResponse(): MutableLiveData<GoogleSignInAccount> {
        return responseCallBack
    }

}