package com.cakenchips.tokoonline.room

import androidx.room.*
import com.cakenchips.tokoonline.model.Wishlist

@Dao
interface DaoWishlist {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Wishlist)

    @Delete
    fun delete(data: Wishlist)

    @Query("SELECT * from wishlist ORDER BY id ASC")
    fun getAll(): List<Wishlist>

    @Query("SELECT * FROM wishlist WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Wishlist

    @Query("DELETE FROM wishlist WHERE id = :id")
    fun deleteById(id: Int): Int

    @Query("DELETE FROM wishlist")
    fun deleteAll(): Int
}