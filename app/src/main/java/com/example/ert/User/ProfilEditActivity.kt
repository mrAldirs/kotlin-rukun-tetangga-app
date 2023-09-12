package com.example.ert.User

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityProfilEditBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfilEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityProfilEditBinding
    lateinit var urlClass: UrlClass

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""

    var jenkel = ""

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Edit Profil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        detail("show_profil")
        profilKeluarga()

        b.rgJenkel.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbLaki -> jenkel = "Laki-laki"
                R.id.rbPerempuan -> jenkel = "Perempuan"
            }
        }

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnKirim.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin mengedit profil Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    edit("edit")
                    saveAnggotaKeluarga()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                b.updateImage.visibility = View.VISIBLE
                imStr = mediaHealper.getBitmapToString(data!!.data,b.updateImage)
            }
        }
    }

    private fun detail(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama")
                val st2 = jsonObject.getString("nik")
                val st3 = jsonObject.getString("jenis_kelamin")
                val st4 = jsonObject.getString("usia")
                val st5 = jsonObject.getString("email")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("foto")
                val st8 = jsonObject.getString("pendidikan")
                val st9 = jsonObject.getString("pekerjaan")

                b.edtNama.setText(st1)
                b.edtNoHp.setText(st6)

                if (st2.equals("null") || st4.equals("null") || st5.equals("null") || st8.equals("null") || st9.equals("null")) {
                    b.edtNik.setText("")
                    b.edtUsia.setText("")
                    b.edtEmail.setText("")
                    b.edtPendidikan.setText("")
                    b.edtPekerjaan.setText("")
                } else {
                    b.edtNik.setText(st2)
                    b.edtUsia.setText(st4)
                    b.edtEmail.setText(st5)
                    b.edtPendidikan.setText(st8)
                    b.edtPekerjaan.setText(st9)
                }
                Picasso.get().load(st7).into(b.editImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_profil" -> {
                        hm.put("mode","show_profil")
                        hm.put("kd_user", preferences.getString(USER, DEF_USER).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun edit(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil mengedit profil!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
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
                    "edit"->{
                        hm.put("mode","edit")
                        hm.put("nm", preferences.getString(NAMA, DEF_NAMA).toString())
                        hm.put("nama", b.edtNama.text.toString())
                        hm.put("nik", b.edtNik.text.toString())
                        hm.put("usia", b.edtUsia.text.toString())
                        hm.put("jenis_kelamin", jenkel)
                        hm.put("email", b.edtEmail.text.toString())
                        hm.put("no_hp", b.edtNoHp.text.toString())
                        hm.put("pendidikan", b.edtPendidikan.text.toString())
                        hm.put("pekerjaan", b.edtPekerjaan.text.toString())
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

    fun profilKeluarga() {
        val request = object : StringRequest(
            Method.POST,urlClass.anggota,
            Response.Listener { response ->
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val jsonArray = JSONArray(response)
                        for (x in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(x)
                            val status = jsonObject.getString("status_anggota")
                            val namaAnggota = jsonObject.getString("nama_anggota")

                            when (status) {
                                "Istri" -> b.edtIstri.setText(namaAnggota)
                                "Anak 1" -> b.edtAnak1.setText(namaAnggota)
                                "Anak 2" -> b.edtAnak2.setText(namaAnggota)
                                "Anak 3" -> b.edtAnak3.setText(namaAnggota)
                                "Anak 4" -> b.edtAnak4.setText(namaAnggota)
                                "Anak 5" -> b.edtAnak5.setText(namaAnggota)
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Tidak Ada Data Anggota Keluarga", Toast.LENGTH_LONG).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "detail_anggota")
                hm.put("kd_user", preferences.getString(USER, DEF_USER).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun saveAnggotaKeluarga() {
        val request = object : StringRequest(
            Method.POST, urlClass.anggota,
            Response.Listener { response ->
                try {
                    val responObj = JSONObject(response)
                    val respon = responObj.getInt("respon")
                    if (respon == 1) {
                        // Data berhasil disimpan, lakukan tindakan sesuai kebutuhan
                        Toast.makeText(this, "Data anggota keluarga berhasil disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        // Gagal menyimpan data, lakukan tindakan sesuai kebutuhan
                        Toast.makeText(this, "Gagal menyimpan data anggota keluarga", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
//                    Toast.makeText(this, "Kesalahan pada server", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Kesalahan koneksi atau respons dari server, lakukan tindakan sesuai kebutuhan
                Toast.makeText(this, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["mode"] = "save_anggota"
                params["kd_user"] = preferences.getString(USER, DEF_USER).toString()

                // Menambahkan kondisi untuk penanganan istri yang belum ditambahkan
                if (b.edtIstri.text.toString().isNotBlank()) {
                    params["nama_istri"] = b.edtIstri.text.toString()
                } else {
                    params["nama_istri"] = "" // Mengirimkan string kosong untuk indikasi istri tidak ditambahkan
                }

                params["nama_anak1"] = b.edtAnak1.text.toString()
                params["nama_anak2"] = b.edtAnak2.text.toString()
                params["nama_anak3"] = b.edtAnak3.text.toString()
                params["nama_anak4"] = b.edtAnak4.text.toString()
                params["nama_anak5"] = b.edtAnak5.text.toString()
                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}