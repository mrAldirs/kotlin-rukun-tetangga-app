package com.example.ert.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.ert.R

class AdapterLaporan(val dataLaporan: List<HashMap<String,String>>) :
    RecyclerView.Adapter<AdapterLaporan.HolderDataLaporan>(){
    class HolderDataLaporan (v: View) : RecyclerView.ViewHolder(v) {
        val kg = v.findViewById<EditText>(R.id.lapMainKegiatan)
        val ms = v.findViewById<EditText>(R.id.lapMainMasuk)
        val kl = v.findViewById<EditText>(R.id.lapMainKeluar)
        val ss = v.findViewById<EditText>(R.id.lapMainSisa)
        val tt = v.findViewById<EditText>(R.id.lapMainTotal)
        val kt = v.findViewById<EditText>(R.id.lapMainKet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataLaporan {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_laporan_main, parent, false)
        return HolderDataLaporan(v)
    }

    override fun getItemCount(): Int {
        return dataLaporan.size
    }

    override fun onBindViewHolder(holder: HolderDataLaporan, position: Int) {
        val data = dataLaporan.get(position)
        holder.kg.setText(data.get("nama_kegiatan"))
        holder.ms.setText("Rp. "+data.get("uang_masuk"))
        holder.kl.setText("Rp. "+data.get("uang_keluar"))
        holder.ss.setText("Rp. "+data.get("sisa"))
        holder.tt.setText("Rp. "+data.get("total"))
        holder.kt.setText(data.get("keterangan"))
    }
}