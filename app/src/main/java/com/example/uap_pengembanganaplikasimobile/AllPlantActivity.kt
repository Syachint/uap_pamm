package com.example.uap_pengembanganaplikasimobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class AllPlantActivity : AppCompatActivity() {

    private lateinit var tambahTanamanButton: Button
    private lateinit var recyclerTanaman: RecyclerView
    private lateinit var tanamanAdapter: TanamanAdapter
    private val dataTanaman = mutableListOf<Plant>()
    private lateinit var buttonTambah: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_plant)

        tambahTanamanButton = findViewById(R.id.btn_tambah_list)
        tambahTanamanButton.setOnClickListener {
            val intent = Intent(this, TambahTanamanActivity::class.java)
            startActivity(intent)
        }
        recyclerTanaman = findViewById(R.id.recycler_view)



        setupRecyclerView()


        ambilDataTanaman()
    }

    override fun onResume() {
        super.onResume()
        ambilDataTanaman()
    }

    private fun setupRecyclerView() {
        tanamanAdapter = TanamanAdapter(dataTanaman, object : OnDeleteClickListener {
            override fun onDeleteClick(plantName: String) {
                Log.i("AllPlantActivity", "Tanaman dihapus: $plantName")
            }
        })

        recyclerTanaman.apply {
            layoutManager = LinearLayoutManager(this@AllPlantActivity)
            adapter = tanamanAdapter
        }
    }

    private fun ambilDataTanaman() {
        val url = "https://uappam.kuncipintu.my.id/plant/all"
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val dataArray = response.optJSONArray("data")
                if (dataArray != null) {
                    updateListFromJson(dataArray)
                }
            },
            { error ->
                Log.e("AllPlantActivity", "Error: ${error.message}")
            })

        queue.add(request)
    }

    private fun updateListFromJson(jsonArray: JSONArray) {
        dataTanaman.clear()

        for (i in 0 until jsonArray.length()) {
            jsonArray.optJSONObject(i)?.let { obj ->
                val id = obj.optInt("id", -1)
                val name = obj.optString("plant_name", "Tanpa Nama")
                val description = obj.optString("description", "Tidak ada deskripsi")
                val price = obj.optString("price", "0")

                dataTanaman.add(Plant(id, name, description, price))
            }
        }

        tanamanAdapter.notifyDataSetChanged()
    }
}
