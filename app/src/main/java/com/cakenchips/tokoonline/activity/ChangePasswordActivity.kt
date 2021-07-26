package com.cakenchips.tokoonline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.app.ApiConfig
import com.cakenchips.tokoonline.databinding.ActivityChangePasswordBinding
import com.cakenchips.tokoonline.helper.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setAction()
    }

    private fun setAction(){
        binding.btnSave.setOnClickListener{
            binding.apply {
                if(edtOldPassword.text.toString() != "" && edtNewPassword.text.toString() != "" && edtConfirmPassword.text.toString() != ""){

                    if(edtNewPassword.text.toString() == edtConfirmPassword.text.toString()){
                        val oldPassword = edtOldPassword.text.toString()
                        val newPassword = edtNewPassword.text.toString()

                        val id = SharedPref(this@ChangePasswordActivity).getUser()!!.id
                        progressBar.visibility = View.VISIBLE
                        ApiConfig.instanceRetrofit.changePassword(id, oldPassword, newPassword).enqueue(object : Callback<Any> {
                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                progressBar.visibility = View.GONE
                            }

                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                if(response.body().toString().contains("berhasil")){
                                    onBackPressed()
                                }else if(response.body().toString().contains("salah")){
                                    Toast.makeText(this@ChangePasswordActivity, "password anda salah atau belum mengisi password baru", Toast.LENGTH_SHORT).show()
                                }
                                progressBar.visibility = View.GONE
                            }
                        })
                    }else{
                        Toast.makeText(this@ChangePasswordActivity, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(this@ChangePasswordActivity, "Mohon lengkapi data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}