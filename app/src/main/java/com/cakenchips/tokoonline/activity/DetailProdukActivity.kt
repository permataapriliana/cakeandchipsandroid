package com.cakenchips.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.helper.Helper
import com.cakenchips.tokoonline.model.Produk
import com.cakenchips.tokoonline.model.Wishlist
import com.cakenchips.tokoonline.room.MyDatabase
import com.cakenchips.tokoonline.util.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.activity_masuk.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_custom.*

class DetailProdukActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var produk: Produk
    lateinit var wishlist: Wishlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDb = MyDatabase.getInstance(this)!! // call database
        setContentView(R.layout.activity_detail_produk)
        getInfo()
        checkIsWishlist()
        mainButton()
        checkKeranjang()
    }

    private fun setDataWishlist() {
        wishlist = Wishlist()
        wishlist.idTb = produk.idTb
        wishlist.id = produk.id
        wishlist.name = produk.name
        wishlist.harga = produk.harga
        wishlist.deskripsi = produk.deskripsi
        wishlist.category_id = produk.category_id
        wishlist.image = produk.image
        wishlist.created_at = produk.created_at
        wishlist.discount = produk.discount
        wishlist.jumlah = produk.jumlah
        wishlist.selected = produk.selected
    }

    private fun setDataProduct() {
        produk = Produk()
        produk.idTb = wishlist.idTb
        produk.id = wishlist.id
        produk.name = wishlist.name
        produk.harga = wishlist.harga
        produk.deskripsi = wishlist.deskripsi
        produk.category_id = wishlist.category_id
        produk.image = wishlist.image
        produk.discount = wishlist.discount
        produk.created_at = wishlist.created_at
        produk.jumlah = wishlist.jumlah
        produk.selected = wishlist.selected
    }

    private fun checkIsWishlist(){
        val isWishlist = myDb.daoWishlist().getProduk(wishlist.id)
        if(isWishlist == null){
            image_btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        } else {
            image_btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    private fun mainButton() {
        btn_keranjang.setOnClickListener {
            val data = myDb.daoKeranjang().getProduk(produk.id)
            if (data == null) {
                insert()
            } else {
                data.jumlah += 1
                update(data)
            }
        }

        btn_favorit.setOnClickListener {
            val isWishlist = myDb.daoWishlist().getProduk(wishlist.id)
            if(isWishlist == null){
                addWishlist()
            } else {
                deleteWishlist()
            }
//            val listData = myDb.daoKeranjang().getAll() // get All data
//            for (note: Produk in listData) {
//                println("-----------------------")
//                println(note.name)
//                println(note.harga)
//                Log.d("Favorite", "Test Wishlist")
//                Log.d("Favorite", note.name)
//                Log.d("Favorite", note.harga)
//            }
        }

        btn_toKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }

        btn_checkoutNow.setOnClickListener{
            checkoutNow()
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun insert() {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    Log.d("respons", "data inserted")
                    Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show()
                })
    }

    private fun checkoutNow(){
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        for (i in listProduk.indices) {
            val produk = listProduk[i]
            produk.selected = false
            update(produk)
        }
        val isDuplicate = myDb.daoKeranjang().getProduk(produk.id)
        if(isDuplicate == null){
            produk.selected = true
            CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Toast.makeText(this, "Berhasil checkout kekeranjang", Toast.LENGTH_SHORT).show()
                    })
        } else {
            val data = myDb.daoKeranjang().getProduk(produk.id)
            data.selected = true
            data.jumlah += 1
            update(data)
        }
    }

    private fun update(data: Produk) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("respons", "data inserted")
                    Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show()
                })
    }

    private fun addWishlist(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoWishlist().insert(wishlist) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("respons", "wishlist inserted")
                    image_btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    Toast.makeText(this, "Berhasil menambah wishlist", Toast.LENGTH_SHORT).show()
                })
    }

    private fun deleteWishlist(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoWishlist().deleteById(wishlist.id) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("respons", "wishlist deleted")
                    image_btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                })
    }

    private fun setUnselected(data: Produk) {
        val myDb = MyDatabase.getInstance(this)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("Cart", "Success Unselected")
                })
    }

    private fun checkKeranjang() {
        val dataKranjang = myDb.daoKeranjang().getAll()

        if (dataKranjang.isNotEmpty()) {
            div_angka.visibility = View.VISIBLE
            tv_angka.text = dataKranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }

    private fun getInfo() {
        if(intent.getStringExtra("Tag") == "produk"){
            val data = intent.getStringExtra("extra")
            produk = Gson().fromJson<Produk>(data, Produk::class.java)
            setDataWishlist()

            // set Value
            tv_nama.text = produk.name
            tv_harga.text = Helper().gantiRupiah(produk.harga)
            tv_deskripsi.text = produk.deskripsi

            val img = Config.productUrl + produk.image
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .resize(400, 400)
                    .into(image)

            // setToolbar
            Helper().setToolbar(this, toolbar, produk.name)

        }else if (intent.getStringExtra("Tag") == "wishlist"){
            val data = intent.getStringExtra("extra")
            wishlist = Gson().fromJson<Wishlist>(data, Wishlist::class.java)
            setDataProduct()

            // set Value
            tv_nama.text = produk.name
            tv_harga.text = Helper().gantiRupiah(produk.harga)
            tv_deskripsi.text = produk.deskripsi

            val img = Config.productUrl + produk.image
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .resize(400, 400)
                    .into(image)

            // setToolbar
            Helper().setToolbar(this, toolbar, produk.name)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
