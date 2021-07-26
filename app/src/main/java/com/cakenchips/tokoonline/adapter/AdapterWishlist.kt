package com.cakenchips.tokoonline.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.activity.DetailProdukActivity
import com.cakenchips.tokoonline.helper.Helper
import com.cakenchips.tokoonline.model.Wishlist
import com.cakenchips.tokoonline.util.Config
import com.squareup.picasso.Picasso

class AdapterWishlist(var activity: Activity, var data: ArrayList<Wishlist>) : RecyclerView.Adapter<AdapterWishlist.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvHargaAsli = view.findViewById<TextView>(R.id.tv_hargaAsli)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val hargaAsli = a.harga!!.toInt()
        var harga = a.harga!!.toInt()

        if (a.discount != 0) {
            harga -= a.discount
        }

        holder.tvHargaAsli.text = Helper().gantiRupiah(hargaAsli)
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().gantiRupiah(harga)
        val image = Config.productUrl + data[position].image
        Log.d("Images", image)
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProdukActivity::class.java)
            val str = Gson().toJson(data[position], Wishlist::class.java)
            activiti.putExtra("Tag", "wishlist")
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }
    }

}