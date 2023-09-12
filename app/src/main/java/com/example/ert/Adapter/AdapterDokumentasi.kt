package com.example.ert.Adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ert.R
import com.example.ert.User.KegiatanDetailActivity
import com.squareup.picasso.Picasso

class AdapterDokumentasi(val dataAlbum: List<HashMap<String,String>>, val parent: KegiatanDetailActivity) :
    RecyclerView.Adapter<AdapterDokumentasi.HolderDataAlbum>(){
    class HolderDataAlbum(v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.adpNamaAlbum)
        val img = v.findViewById<ImageView>(R.id.adpAlbum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAlbum {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_dokumentasi, parent, false)
        return HolderDataAlbum(v)
    }

    override fun getItemCount(): Int {
        return dataAlbum.size
    }

    override fun onBindViewHolder(holder: HolderDataAlbum, position: Int) {
        val data = dataAlbum.get(position)
        holder.nm.setText(data.get("foto_dokumentasi"))
        Picasso.get().load(data.get("img_dokumentasi")).into(holder.img)

        holder.img.setOnClickListener {

        }

        holder.img.setOnLongClickListener { v: View ->
            val contextMenu = PopupMenu(v.context, v)
            contextMenu.menuInflater.inflate(R.menu.context_hapus, contextMenu.menu)
            contextMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.context_hapus -> {
                        AlertDialog.Builder(v.context)
                            .setTitle("Hapus Album!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Apakah Anda ingin menghapus Album ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                parent.delete((data.get("kd_dokumentasi").toString()))
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                            })
                            .show()
                        true
                    }
                }
                false
            }
            contextMenu.show()
            true
        }
    }
}