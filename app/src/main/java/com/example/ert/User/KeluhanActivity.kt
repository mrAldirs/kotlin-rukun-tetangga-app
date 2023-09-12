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
import com.example.ert.databinding.ActivityKeluhanBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KeluhanActivity : AppCompatActivity(){
    private lateinit var b: ActivityKeluhanBinding
    lateinit var urlClass: UrlClass

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKeluhanBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Keluhan dan Masukan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        b.btnImage.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnKirim.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin memberikan keluhan dan masukan?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insert("insert")
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
                b.insImage.visibility = View.VISIBLE
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insImage)
            }
        }
    }

    fun insert(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.keluhan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menambahkan keluhan dan masukan!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
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
                        hm.put("kd_user",preferences.getString(USER, DEF_USER).toString())
                        hm.put("teks_keluhan", b.insKeluhan.text.toString())
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