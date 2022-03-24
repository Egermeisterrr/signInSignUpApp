package com.example.signinsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = ""
    }

    fun signIn(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun signUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}