package com.dev.pari.googlesignin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface SignInResponseCallBack {
    fun onResponse(response : GoogleSignInAccount?)
    fun onError(error : String)
}