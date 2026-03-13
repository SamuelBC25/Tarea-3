package com.example.tarea3

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tarea3.api.*
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        btnLogin.setOnClickListener {

            val user = User(
                etUser.text.toString(),
                etPass.text.toString()
            )

            lifecycleScope.launch {
                try {

                    val response = apiService.login(user)

                    Toast.makeText(
                        this@LoginActivity,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                    intent.putExtra("username", user.username)
                    startActivity(intent)

                } catch (e: Exception) {

                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}