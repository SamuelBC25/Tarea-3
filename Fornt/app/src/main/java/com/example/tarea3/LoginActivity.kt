package com.example.tarea3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tarea3.api.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        // Ejercicio 1: Petición GET al iniciar la aplicación
        lifecycleScope.launch {
            try {
                val statusResponse = apiService.checkStatus()
                tvStatus.text = "API Status: ${statusResponse.message}"
            } catch (e: Exception) {
                // Si el error es de parsing (JSON mal formado), mostrarlo
                tvStatus.text = "API Status: Error (${e.localizedMessage})"
                Log.e("LoginActivity", "Error checking status", e)
            }
        }

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

                } catch (e: IOException) {
                    Log.e("LoginActivity", "Network error", e)
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión: El servidor no responde. Verifique que el backend esté activo.",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: HttpException) {
                    val errorMsg = if (e.code() == 401) "Usuario o contraseña incorrectos" else "Error del servidor: ${e.code()}"
                    Toast.makeText(
                        this@LoginActivity,
                        errorMsg,
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Unexpected error", e)
                    Toast.makeText(
                        this@LoginActivity,
                        "Error inesperado: ${e.message}",
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
