package com.cakenchips.tokoonline.app

import com.cakenchips.tokoonline.model.Chekout
import com.cakenchips.tokoonline.model.ResponModel
import com.cakenchips.tokoonline.model.rajaongkir.ResponOngkir
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") nomortlp: String,
            @Field("password") password: String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Call<ResponModel>

//    Route::post('user/{id}', 'Api\UserController@update');
//    Route::post('user/password/{id}', 'Api\UserController@change_password');
    @FormUrlEncoded
    @POST("user/{id}")
    fun editProfile(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<Any>

    @FormUrlEncoded
    @POST("user/password/{id}")
    fun changePassword(
            @Path("id") id: Int,
            @Field("password") password: String,
            @Field("newpassword") newPassword: String
    ): Call<Any>

    @POST("chekout")
    fun chekout(
            @Body data: Chekout
    ): Call<ResponModel>

    @GET("produk")
    fun getProduk(): Call<ResponModel>

    @GET("province")
    fun getProvinsi(
            @Header("key") key: String
    ): Call<ResponModel>

    @GET("city")
    fun getKota(
            @Header("key") key: String,
            @Query("province") id: String
    ): Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
            @Query("id_kota") id: Int
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
            @Header("key") key: String,
            @Field("origin") origin: String,
            @Field("destination") destination: String,
            @Field("weight") weight: Int,
            @Field("courier") courier: String
    ): Call<ResponOngkir>

    @GET("chekout/user/{id}")
    fun getRiwayat(
            @Path("id") id: Int
    ): Call<ResponModel>

    @POST("chekout/batal/{id}")
    fun batalChekout(
            @Path("id") id: Int
    ): Call<ResponModel>
}