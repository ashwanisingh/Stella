package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.BoardingPassResponse
import com.ns.networking.model.Booking
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBoardingPassBinding
import com.ns.stellarjet.drawer.adapter.BoardingListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UiUtils
import kotlinx.android.synthetic.main.activity_boarding_pass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardingPassActivity : AppCompatActivity(), (Booking) -> Unit {


    private lateinit var binding:ActivityBoardingPassBinding
    private var mBoardingPassList : List<Booking> = ArrayList()
    private var loading = true
    private var pastVisiblesItems: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var offset = 0
    private var limit = 10

    private var mLayoutManager: LinearLayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_boarding_pass
        )

        if(StellarJetUtils.isConnectingToInternet(applicationContext)){
            getBoardingPass()
        }else{
            UiUtils.showNoInternetDialog(this@BoardingPassActivity)
        }

        binding.recyclerViewBoardingPass.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                //check for scroll down
                {
                    visibleItemCount = mLayoutManager!!.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    pastVisiblesItems = mLayoutManager!!.findFirstVisibleItemPosition()

                    if (loading && mBoardingPassList.size==limit) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            //pagination.. i.e. fetch new data
                            offset += 10
                            limit += 10
                            getBoardingPass()
                        }
                    }
                }
            }
        })

        button_boarding_pass_back.setOnClickListener {
            onBackPressed()
        }
    }


    private fun getBoardingPass(){
        val progress = Progress.getInstance()
        progress.showProgress(this)
        val boardingPassCall: Call<BoardingPassResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.getBoardingPassResponse(
            SharedPreferencesHelper.getUserToken(this) ,
            offset,
            limit
        )

        boardingPassCall.enqueue(object : Callback<BoardingPassResponse> {
            override fun onResponse(
                call: Call<BoardingPassResponse>,
                response:
                Response<BoardingPassResponse>) {
                progress.hideProgress()
                mBoardingPassList = response.body()!!.data.boarding_pass
                val adapter = BoardingListAdapter(mBoardingPassList , this@BoardingPassActivity)
                mLayoutManager = LinearLayoutManager(
                    this@BoardingPassActivity ,
                    RecyclerView.VERTICAL ,
                    false
                )
                binding.recyclerViewBoardingPass.adapter = adapter
                binding.recyclerViewBoardingPass.layoutManager = mLayoutManager

            }

            override fun onFailure(call: Call<BoardingPassResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@BoardingPassActivity )
            }
        })
    }

    override fun invoke(boardingPass: Booking) {
        val mIntent = Intent(
            this ,
            BoardingPassDetailsActivity::class.java
        )
        mIntent.putExtra("BoardingPass" , boardingPass)
        startActivity(mIntent)
    }
}
