package com.example.tarea3

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tarea3.api.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUser.text.toString().trim()
            val password = etPass.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(username, password)

            lifecycleScope.launch {
                try {
                    val response = apiService.register(user)
                    // Caso Exitoso: El servidor responde con 200/201 OK
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registro exitoso: ${response.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // Regresa al login tras el éxito

                } catch (e: HttpException) {
                    // Caso Usuario Duplicado: El servidor responde con error (ej. 409 Conflict o 400)
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("RegisterActivity", "HTTP Error: $errorBody")
                    
                    if (e.code() == 409 || e.code() == 400) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: El usuario ya existe",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error del servidor: ${e.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: IOException) {
                    // Manejo de errores de red (Ejercicio 4)
                    Log.e("RegisterActivity", "Network error", e)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error de conexión: Verifique su servidor",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Unexpected error", e)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error inesperado: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
