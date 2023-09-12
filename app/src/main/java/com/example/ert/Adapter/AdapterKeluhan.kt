package com.example.ert.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ert.Admin.KeluhanActivity
import com.example.ert.Admin.KeluhanDetailActivity
import com.example.ert.R

class AdapterKeluhan(val dataKeluhan: List<HashMap<String,String>>, val parent: KeluhanActivity) :
    RecyclerView.Adapter<AdapterKeluhan.HolderDataKeluhan>(){
    class HolderDataKeluhan (v: View): RecyclerView.ViewHolder(v) {
        val img = v.findViewById<ImageView>(R.id.keluhanImage)
        val nm = v.findViewById<TextView>(R.id.keluhanNama)
        val tx = v.findViewById<TextView>(R.id.keluhanPesan)
        val dt = v.findViewById<TextView>(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluhan {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_keluhan, parent, false)
        return HolderDataKeluhan(v)
    }

    override fun getItemCount(): Int {
        return dataKeluhan.size
    }

    override fun onBindViewHolder(holder: HolderDataKeluhan, position: Int) {
        val data = dataKeluhan.get(position)
        holder.nm.setText(data.get("nama"))
        holder.tx.setText(data.get("teks_keluhan"))

        val image = data.get("img").toString()
        if (image.equals("null")){
            holder.img.visibility = View.GONE
        } else {
            holder.img.visibility = View.VISIBLE
        }

        holder.dt.setOnClickListener {
            val intent = Intent(it.context, KeluhanDetailActivity::class.java)
            intent.putExtra("kode", data.get("kd_keluhan").toString())
            it.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }
    }
}