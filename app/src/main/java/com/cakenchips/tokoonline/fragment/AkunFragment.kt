package com.cakenchips.tokoonline.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cakenchips.tokoonline.MainActivity
import com.cakenchips.tokoonline.R
import com.cakenchips.tokoonline.activity.*
import com.cakenchips.tokoonline.helper.SharedPref
import com.cakenchips.tokoonline.model.Wishlist
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass.
 */
class AkunFragment : Fragment() {

    lateinit var s: SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView


    lateinit var btnRiwayat: RelativeLayout
    lateinit var btnWishList: RelativeLayout
    lateinit var btnSettingAlamat: RelativeLayout
    lateinit var btnCart: LinearLayout
    lateinit var btnUbahProfile: RelativeLayout
    lateinit var btnGantiPassword: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)

        s = SharedPref(requireActivity())

        mainButton()
        setData()
        return view
    }

    fun mainButton() {
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            s.clear()
            activity?.recreate();
        }

        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }

        btnWishList.setOnClickListener{
            startActivity(Intent(requireActivity(), WishlistActivity::class.java))
        }
        btnCart.setOnClickListener{
            (activity as MainActivity).callFargment(1, (activity as MainActivity).fragmentKeranjang)
        }
        btnSettingAlamat.setOnClickListener{
            startActivity(Intent(requireActivity(), ListAlamatActivity::class.java))
        }
        btnUbahProfile.setOnClickListener{
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }
        btnGantiPassword.setOnClickListener{
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }
    }

    fun setData() {

        if (s.getUser() == null) {
//            val intent = Intent(activity, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
            return
        }

        val user = s.getUser()!!

        tvNama.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)
        btnWishList = view.findViewById(R.id.btn_wishlist)
        btnCart = view.findViewById(R.id.btn_cart)
        btnSettingAlamat = view.findViewById(R.id.btn_settingAlamat)
        btnGantiPassword = view.findViewById(R.id.btn_ubahPassword)
        btnUbahProfile = view.findViewById(R.id.btn_editProfile)
    }

    override fun onResume() {
        super.onResume()
        setData()
    }


}
