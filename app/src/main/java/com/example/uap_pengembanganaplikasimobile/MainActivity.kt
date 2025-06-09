package com.example.uap_pengembanganaplikasimobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uap_pengembanganaplikasimobile.LoginActivity
import com.example.uap_pengembanganaplikasimobile.R
import com.example.uap_pengembanganaplikasimobile.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById(R.id.buttonLogin)
        registerLink = findViewById(R.id.registerLink)

        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}