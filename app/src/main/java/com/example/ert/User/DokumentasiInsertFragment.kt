package com.example.ert.User

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.FragmentDokumentasiTambahBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class DokumentasiInsertFragment : DialogFragment() {
    private lateinit var b: FragmentDokumentasiTambahBinding
    lateinit var v : View
    lateinit var parent: KegiatanDetailActivity

    lateinit var urlClass: UrlClass

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentDokumentasiTambahBinding.inflate(layoutInflater)
        v = b.root
        parent = activity as KegiatanDetailActivity

        mediaHealper = MediaHelper(v.context)
        urlClass = UrlClass()

        b.btnBatalkan.setOnClickListener { dismiss() }

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Tambah Dokumentasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambah dokumentasi kegiatan baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insert()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insImg)
            }
        }
    }

    private fun insert() {
        val request = object : StringRequest(
            Method.POST,urlClass.dokumentasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil menambahkan dokumentasi kegiatan baru.", Toast.LENGTH_LONG)
                        .show()
                    dismiss()
                    parent.showData(arguments?.getString("kode").toString())
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                hm.put("mode","insert")
                hm.put("kd_kegiatan", arguments?.getString("kode").toString())
                hm.put("image",imStr)
                hm.put("file",nmFile)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}