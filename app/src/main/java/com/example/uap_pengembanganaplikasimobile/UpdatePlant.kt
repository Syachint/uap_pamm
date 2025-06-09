package com.example.uap_pengembanganaplikasimobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.URLEncoder

class UpdatePlant : AppCompatActivity() {

    private lateinit var textNama: EditText
    private lateinit var textHarga: EditText
    private lateinit var txtDeskripsi: EditText
    private lateinit var btnSimpan: Button

    private var originalPlantName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_plant)

        initViews()
        populateFormFromIntent()
        setupListeners()
    }

    private fun initViews() {
        textNama = findViewById(R.id.nama_Plant)
        textHarga = findViewById(R.id.harga_Plant)
        txtDeskripsi = findViewById(R.id.deskripsi_Plant)
        btnSimpan = findViewById(R.id.btn_simpan)
    }

    private fun populateFormFromIntent() {
        originalPlantName = intent.getStringExtra("nama")

        textNama.setText(originalPlantName)
        intent.getStringExtra("harga")?.let {
            textHarga.setText("Rp $it")
        }
        txtDeskripsi.setText(intent.getStringExtra("deskripsi") ?: "")
    }

    private fun setupListeners() {
        btnSimpan.setOnClickListener {
            if (originalPlantName.isNullOrEmpty()) {
                showToast("Nama tanaman asli tidak ditemukan. Gagal update.")
                return@setOnClickListener
            }

            val jsonBody = buildJsonRequest() ?: return@setOnClickListener
            val encodedName = URLEncoder.encode(originalPlantName, "UTF-8")
            val url = "https://uappam.kuncipintu.my.id/plant/$encodedName"

            val request = JsonObjectRequest(
                Request.Method.PUT, url, jsonBody,
                { response ->
                    showToast("Tanaman berhasil diupdate!")
                    navigateToAllPlants()
                },
                { error ->
                    error.printStackTrace()
                    showToast("Gagal update tanaman")
                }
            )

            Volley.newRequestQueue(this).add(request)
        }
    }

    private fun buildJsonRequest(): JSONObject? {
        val nama = textNama.text.toString().trim()
        var harga = textHarga.text.toString().trim().replace("Rp", "").trim()
        val deskripsi = txtDeskripsi.text.toString().trim()

        if (nama.isEmpty() || harga.isEmpty() || deskripsi.isEmpty()) {
            showToast("Semua kolom harus diisi.")
            return null
        }

        return JSONObject().apply {
            put("plant_name", nama)
            put("price", harga)
            put("description", deskripsi)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAllPlants() {
        startActivity(Intent(this, AllPlantActivity::class.java))
        finish()
    }
}
