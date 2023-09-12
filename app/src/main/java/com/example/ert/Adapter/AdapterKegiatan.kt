package com.example.ert.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ert.R

class AdapterKegiatan(val dataKegiatan: List<HashMap<String,String>>):
    RecyclerView.Adapter<AdapterKegiatan.HolderDataKegiatan>(){
    class HolderDataKegiatan (v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.kegiatanNama)
        val pj = v.findViewById<TextView>(R.id.kegiatanPj)
        val jam = v.findViewById<TextView>(R.id.kegiatanJam)
        val tgl = v.findViewById<TextView>(R.id.kegiatanTanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKegiatan {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_kegiatan_main, parent, false)
        return HolderDataKegiatan(v)
    }

    override fun getItemCount(): Int {
        return dataKegiatan.size
    }

    override fun onBindViewHolder(holder: HolderDataKegiatan, position: Int) {
        val data = dataKegiatan.get(position)
        holder.nm.setText(data.get("nama_kegiatan"))
        holder.pj.setText("PJ : "+data.get("nama"))
        holder.jam.setText("Jam : "+data.get("jam_kegiatan"))
        holder.tgl.setText(data.get("tgl_kegiatan"))
    }
}