package com.example.signinsignup

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val b: Button = findViewById(R.id.authorization)
        b.setOnClickListener { auth() }
    }

    private fun auth() {
        val auth = FirebaseAuth.getInstance()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setCancelable(false)
        val login: EditText = findViewById(R.id.log)
        val password: EditText = findViewById(R.id.pas)

        if(login.text.toString() != "" && password.text.toString() != "") {
            val u = Firebase.database.reference.child("Users").child(login.text.toString()).get()
                .addOnSuccessListener {
                    val user: String = it.value.toString()
                    val mail = user.substringAfterLast('=').substringBefore('}')
                    auth.signInWithEmailAndPassword(mail, password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val uId = FirebaseAuth.getInstance().currentUser?.uid
                                builder.setMessage("Авторизация успешна. ID - $uId")
                                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                    dialog.dismiss()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                            } else {
                                builder.setMessage("Авторизация не выполнена, проверьте введённые данные")
                                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                    dialog.dismiss()
                                }
                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                            }
                        }
                }
        }
        else if(login.text.toString() == "" && password.text.toString() == ""){
            builder.setMessage("Введите логин и пароль")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        else if(login.text.toString() == ""){
            builder.setMessage("Введите логин")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        else if(password.text.toString() == ""){
            builder.setMessage("Введите пароль")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    fun forgotPassword(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setCancelable(false)
        val login: EditText = findViewById(R.id.log)
        if(login.text.toString() != "") {
            val u = Firebase.database.reference.child("Users").child(login.text.toString()).get()
                .addOnSuccessListener {
                    val user: String = it.value.toString()
                    val pas = user.substringAfter('=').substringAfter('=').substringBefore(',')
                    if(pas != "null"){
                        builder.setMessage("Ваш пароль $pas")
                    }
                    else {
                        builder.setMessage("Пользователь с таким логином не зарегистрирован")
                    }
                    builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
        }
        else {
            builder.setMessage("Введите логин")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_mode_close_button) {
            return true
        } else {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun signUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}