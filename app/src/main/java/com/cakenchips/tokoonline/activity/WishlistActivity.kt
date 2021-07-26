package com.cakenchips.tokoonline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.adapter.AdapterWishlist
import com.cakenchips.tokoonline.model.Produk
import com.cakenchips.tokoonline.model.Wishlist
import com.cakenchips.tokoonline.room.MyDatabase
import kotlinx.android.synthetic.main.activity_wishlist.*

class WishlistActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    lateinit var produk: Produk
    lateinit var wishlist: ArrayList<Wishlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDb = MyDatabase.getInstance(this)!!
        setContentView(R.layout.activity_wishlist)
        getWishlist()
    }

    private fun getWishlist(){
        wishlist = myDb.daoWishlist().getAll() as ArrayList<Wishlist>
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        if(wishlist.isNotEmpty()){
            rv_favorite.apply {
                layoutManager = GridLayoutManager(this@WishlistActivity, 2)
                adapter = AdapterWishlist(this@WishlistActivity, wishlist)
            }
        }
    }

    override fun onResume() {
        getWishlist()
        super.onResume()
    }
}