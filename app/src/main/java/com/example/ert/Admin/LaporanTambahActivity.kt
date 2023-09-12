package com.example.ert.Admin

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityLaporanTambahBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LaporanTambahActivity : AppCompatActivity() {
    private lateinit var b: ActivityLaporanTambahBinding

    lateinit var urlClass: UrlClass

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLaporanTambahBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Laporan Keuangan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)
        namaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,daftarNama)
        b.spNamaKegiatan.adapter = namaAdapter

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnResult.setOnClickListener {
            val plus = b.insUangMasuk.text.toString().toInt()
            val min = b.insUangKeluar.text.toString().toInt()
            val result = plus - min
            b.insTotal.setText(result.toString())
        }

        b.btnSend.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambah laporan keuangan di RT Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insert("insert")
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insImage)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        getNama("get_nama")
    }

    private fun getNama(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama_kegiatan"))
                }
                namaAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode) {
                    "get_nama" -> {
                        hm.put("mode", "get_nama")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun insert(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menambahkan laporan keuangan!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
                } else {
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("nama_kegiatan", b.spNamaKegiatan.selectedItem.toString())
                        hm.put("uang_masuk", b.insUangMasuk.text.toString())
                        hm.put("uang_keluar", b.insUangKeluar.text.toString())
                        hm.put("sisa", b.insTotal.text.toString())
                        hm.put("keterangan", b.insKeterangan.text.toString())
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}