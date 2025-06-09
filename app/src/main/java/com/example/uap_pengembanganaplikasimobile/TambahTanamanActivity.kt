package com.example.uap_pengembanganaplikasimobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class TambahTanamanActivity : AppCompatActivity() {

    private lateinit var namaPlant: EditText
    private lateinit var hargaPlant: EditText
    private lateinit var deskripsiPlant: EditText
    private lateinit var btnTambah: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_tanaman)

        namaPlant = findViewById(R.id.edit_nama_Plant)
        hargaPlant = findViewById(R.id.edit_harga_Plant)
        deskripsiPlant = findViewById(R.id.edit_deskripsi_Plant)
        btnTambah = findViewById(R.id.btn_update)

        btnTambah.setOnClickListener {
            val nama = namaPlant.text.toString().trim()
            val harga = hargaPlant.text.toString().trim()
            val deskripsi = deskripsiPlant.text.toString().trim()

            if (nama.isEmpty() || harga.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                kirimDataKeServer(nama, harga, deskripsi)
            }
        }
    }

    private fun kirimDataKeServer(nama: String, harga: String, deskripsi: String) {
        val url = "https://uappam.kuncipintu.my.id/plant/new"

        val jsonBody = JSONObject().apply {
            put("plant_name", nama)
            put("description", deskripsi)
            put("price", harga)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(this, "Berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                Toast.makeText(this, "Gagal menambahkan: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
