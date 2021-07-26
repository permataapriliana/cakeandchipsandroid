package com.cakenchips.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.app.ApiConfig
import com.cakenchips.tokoonline.databinding.ActivityEditProfileBinding
import com.cakenchips.tokoonline.fragment.AkunFragment
import com.cakenchips.tokoonline.helper.SharedPref
import com.cakenchips.tokoonline.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        s = SharedPref(this)
        setData()
        setAction()
    }

    private fun setAction(){
        binding.btnSave.setOnClickListener{
            binding.apply {
                if(edtName.text.toString() != "" && edtEmail.text.toString() != "" && edtPhone.text.toString() != ""){
                    val name = edtName.text.toString()
                    val email = edtEmail.text.toString()
                    val phone = edtPhone.text.toString()

                    val id = SharedPref(this@EditProfileActivity).getUser()!!.id
                    val user = User()
                    user.id = id
                    user.name = name
                    user.email = email
                    user.phone = phone
                    progressBar.visibility = View.VISIBLE
                    ApiConfig.instanceRetrofit.editProfile(id,name,email,phone).enqueue(object : Callback<Any> {
                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            Log.d("profile",t.message.toString())
                            progressBar.visibility = View.GONE
                        }

                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if(response.body().toString().contains("berhasil")){
                                s.setUser(user)
                                onBackPressed()
                            }else if(response.body().toString().contains("Nomor")){
                                Toast.makeText(this@EditProfileActivity, "Nomor telah digunakan", Toast.LENGTH_SHORT).show()
                            } else if (response.body().toString().contains("Email")){
                                Toast.makeText(this@EditProfileActivity, "Email telah digunakan", Toast.LENGTH_SHORT).show()
                            }
                            progressBar.visibility = View.GONE
                        }
                    })
                }else{
                    Toast.makeText(this@EditProfileActivity, "Mohon lengkapi data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(){
        val user = s.getUser()!!
        binding.apply {
            edtName.setText(user.name)
            edtEmail.setText(user.email)
            edtPhone.setText(user.phone)
        }
    }
}