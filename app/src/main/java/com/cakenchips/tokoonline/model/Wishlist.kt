package com.cakenchips.tokoonline.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "wishlist")
class Wishlist : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0
    var id = 0
    var name: String? = null
    var harga: String? = null
    var deskripsi: String? = null
    var category_id = 0
    var image: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var discount = 0
    var jumlah = 1
    var selected = true
}