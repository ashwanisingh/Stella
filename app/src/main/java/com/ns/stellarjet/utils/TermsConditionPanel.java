package com.ns.stellarjet.utils;

import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.ns.networking.model.TCModel;
import com.ns.networking.model.ValidateCustomerResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.DateSelectionActivity;
import com.ns.stellarjet.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionPanel {


    private View sliderView;
    private SlideUp slideUp;


    private ImageView mTCCrossImg;
    private Button mTcBotton;
    private TextView mTCWebView;
    private TextView mTCHeading;
    private TextView mIAgreetView;
    private boolean mIsUserAgree;

    public TermsConditionPanel(AppCompatActivity activity, TCSliderListener tcSliderListener, String conditionType) {
        initTcSlider(activity, tcSliderListener, conditionType);
    }

    private void initTcSlider(AppCompatActivity activity, TCSliderListener tcSliderListener, String conditionType) {
        sliderView = activity.findViewById(R.id.slideView);
        mTCCrossImg = activity.findViewById(R.id.tc_cross_button);
        mTcBotton = activity.findViewById(R.id.tc_button);
        mTCWebView = activity.findViewById(R.id.tc_textview);
        mIAgreetView = activity.findViewById(R.id.i_agree_textview);
        mTCHeading = activity.findViewById(R.id.tc_textviewHeading);


        // Setting Button Name
        if(conditionType.equals("SIGNUP")) {
            mTcBotton.setText("Proceed");
        }
        else if(conditionType.equals("BOOKING")) {
            mTcBotton.setText("Confirm Booking");
        }
        else if(conditionType.equals("BOARDING")) {
            mTcBotton.setText("Download");
        }


//        mTCWebView.loadUrl("http://dev.stellarjet.com/app/static/terms.html");

//        mTCWebView.setText(tc);
//        mTCWebView.setMovementMethod(new ScrollingMovementMethod());
//        mTCHeading.setText(tc_heading);

        makeRequest(activity, conditionType);
        mTCWebView.setMovementMethod(new ScrollingMovementMethod());
        mTCHeading.setText(tc_heading);



        // Bottom to Top Slider
        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percentage) {
                        if(tcSliderListener != null) {
                            tcSliderListener.onTCSliderSlide(percentage);
                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if(tcSliderListener != null) {
                            tcSliderListener.onTcSliderVisibilityChanged(visibility);
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
//                .withSlideFromOtherView(activity.findViewById(R.id.rootView))
                .build();


        // Cross Img Click Listener
        mTCCrossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.hide();
            }
        });

        // Button Click Listener
        mTcBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcSliderListener != null) {
                    if(!mIsUserAgree) {
                        UiUtils.Companion.showSimpleDialog(
                                v.getContext(), "Please accept T&C."
                        );
                        return;
                    }
                    tcSliderListener.onTCButtonClick(mIsUserAgree);
                }
            }
        });

        // I Agree TextView Click Listener
        mIAgreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsUserAgree = !mIsUserAgree;
                if(mIsUserAgree) {
                    mIAgreetView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tik_check, 0, 0, 0);
                } else {
                    mIAgreetView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tik_uncheck, 0, 0, 0);
                }

            }
        });


    }


    public void showTcSlider() {
        slideUp.show();
    }

    public void hideTcSlider() {
        slideUp.hide();
    }


    public interface TCSliderListener {
        void onTcSliderVisibilityChanged(int visibility);
        void onTCButtonClick(boolean isUserAgree);
        void onTCSliderSlide(float percentage);
    }

    public boolean isVisible() {
        return slideUp.isVisible();
    }


    private void makeRequest(AppCompatActivity activity, final String screenType) {
        Call<TCModel> mLoginResponseCall = RetrofitAPICaller.getInstance(activity).getStellarJetAPIs().getTC();
        mLoginResponseCall.enqueue(new Callback<TCModel>() {
            @Override
            public void onResponse(Call<TCModel> call, Response<TCModel> response) {
                TCModel model = response.body();
                if(screenType.equals("SIGNUP")) {
                    mTCWebView.setText(model.getData().get(0).getScreenContent());
                }
                else if(screenType.equals("BOOKING")) {
                    mTCWebView.setText(model.getData().get(1).getScreenContent());
                }
                else if(screenType.equals("BOARDING")) {
                    mTCWebView.setText(model.getData().get(2).getScreenContent());
                }
            }

            @Override
            public void onFailure(Call<TCModel> call, Throwable t) {

            }
        });

    }


    String tc_heading = "Please, agree Terms & Conditions to continue";

}
