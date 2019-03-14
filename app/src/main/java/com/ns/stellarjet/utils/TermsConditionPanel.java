package com.ns.stellarjet.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.ns.stellarjet.R;

public class TermsConditionPanel {


    private View sliderView;
    private SlideUp slideUp;


    private ImageView mTCCrossImg;
    private Button mTcBotton;
    private TextView mTCTextView;
    private TextView mIAgreetView;
    private boolean mIsUserAgree;

    public TermsConditionPanel(AppCompatActivity activity, TCSliderListener tcSliderListener, String btnName) {
        initTcSlider(activity, tcSliderListener, btnName);
    }

    private void initTcSlider(AppCompatActivity activity, TCSliderListener tcSliderListener, String btnName) {
        sliderView = activity.findViewById(R.id.slideView);
        mTCCrossImg = activity.findViewById(R.id.tc_cross_button);
        mTcBotton = activity.findViewById(R.id.tc_button);
        mTCTextView = activity.findViewById(R.id.tc_textview);
        mIAgreetView = activity.findViewById(R.id.i_agree_textview);

        // Setting Button Name
        mTcBotton.setText(btnName);

        // Bottom to Top Slider
        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {

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
    }

}
