package com.example.uap_pengembanganaplikasimobile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder

interface OnDeleteClickListener {
    fun onDeleteClick(plantName: String)
}

class TanamanAdapter(
    private val listTanaman: MutableList<Plant>,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<TanamanAdapter.TanamanViewHolder>() {

    inner class TanamanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.nama)
        val harga: TextView = itemView.findViewById(R.id.harga)
        val detailButton: Button = itemView.findViewById(R.id.btn_detail)
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TanamanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemall, parent, false)
        return TanamanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TanamanViewHolder, position: Int) {
        val tanaman = listTanaman[position]
        holder.nama.text = tanaman.name
        holder.harga.text = "Rp ${tanaman.price}"

        holder.deleteButton.setOnClickListener {
            performDelete(tanaman.name, holder.adapterPosition, holder.itemView.context)
        }

        holder.detailButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPlantActivity::class.java)
            intent.putExtra("namatanaman", tanaman.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listTanaman.size

    private fun performDelete(plantName: String, position: Int, context: Context) {
        val encodedPlantName = URLEncoder.encode(plantName, "UTF-8")
        val url = "https://uappam.kuncipintu.my.id/plant/$encodedPlantName"

        val request = StringRequest(
            Request.Method.DELETE, url,
            {
                Toast.makeText(context, "Tanaman '$plantName' berhasil dihapus!", Toast.LENGTH_SHORT).show()
                listTanaman.removeAt(position)
                notifyItemRemoved(position)
                onDeleteClickListener.onDeleteClick(plantName)
            },
            { error ->
                Toast.makeText(context, "Gagal menghapus tanaman '$plantName'", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
                error.networkResponse?.data?.let {
                    Log.e("TanamanAdapter", "Error Delete Response: ${String(it)}")
                }
            }
        )

        Volley.newRequestQueue(context).add(request)
    }
}
