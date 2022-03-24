package com.example.signinsignup

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.RadioButton
import com.example.signinsignup.adapters.DataTextWatcher
import com.example.signinsignup.adapters.PhoneTextWatcher
import com.example.signinsignup.modul.User
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance()
    private val users = db.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val data: EditText = findViewById(R.id.data)
        val phone: EditText = findViewById(R.id.phone)
        phone.addTextChangedListener(PhoneTextWatcher())
        data.addTextChangedListener(DataTextWatcher(data))

        val rb: RadioButton = findViewById(R.id.r3)
        val b: Button = findViewById(R.id.register)

        rb.setOnClickListener { activeButton(b) }
        b.setOnClickListener { register(createUser()) }
    }

    private fun createUser(): User {
        val fio: EditText = findViewById(R.id.fio)
        var gender = ""
        val gen1: RadioButton = findViewById(R.id.radioButton)
        val gen2: RadioButton = findViewById(R.id.radioButton2)

        if (gen1.isChecked) {
            gender = "Мужской"
        }
        else if (gen2.isChecked) {
            gender = "Женский"
        }

        val data: EditText = findViewById(R.id.data)
        val phone: EditText = findViewById(R.id.phone)
        val mail: EditText = findViewById(R.id.mail)
        val company: EditText = findViewById(R.id.company)
        val number: EditText = findViewById(R.id.num)
        val login: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)

        return User(
            fio.text.toString(),
            gender,
            data.text.toString(),
            phone.text.toString(),
            mail.text.toString(),
            company.text.toString(),
            number.text.toString(),
            login.text.toString(),
            password.text.toString()
        )
    }

    private fun register(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val us = auth.currentUser
                    users.child(user.login).setValue(user)
                        .addOnSuccessListener {
                            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                            builder.setTitle("")
                            builder.setCancelable(false)
                            builder.setMessage("Регистрация успешна. ID - " + us?.uid.toString())
                            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                dialog.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                }
                else {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("")
                    builder.setCancelable(false)
                    builder.setMessage("Регистрация не выполнена, проверьте корректность данных")
                    builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                        dialog.dismiss()
                        val rb: RadioButton = findViewById(R.id.r3)
                        rb.isChecked = false
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_mode_close_button) {
            return true
        }
        else {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun signIn(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun activeButton(b: Button) {
        val fio: EditText = findViewById(R.id.fio)
        var gender = ""
        val gen1: RadioButton = findViewById(R.id.radioButton)
        val gen2: RadioButton = findViewById(R.id.radioButton2)

        if (gen1.isChecked) {
            gender = "Мужской"
        }
        else if (gen2.isChecked) {
            gender = "Женский"
        }

        val data: EditText = findViewById(R.id.data)
        val phone: EditText = findViewById(R.id.phone)
        val mail: EditText = findViewById(R.id.mail)
        val company: EditText = findViewById(R.id.company)
        val number: EditText = findViewById(R.id.num)
        val login: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)
        val confirmPassword: EditText = findViewById(R.id.confirmPassword)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setCancelable(false)

        if (fio.text.toString() == "" || gender == "" || data.text.toString() == "" ||
            phone.text.toString() == "" || mail.text.toString() == "" ||
            company.text.toString() == "" || number.text.toString() == "" ||
            login.text.toString() == "" || password.text.toString() == ""
        ) {
            builder.setMessage("Не все поля заполнены")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
                val rb: RadioButton = findViewById(R.id.r3)
                rb.isChecked = false
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        if (password.text.toString() != confirmPassword.text.toString()) {
            builder.setMessage("Пароли должны совпадать")
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
                val rb: RadioButton = findViewById(R.id.r3)
                rb.isChecked = false
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        b.isClickable = true
        b.alpha = 1.0f
    }
}