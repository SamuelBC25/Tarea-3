package com.example.tarea3

import com.example.tarea3.api.* import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // Target IP for the Android Emulator [cite: 59]
    private val BASE_URL = "http://10.0.2.2:5000/"

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)

        // Ejercicio 1: Check API Status [cite: 61]
        lifecycleScope.launch {
            try {
                val response = apiService.checkStatus()
                tvStatus.text = "API Status: ${response.message}" // [cite: 64]
            } catch (e: Exception) {
                // Ejercicio 4: Handle Network Errors [cite: 81]
                tvStatus.text = "Error: Server is offline"
            }
        }

        // Ejercicio 2: Register [cite: 67]
        btnRegister.setOnClickListener {
            val user = User(etUser.text.toString(), etPass.text.toString())
            lifecycleScope.launch {
                try {
                    val res = apiService.register(user)
                    Toast.makeText(this@MainActivity, res.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Connection Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}