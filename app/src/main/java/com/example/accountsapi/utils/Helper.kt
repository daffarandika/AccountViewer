package com.example.accountsapi.utils

import com.example.accountsapi.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Helper {
    companion object API{
        val url = "https://reqres.in/api/users"
        val accounts : MutableList<AccountModel> = mutableListOf()
        fun GetAt(id : String) = runBlocking{
            launch (Dispatchers.IO) {
                val conn = URL(url+"/"+id).openConnection() as HttpURLConnection
                val jsonString = conn.inputStream.bufferedReader().readText()
                val jsonObject = JSONObject(jsonString)
                val jsonDataObject = jsonObject.getJSONObject("data")
                accounts.clear()
                accounts.add(AccountModel(
                    jsonDataObject.getString("id"),
                    "${jsonDataObject.getString("first_name")} ${jsonDataObject.getString("last_name")}",
                    jsonDataObject.getString("email"),
                    jsonDataObject.getString("avatar")
                ))
            }
        }
    }
}