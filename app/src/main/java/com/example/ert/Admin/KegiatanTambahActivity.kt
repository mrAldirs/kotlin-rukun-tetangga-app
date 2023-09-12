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

class KegiatanTambahActivity : AppCompatActivity() {
    private lateinit var b: ActivityKegiatanTambahBinding

    lateinit var urlClass: UrlClass

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKegiatanTambahBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Tambah Kegiatan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        namaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,daftarNama)
        b.insNama.setAdapter(namaAdapter)
        b.insNama.threshold = 1

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
                    insert("insert")
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
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

    private fun insert(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menambahkan kegiatan RT!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
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