package com.ns.stellarjet.drawer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.secondaryusers.AddSecondaryUserResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityPreferenceManagerInfoBinding
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import kotlinx.android.synthetic.main.activity_preference_manager_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferenceManagerInfoActivity : AppCompatActivity() {

    private lateinit var mSecondaryUserBinding:ActivityPreferenceManagerInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_manager_info)

        mSecondaryUserBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_preference_manager_info
        )

        val isAddUser = intent?.extras?.getBoolean("isAddSecondaryUser")

        if(isAddUser!!){
            mSecondaryUserBinding.buttonManagerInfoEdit.visibility = View.GONE
        }else{
            mSecondaryUserBinding.buttonManagerInfoEdit.visibility = View.VISIBLE
            mSecondaryUserBinding.textViewManagerInfoTitle.text = resources.getString(R.string.preferences_manager_info_title)
            mSecondaryUserBinding.textViewManagerInfoSubHeading.visibility = View.GONE
            mSecondaryUserBinding.buttonManagersInfoConfirm.visibility = View.GONE
        }

        mSecondaryUserBinding.buttonManagerInfoBack.setOnClickListener {
            onBackPressed()
        }

        mSecondaryUserBinding.buttonManagersInfoConfirm.setOnClickListener {
            val name = mSecondaryUserBinding.editTextManagersInfoName.text.toString()
            val email = mSecondaryUserBinding.editTextManagersInfoEmail.text.toString()
            val phone = mSecondaryUserBinding.editTextManagersInfoMobile.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()){

                if(StellarJetUtils.isConnectingToInternet(applicationContext)){
                    addSecondaryUser(name , email , phone)
                }else{
                    Toast.makeText(applicationContext, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@PreferenceManagerInfoActivity ,
                    "All Fields are mandatory" , Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun addSecondaryUser(name: String , email :String , phone:String){
        val progress = Progress.getInstance()
        progress.showProgress(this)
        val addSecondaryUserCall: Call<AddSecondaryUserResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.addSecondaryUserToken(
            SharedPreferencesHelper.getUserToken(this) ,
            name ,
            email,
            phone
        )

        addSecondaryUserCall.enqueue(object : Callback<AddSecondaryUserResponse> {
            override fun onResponse(
                call: Call<AddSecondaryUserResponse>,
                response:
                Response<AddSecondaryUserResponse>) {
                progress.hideProgress()
                if(response.body()!=null && response.body()!!.resultcode == 1){
                    Toast.makeText(this@PreferenceManagerInfoActivity ,
                        response.body()!!.message
                        , Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@PreferenceManagerInfoActivity , "Something went wrong" , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddSecondaryUserResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                Toast.makeText(this@PreferenceManagerInfoActivity , "Server Error" , Toast.LENGTH_SHORT).show()
            }
        })
    }
}
