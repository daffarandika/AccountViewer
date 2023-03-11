package com.example.accountsapi

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.accountsapi.databinding.AccountDetailBinding
import com.example.accountsapi.model.AccountModel
import com.example.accountsapi.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity : AppCompatActivity(){
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    lateinit var binding: AccountDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = AccountDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pb.visibility = View.VISIBLE
        if (intent.hasExtra("id")) {
            val id : String = intent.getStringExtra("id").toString()
            lifecycleScope.launch(Dispatchers.IO) {
                val conn = URL(Helper.url +"/"+id).openConnection() as HttpURLConnection
                val jsonString = conn.inputStream.bufferedReader().readText()
                val jsonObject = JSONObject(jsonString)
                val jsonDataObject = jsonObject.getJSONObject("data")
                Helper.accounts.clear()
                Helper.accounts.add(
                    AccountModel(
                        jsonDataObject.getString("id"),
                        "${jsonDataObject.getString("first_name")} ${jsonDataObject.getString("last_name")}",
                        jsonDataObject.getString("email"),
                        jsonDataObject.getString("avatar")
                    )
                )
                withContext(Dispatchers.Main) {
                    val accounts = Helper.accounts
                    val name: String = accounts.first().name
                    binding.tvName.text = name
                    binding.tvEmail.text = accounts.first().email
                    binding.tvDesc.text = getString(R.string.lorem)
                    getImage(accounts.first().avatar)
                    binding.pb.visibility = View.GONE
                }
            }
        } else {
            val toast = Toast.makeText(this, "gagal cok", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun getImage (imgURL : String){
        binding.pb.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){
            val inputStream = URL(imgURL).openConnection().getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            runOnUiThread{
                binding.ivAvatar.setImageBitmap(bitmap)
            }
        }
        binding.pb.visibility = View.GONE
    }
}