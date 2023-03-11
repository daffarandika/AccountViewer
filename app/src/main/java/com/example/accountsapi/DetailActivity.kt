package com.example.accountsapi

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.accountsapi.databinding.AccountDetailBinding
import com.example.accountsapi.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        if (intent.hasExtra("id")){

            val id : String = intent.getStringExtra("id").toString()
            Helper.GetAt(id)
            val accounts = Helper.accounts
            val name: String = accounts.first().name
            binding.tvName.text = name
            binding.tvEmail.text = accounts.first().email
            binding.tvDesc.text = getString(R.string.lorem)
            getImage(accounts.first().avatar)

        } else {
            val toast = Toast.makeText(this, "gagal cok", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun getImage (imgURL : String) = runBlocking{

        launch (Dispatchers.IO){

            val inputStream = URL(imgURL).openConnection().getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            runOnUiThread{

                binding.ivAvatar.setImageBitmap(bitmap)

            }

        }
    }
}