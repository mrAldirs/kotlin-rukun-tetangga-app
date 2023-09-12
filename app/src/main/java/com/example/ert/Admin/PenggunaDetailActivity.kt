package com.example.ert.Admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityProfilBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PenggunaDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityProfilBinding
    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Profil Pengguna")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        b.btnEdit.visibility = View.GONE
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
        detail("show_profil")
        profilKeluarga()
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

                b.detailNama.setText(st1)
                b.detailNoHp.setText(st6)

                if (st2.equals("null") || st3.equals("null") || st4.equals("null") || st5.equals("null") || st8.equals("null") || st9.equals("null")) {
                    b.detailNik.setText("<kosong>")
                    b.detailJenkel.setText("<kosong>")
                    b.detailUsia.setText("<kosong>")
                    b.detailEmail.setText("<kosong>")
                    b.detailPendidikan.setText("<kosong>")
                    b.detailPekerjaan.setText("<kosong>")
                } else {
                    b.detailNik.setText(st2)
                    b.detailJenkel.setText(st3)
                    b.detailUsia.setText(st4)
                    b.detailEmail.setText(st5)
                    b.detailPendidikan.setText(st8)
                    b.detailPekerjaan.setText(st9)
                }
                Picasso.get().load(st7).into(b.detailImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "show_profil" -> {
                        hm.put("mode","show_profil")
                        hm.put("kd_user", paket?.getString("kode").toString())
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
            Method.POST, urlClass.anggota,
            Response.Listener { response ->
                if (response.equals(0)) {
                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val jsonArray = JSONArray(response)
                        if (jsonArray.length() == 0) {
                            // Tidak ada data yang ditemukan, lakukan tindakan sesuai kebutuhan
                            Toast.makeText(
                                this,
                                "Tidak ada data anggota keluarga",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            for (x in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(x)
                                val status = jsonObject.getString("status_anggota")
                                val namaAnggota = jsonObject.getString("nama_anggota")

                                when (status) {
                                    "Istri" -> {
                                        b.detailIstri.setText(namaAnggota)
                                    }

                                    "Anak 1" -> {
                                        b.detailAnak1.setText(namaAnggota)
                                    }

                                    "Anak 2" -> {
                                        b.detailAnak2.setText(namaAnggota)
                                    }

                                    "Anak 3" -> {
                                        b.detailAnak3.setText(namaAnggota)
                                    }

                                    "Anak 4" -> {
                                        b.detailAnak4.setText(namaAnggota)
                                    }

                                    "Anak 5" -> {
                                        b.detailAnak5.setText(namaAnggota)
                                    }
                                }
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
//                        Toast.makeText(this, "Tidak Ada Data Anggota Keluarga", Toast.LENGTH_LONG).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                hm.put("mode", "detail_anggota")
                hm.put("kd_user", paket?.getString("kode").toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}