package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.SavedAddressResponse
import com.ns.networking.model.SavedAddresse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivitySavedAddressBinding
import com.ns.stellarjet.personalize.adapter.SavedAddressListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UIConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedAddressActivity : AppCompatActivity(), (SavedAddresse) -> Unit {

    private lateinit var cabType : String

    private lateinit var binding : ActivitySavedAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // obtain binding

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_saved_address
        )

        cabType = intent?.extras?.getString(UIConstants.BUNDLE_CAB_TYPE)!!

        if(StellarJetUtils.isConnectingToInternet(applicationContext)){
            getSavedAddress()
        }else{
            Toast.makeText(applicationContext, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
        }

        binding.textViewSavedAddressCurrentLocation.setOnClickListener {
            val mAddAddressIntent = Intent(
                this ,
                AddAddressScrollActivity::class.java
            )
            mAddAddressIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , cabType)
            startActivity(mAddAddressIntent)
            finish()
        }

        binding.buttonSavedAddressBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getSavedAddress(){
        val progress = Progress.getInstance()
        progress.showProgress(this)
        val boardingPassCall: Call<SavedAddressResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.getSavedAddress(
            SharedPreferencesHelper.getUserToken(this) ,
            SharedPreferencesHelper.getUserId(this)
        )

        boardingPassCall.enqueue(object : Callback<SavedAddressResponse>{
            override fun onResponse(
                call: Call<SavedAddressResponse>,
                response:
                Response<SavedAddressResponse> ) {
                progress.hideProgress()
                val addresses = response.body()?.data?.addresses
                setSavedAddress(addresses)
            }

            override fun onFailure(call: Call<SavedAddressResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                Toast.makeText(this@SavedAddressActivity , "Server Error" ,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setSavedAddress(addresses : List<SavedAddresse>?){
        val savedAddressAdapter  = SavedAddressListAdapter(addresses ,
            this@SavedAddressActivity)
        val layoutManager = LinearLayoutManager(
            this@SavedAddressActivity ,
            RecyclerView.VERTICAL ,
            false
        )
        binding.recyclerViewSavedAddress.layoutManager = layoutManager
        binding.recyclerViewSavedAddress.adapter = savedAddressAdapter
    }

    override fun invoke(savedAddresse: SavedAddresse) {
        if(cabType.equals(UIConstants.BUNDLE_CAB_TYPE_PICK , false)){
            SharedPreferencesHelper.saveCabPickupPersoalize(this , savedAddresse.address_tag)
            SharedPreferencesHelper.saveCabPickupPersoalizeID(this , savedAddresse.id.toString())
        }else if(cabType.equals(UIConstants.BUNDLE_CAB_TYPE_DROP , false)){
            SharedPreferencesHelper.saveCabDropPersonalize(this , savedAddresse.address_tag)
            SharedPreferencesHelper.saveCabDropPersonalizeID(this , savedAddresse.id.toString())
        }
        finish()
    }
}
