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
import com.ns.stellarjet.R;

public class TermsConditionPanel {


    private View sliderView;
    private SlideUp slideUp;


    private ImageView mTCCrossImg;
    private Button mTcBotton;
    private TextView mTCWebView;
    private TextView mIAgreetView;
    private boolean mIsUserAgree;

    public TermsConditionPanel(AppCompatActivity activity, TCSliderListener tcSliderListener, String btnName) {
        initTcSlider(activity, tcSliderListener, btnName);
    }

    private void initTcSlider(AppCompatActivity activity, TCSliderListener tcSliderListener, String btnName) {
        sliderView = activity.findViewById(R.id.slideView);
        mTCCrossImg = activity.findViewById(R.id.tc_cross_button);
        mTcBotton = activity.findViewById(R.id.tc_button);
        mTCWebView = activity.findViewById(R.id.tc_textview);
        mIAgreetView = activity.findViewById(R.id.i_agree_textview);

        // Setting Button Name
        mTcBotton.setText(btnName);

        mTCWebView.setText(tc);
        mTCWebView.setMovementMethod(new ScrollingMovementMethod());

//        mTCWebView.loadUrl("https://news.google.com/?hl=en-IN&gl=IN&ceid=IN:en");

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

    String tc = "Chances are unless you are very lucky you will go thru many different relationships before you find your special someone. Finding your sole mate is like gambling. In poker and blackjack you may have to play dozens of hands until you get a winning hand, and it is the same with relationships.\n" +
            "\n" +
            "During your life you will probably meet some people who seem like they may be the one, or that they are close, but still have the feeling that something else is missing. My advice is that if you are not happy, because something seems like it is missing, then it usually is not right.\n" +
            "\n" +
            "Before I found my special someone, I was in a relationship for over 10 years, and I thought everything was great, until I started to seriously consider getting married. Then I noticed that we had so little in common and in reality wanted so many different things out of life and one day we both realized there was much about each of us that we both wanted from someone, but it was not us that we wanted.\n" +
            "\n" +
            "Then one day I decided I had enough of trying to go to clubs and bars to meet people. I was sick and tired of trying to find someone in the time it takes to finish a drink. It always seemed the ladies I would meet were all wrong for me, or they seemed great after talking to them for 5 minutes but they seemed to have no interest in me.\n" +
            "\n" +
            "Then one day I had an idea, it was not an original idea but I decided to use the internet to try to find the right person for me. So I proceeded to make a myspace profile. On this page I tried to put the real me and not the funny guy trying to be charming that was looking for love at the bar.\n" +
            "\n" +
            "And as I would at the poker rooms I went all in, I poured my heart and sole into this. I wrote what I wanted and wrote down who I truly think I am and not who I want to be, and I was rewarded by 1 email responding to my site.\n" +
            "\n" +
            "It was amazing; the response I got was like a dream. Imagine you are sitting in a Las Vegas casino playing poker. You are down to your last few dollars, you go all in and you wind up being dealt a Royal Flush, and suddenly the sky is bluer, the grass is greener, and all your worries seem to just fade away.\n" +
            "\n" +
            "At first things were a little awkward for the both of us. We decided to hold of on actually meeting until we got to know each other first. We spent a month just talking everyday on the internet. You can really open up to someone and show them the real you and not have to worry about rejection on the internet, after all you are just a faceless ghost, and if things don’t work out you could be sitting next to her on the bus one day without ever knowing it.\n" +
            "\n" +
            "The key to finding happiness is realizing you are going to bust more then you are going to get blackjack, but you must keep trying, trying to remember you only need to find the real thing once.";

}
