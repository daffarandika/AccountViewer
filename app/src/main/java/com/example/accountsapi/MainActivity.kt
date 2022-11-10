package com.example.accountsapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountsapi.adapter.AccountAdapter
import com.example.accountsapi.databinding.ActivityMainBinding
import com.example.accountsapi.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding : ActivityMainBinding
    lateinit var jsonString : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        fetchAPI().start()
        updateUI()
    }

    fun fetchAPI() = runBlocking {
        launch(Dispatchers.IO){
            val conn = URL("https://reqres.in/api/users?page=1").openConnection() as HttpURLConnection
            val data = conn.inputStream.bufferedReader().readText()
            jsonString = data
        }
    }
    fun parseJSON(jsonString: String) : MutableList<AccountModel>{
        val accountList : MutableList<AccountModel> = mutableListOf()
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("data")
        for (i in 0 until jsonArray.length()){
            val jsonOutputObject = jsonArray.getJSONObject(i)
            accountList.add(AccountModel(
                jsonOutputObject.getString("id"),
                jsonOutputObject.getString("first_name"),
                jsonOutputObject.getString("last_name"),
                jsonOutputObject.getString("email"),
                jsonOutputObject.getString("avatar"),
            ))
            Log.d(TAG, "parseJSON: ${jsonOutputObject}")
        }
        return accountList
    }
    fun updateUI() {
        runOnUiThread{
            kotlin.run{
                val adapter = AccountAdapter(this, parseJSON(jsonString))
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = adapter
            }
        }
    }
}