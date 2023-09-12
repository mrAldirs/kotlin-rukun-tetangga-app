package com.example.ert.Admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityKegiatanTambahBinding
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class KegiatanEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityKegiatanTambahBinding

    lateinit var urlClass: UrlClass

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKegiatanTambahBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Edit Kegiatan")

        urlClass = UrlClass()

        namaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,daftarNama)
        b.insNama.setAdapter(namaAdapter)
        b.insNama.threshold = 1

        detail("detail")

        b.insJam.setOnClickListener {
            showTimePickerDialog()
        }

        b.insTgl.setOnClickListener {
            showDatePickerDialog()
        }

        b.btnSend.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambah kegiatan di RT Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    edit("edit")
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }

        b.judul.setText("Edit \nKegiatan \n RT Anda!")
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                b.insJam.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                b.insTgl.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
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
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama"))
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

    private fun detail(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_kegiatan")
                val st2 = jsonObject.getString("jam_kegiatan")
                val st3 = jsonObject.getString("tgl_kegiatan")
                val st4 = jsonObject.getString("nama")
                val st5 = jsonObject.getString("kd_kegiatan")

                b.insNamaKegiatan.setText(st1)
                b.insJam.setText(st2)
                b.insTgl.setText(st3)
                b.insNama.setText(st4)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "detail" -> {
                        hm.put("mode","detail")
                        hm.put("kd_kegiatan", paket?.getString("kode").toString())
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
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil melakukan edit kegiatan RT!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "edit"->{
                        hm.put("mode","edit")
                        hm.put("kd_kegiatan", paket?.getString("kode").toString())
                        hm.put("nama", b.insNama.text.toString())
                        hm.put("nama_kegiatan", b.insNamaKegiatan.text.toString())
                        hm.put("jam_kegiatan", b.insJam.text.toString())
                        hm.put("tgl_kegiatan", b.insTgl.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}