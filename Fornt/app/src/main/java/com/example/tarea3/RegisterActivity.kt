package com.example.tarea3

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tarea3.api.*
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val user = User(
                etUser.text.toString(),
                etPass.text.toString()
            )

            lifecycleScope.launch {
                try {

                    val response = apiService.register(user)

                    Toast.makeText(
                        this@RegisterActivity,
                        response.message,
                        Toast.LENGTH_LONG
                    ).show()

                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error during registration", e)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Connection Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }
    }
}