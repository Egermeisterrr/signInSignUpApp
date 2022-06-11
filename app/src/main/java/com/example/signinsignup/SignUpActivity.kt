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
        } else if (gen2.isChecked) {
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
                            val text = "Регистрация успешна. ID - " + us?.uid.toString()
                            dialog(text)
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                } 
                else {
                    val text = "Регистрация не выполнена, проверьте корректность данных"
                    dialog(text)
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

        if(fio.text.toString() == "") {
            badResult("ФИО")
        }

        else if(gender == "") {
            badResult("Пол")
        }

        else if(data.text.toString() == "") {
            badResult("Дата рождения")
        }

        else if(phone.text.toString() == "") {
            badResult("Телефон")
        }

        else if(mail.text.toString() == "") {
            badResult("Электонная почта")
        }

        else if(company.text.toString() == "") {
            badResult("Название компании")
        }

        else if(number.text.toString() == "") {
            badResult("Номер")
        }

        else if(login.text.toString() == "") {
            badResult("Логин")
        }

        else if(password.text.toString() == "") {
            badResult("Пароль")
        }

        else if (password.text.toString().length < 6) {
            val text = "Пароль должкен включать не менее 6 символов"
            dialog(text)
        }

        else if(!mail.text.toString().contains("@")) {
            val text = "Проверьте корректность введённой электронной почты"
            dialog(text)
        }

        else if (password.text.toString() != confirmPassword.text.toString()) {
            val text = "Пароли должны совпадать"
            dialog(text)
        }

        b.isClickable = true
        b.alpha = 1.0f
    }

    private fun badResult(fieldName : String) {
        val text = "Поле $fieldName должно быть заполнено"
        dialog(text)
    }

    private fun dialog(text: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setCancelable(false)

        builder.setMessage(text)
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
            val rb: RadioButton = findViewById(R.id.r3)
            rb.isChecked = false
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}