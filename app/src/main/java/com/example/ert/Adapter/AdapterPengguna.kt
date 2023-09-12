package com.example.ert.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ert.Admin.PenggunaActivity
import com.example.ert.Admin.PenggunaDetailActivity
import com.example.ert.R
import com.squareup.picasso.Picasso

class AdapterPengguna(val dataPengguna: List<HashMap<String,String>>, val parent: PenggunaActivity) :
    RecyclerView.Adapter<AdapterPengguna.HolderDataPengguna>(){
    class HolderDataPengguna (v: View): RecyclerView.ViewHolder(v) {
        val img = v.findViewById<ImageView>(R.id.penggunaImage)
        val nm = v.findViewById<TextView>(R.id.penggunaNama)
        val hp = v.findViewById<TextView>(R.id.penggunaNoHp)
        val dt = v.findViewById<TextView>(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataPengguna {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_penguna, parent, false)
        return HolderDataPengguna(v)
    }

    override fun getItemCount(): Int {
        return dataPengguna.size
    }

    override fun onBindViewHolder(holder: HolderDataPengguna, position: Int) {
        val data = dataPengguna.get(position)
        holder.nm.setText(data.get("nama"))
        holder.hp.setText(data.get("no_hp"))
        Picasso.get().load(data.get("img")).into(holder.img)
        holder.dt.setOnClickListener {
            val intent = Intent(it.context, PenggunaDetailActivity::class.java)
            intent.putExtra("kode", data.get("kd_user").toString())
            it.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }
    }
}