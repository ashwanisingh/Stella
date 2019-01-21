package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.secondaryusers.SecondaryUserInfoList
import com.ns.networking.model.secondaryusers.SecondaryUsersListResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityPreferencesManagersAddBinding
import com.ns.stellarjet.drawer.adapter.SecondaryUserListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferencesManagersListActivity : AppCompatActivity(), (SecondaryUserInfoList) -> Unit,
        (SecondaryUserInfoList, Int) -> Unit {

    private lateinit var mSecondaryListBinding : ActivityPreferencesManagersAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_managers_add)

        mSecondaryListBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_preferences_managers_add
        )

        /*mSecondaryListBinding.buttonManagersAdd.setOnClickListener {
            var managersBottomFragment = ManagersBottomFragment()
            managersBottomFragment.show(supportFragmentManager , managersBottomFragment.tag)
        }*/
        mSecondaryListBinding.buttonManagerAddBack.setOnClickListener {
            onBackPressed()
        }

        mSecondaryListBinding.buttonManagersAdd.setOnClickListener {

            val mAddSecondaryUserIntent = Intent(
                this ,
                PreferenceManagerInfoActivity::class.java
            )
            mAddSecondaryUserIntent.putExtra("isAddSecondaryUser" , true)
            startActivity(mAddSecondaryUserIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        getSecondaryUsersList()
    }

    private fun getSecondaryUsersList(){
        val progress = Progress.getInstance()
        progress.showProgress(this)
        val secondaryUserCallList: Call<SecondaryUsersListResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.getSecondaryUsersList(
            SharedPreferencesHelper.getUserToken(this)
        )

        secondaryUserCallList.enqueue(object : Callback<SecondaryUsersListResponse> {
            override fun onResponse(
                call: Call<SecondaryUsersListResponse>,
                response:
                Response<SecondaryUsersListResponse>) {
                progress.hideProgress()
                if(response.body()!=null && response.body()!!.resultcode == 1){
                    val mSecondaryUserListAdapter = SecondaryUserListAdapter(
                        response.body()!!.data.secondary_users ,
                        this@PreferencesManagersListActivity ,
                        this@PreferencesManagersListActivity
                    )

                    val layoutManager : LinearLayoutManager = LinearLayoutManager(
                        this@PreferencesManagersListActivity ,
                        RecyclerView.VERTICAL ,
                        false
                    )

                    mSecondaryListBinding.recyclerViewManagers.adapter = mSecondaryUserListAdapter
                    mSecondaryListBinding.recyclerViewManagers.layoutManager = layoutManager
                }
            }

            override fun onFailure(call: Call<SecondaryUsersListResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                Toast.makeText(this@PreferencesManagersListActivity , "Server Error" , Toast.LENGTH_SHORT).show()
            }
        })
    }


    /***
     * when the itemView is clicked
     * */
    override fun invoke(p1: SecondaryUserInfoList) {
        val mAddSecondaryUserIntent = Intent(
            this ,
            PreferenceManagerInfoActivity::class.java
        )
        mAddSecondaryUserIntent.putExtra("isAddSecondaryUser" , false)
        startActivity(mAddSecondaryUserIntent)
    }


    /**
     * when each row overflow icon is clicked
     */
    override fun invoke(p1: SecondaryUserInfoList, p2: Int) {
        var managersBottomFragment = ManagersBottomFragment()
        managersBottomFragment.show(supportFragmentManager , managersBottomFragment.tag)
    }
}
