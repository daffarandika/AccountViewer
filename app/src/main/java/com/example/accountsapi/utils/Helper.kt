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
        }
    }
}