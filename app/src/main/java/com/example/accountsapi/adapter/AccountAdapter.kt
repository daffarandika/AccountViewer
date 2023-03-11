package com.example.accountsapi.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountsapi.DetailActivity
import com.example.accountsapi.R
import com.example.accountsapi.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL

class AccountAdapter(val context : Context, val accountList: MutableList<AccountModel>) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>(){
    class AccountViewHolder (view : View) : RecyclerView.ViewHolder(view){
        val textEmail : TextView = view.findViewById(R.id.text_email)
        val textName : TextView = view.findViewById(R.id.text_name)
        val imageView : ImageView = view.findViewById(R.id.iv_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false)
        return AccountViewHolder(layout)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accountList[position]
        updateUI(holder, account)
        holder.itemView.setOnClickListener {
            rowClicked(account.id)
        }
    }

    private fun rowClicked(id: String) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("id", id)
        (context as Activity).startActivity(intent)
    }

    fun updateUI(holder: AccountViewHolder, account: AccountModel) = runBlocking{
        launch (Dispatchers.IO){
            val url = account.avatar
            val inputStream = URL(url).openConnection().getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            (context as Activity).runOnUiThread {
                val resources = context.resources
                holder.textEmail.text = resources.getString(R.string.email_1_s, account.email)
                holder.textName.text = account.name
                holder.imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = accountList.size
}