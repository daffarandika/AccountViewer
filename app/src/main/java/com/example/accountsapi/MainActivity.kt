package com.example.accountsapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountsapi.adapter.AccountAdapter
import com.example.accountsapi.databinding.ActivityMainBinding
import com.example.accountsapi.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pb.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val conn = URL("https://reqres.in/api/users?page=1").openConnection() as HttpURLConnection
            val inputString = conn.inputStream.bufferedReader().readText()
            val jsonObject = JSONObject(inputString)
            val jsonArray = jsonObject.getJSONArray("data")
            val accounts: MutableList<AccountModel> = mutableListOf()
            for (i in 0 until jsonArray.length()){
                val jsonOutputObject = jsonArray.getJSONObject(i)
                accounts.add(AccountModel(
                    id = jsonOutputObject.getString("id"),
                    name = jsonOutputObject.getString("first_name") + " " + jsonOutputObject.getString("last_name"),
                    email = jsonOutputObject.getString("email"),
                    avatar = jsonOutputObject.getString("avatar"),
                ))
            }
            withContext(Dispatchers.Main) {
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = AccountAdapter(this@MainActivity, accounts)
                }
                binding.pb.visibility = View.GONE
            }
        }
    }
}