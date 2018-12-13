package com.ns.stellarjet.personalize

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.CabPersonalizeResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityCabPreferncesBinding
import com.ns.stellarjet.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CabPreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // obtain binding
        val binding : ActivityCabPreferncesBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_cab_prefernces
        )

        binding.buttonCabPreferencesBack.setOnClickListener {
            onBackPressed()
        }

        binding.buttonCabPreferencesUpdate.setOnClickListener {
            val personlaizeCab : Call<CabPersonalizeResponse> = RetrofitAPICaller.getInstance(this)
                .stellarJetAPIs.personalizeCab(
                SharedPreferencesHelper.getUserToken(this) ,
                SharedPreferencesHelper.getBookingId(this) ,
                1,
                3
            )

            personlaizeCab.enqueue(object : Callback<CabPersonalizeResponse> {
                override fun onResponse(
                    call: Call<CabPersonalizeResponse>,
                    response: Response<CabPersonalizeResponse>){
                    if (response.body() != null && response.body()!!.resultcode == 1) {
                        Log.d("Booking", "onResponse: " + response.body())
                        finish()
                    }
                }

                override fun onFailure(call: Call<CabPersonalizeResponse>, t: Throwable) {
                    Log.d("Booking", "onResponse: $t")
                }
            })
        }
    }
}
