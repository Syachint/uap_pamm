package com.example.uap_pengembanganaplikasimobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class DetailPlantActivity: AppCompatActivity() {

    private lateinit var txtPlantName: TextView
    private lateinit var txtPlantPrice: TextView
    private lateinit var txtPlantDescription: TextView
    private lateinit var updateButton: Button

    private var hargaBersih: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_plant)
        initViews()
        setupListeners()
        loadPlantDetails()
    }

    private fun initViews() {
        txtPlantName = findViewById(R.id.txtPlantName)
        txtPlantPrice = findViewById(R.id.txtPlantPrice)
        txtPlantDescription = findViewById(R.id.txtPlantDescription)
        updateButton = findViewById(R.id.btnUpdate)
    }

    private fun setupListeners() {
        updateButton.setOnClickListener {
            val intent = Intent(this, UpdatePlant::class.java)
            intent.putExtra("nama", txtPlantName.text.toString())
            intent.putExtra("harga", hargaBersih)
            intent.putExtra("deskripsi", txtPlantDescription.text.toString())
            startActivity(intent)
            Toast.makeText(this, "Tombol update ditekan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPlantDetails() {
        val namaTanaman = intent.getStringExtra("namatanaman")
        if (!namaTanaman.isNullOrEmpty()) {
            fetchPlantDetails(namaTanaman)
        } else {
            Toast.makeText(this, "Nama tanaman tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPlantDetails(nama: String) {
        val encodedNama = nama.replace(" ", "%20")
        val url = "https://uappam.kuncipintu.my.id/plant/$encodedNama"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response -> handleResponse(response) },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun handleResponse(response: org.json.JSONObject) {
        try {
            val data = response.getJSONObject("data")
            val namaTanaman = data.getString("plant_name")
            val harga = data.getString("price")
            val deskripsi = data.getString("description")

            updateUI(namaTanaman, harga, deskripsi)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal parsing data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(nama: String, harga: String, deskripsi: String) {
        txtPlantName.text = nama
        hargaBersih = harga
        txtPlantPrice.text = "Rp $harga"
        txtPlantDescription.text = deskripsi
    }
}
