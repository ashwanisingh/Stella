package com.ns.stellarjet.drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonElement;
import com.ns.networking.model.Booking;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private ImageView flightExperience_btn;
    private ImageView foodExperience_btn;
    private ImageView limousineExperience_btn;

    private ImageView flightExperience_star1;
    private ImageView flightExperience_star2;
    private ImageView flightExperience_star3;
    private ImageView flightExperience_star4;
    private ImageView flightExperience_star5;

    private ImageView foodExperience_star1;
    private ImageView foodExperience_star2;
    private ImageView foodExperience_star3;
    private ImageView foodExperience_star4;
    private ImageView foodExperience_star5;

    private ImageView limousineExperience_star1;
    private ImageView limousineExperience_star2;
    private ImageView limousineExperience_star3;
    private ImageView limousineExperience_star4;
    private ImageView limousineExperience_star5;

    private EditText flightExperience_editText;
    private EditText foodExperience_editText;
    private EditText limousineExperience_editText;

    private LinearLayout flightExperience_InputLayout;
    private LinearLayout foodExperience_InputLayout;
    private LinearLayout limousineExperience_InputLayout;

    private Button submitBtn;

    private Booking mBooking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mBooking = getIntent().getParcelableExtra("BookingData");

        flightExperience_btn = findViewById(R.id.flightExperience_btn);
        foodExperience_btn = findViewById(R.id.foodExperience_btn);
        limousineExperience_btn = findViewById(R.id.limousineExperience_btn);

        flightExperience_star1 = findViewById(R.id.flightExperience_star1);
        flightExperience_star2 = findViewById(R.id.flightExperience_star2);
        flightExperience_star3 = findViewById(R.id.flightExperience_star3);
        flightExperience_star4 = findViewById(R.id.flightExperience_star4);
        flightExperience_star5 = findViewById(R.id.flightExperience_star5);

        foodExperience_star1 = findViewById(R.id.foodExperience_star1);
        foodExperience_star2 = findViewById(R.id.foodExperience_star2);
        foodExperience_star3 = findViewById(R.id.foodExperience_star3);
        foodExperience_star4 = findViewById(R.id.foodExperience_star4);
        foodExperience_star5 = findViewById(R.id.foodExperience_star5);

        limousineExperience_star1 = findViewById(R.id.limousineExperience_star1);
        limousineExperience_star2 = findViewById(R.id.limousineExperience_star2);
        limousineExperience_star3 = findViewById(R.id.limousineExperience_star3);
        limousineExperience_star4 = findViewById(R.id.limousineExperience_star4);
        limousineExperience_star5 = findViewById(R.id.limousineExperience_star5);

        flightExperience_editText = findViewById(R.id.flightExperience_editText);
        foodExperience_editText = findViewById(R.id.foodExperience_editText);
        limousineExperience_editText = findViewById(R.id.limousineExperience_editText);

        flightExperience_InputLayout = findViewById(R.id.flightExperience_InputLayout);
        foodExperience_InputLayout = findViewById(R.id.foodExperience_InputLayout);
        limousineExperience_InputLayout = findViewById(R.id.limousineExperience_InputLayout);


        flightExperience_InputLayout.setVisibility(View.GONE);
        foodExperience_InputLayout.setVisibility(View.GONE);
        limousineExperience_InputLayout.setVisibility(View.GONE);


        flightExperience();
        foodExperience();
        limousineExperience();

        findViewById(R.id.feedboack_backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Progress progress = Progress.getInstance();
                progress.showProgress(FeedbackActivity.this);

                String token = SharedPreferencesHelper.getUserToken(FeedbackActivity.this);
                String booking_id = ""+mBooking.getBooking_id();
                String passenger_id = ""+mBooking.getUser();
                String passenger_type = mBooking.getBooked_by_user_type();

                String flightExpId = "1";
                String flightExpRating = ""+flightExperience_rating;
                String flightExpDesc = flightExperience_editText.getText().toString();

                String foodExpId = "2";
                String foodExpRating = ""+foodExperience_rating;
                String foodExpDesc = foodExperience_editText.getText().toString();

                String limousineExpId = "3";
                String limousineExpRating = ""+limousineExperience_rating;
                String limousineExpDesc = limousineExperience_editText.getText().toString();

                Call<JsonElement> call = RetrofitAPICaller.getInstance(FeedbackActivity.this).getStellarJetAPIs()
                        .feedback(token, booking_id, passenger_id, passenger_type, flightExpId, flightExpRating,
                                flightExpDesc, foodExpId, foodExpRating, foodExpDesc,
                                limousineExpId, limousineExpRating, limousineExpDesc);

                /*call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.i("", "");
                        progress.hideProgress();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Log.i("", "");
                        progress.hideProgress();
                    }
                });*/





            }
        });



    }


    private int flightExperience_rating = 0;
    private int foodExperience_rating = 0;
    private int limousineExperience_rating = 0;

    private boolean flightExperience_selected = false;
    private boolean foodExperience_selected = false;
    private boolean limousineExperience_selected = false;


    private void flightExperience() {
        flightExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
        flightExperience_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flightExperience_InputLayout.getVisibility() == View.GONE) {
                    flightExperience_InputLayout.setVisibility(View.VISIBLE);
                    flightExperience_btn.setImageResource(R.drawable.ic_tik_check);
                    flightExperience_selected = true;
                } else {
                    flightExperience_InputLayout.setVisibility(View.GONE);
                    flightExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
                    flightExperience_selected = false;
                }
            }
        });

        // Star 1
        flightExperience_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightExperience_star1.setImageResource(R.drawable.star_enabled);
                flightExperience_star2.setImageResource(R.drawable.star_disabled);
                flightExperience_star3.setImageResource(R.drawable.star_disabled);
                flightExperience_star4.setImageResource(R.drawable.star_disabled);
                flightExperience_star5.setImageResource(R.drawable.star_disabled);
                flightExperience_rating = 1;
            }
        });

        // Star 2
        flightExperience_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightExperience_star1.setImageResource(R.drawable.star_enabled);
                flightExperience_star2.setImageResource(R.drawable.star_enabled);
                flightExperience_star3.setImageResource(R.drawable.star_disabled);
                flightExperience_star4.setImageResource(R.drawable.star_disabled);
                flightExperience_star5.setImageResource(R.drawable.star_disabled);
                flightExperience_rating = 2;
            }
        });

        // Star 3
        flightExperience_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightExperience_star1.setImageResource(R.drawable.star_enabled);
                flightExperience_star2.setImageResource(R.drawable.star_enabled);
                flightExperience_star3.setImageResource(R.drawable.star_enabled);
                flightExperience_star4.setImageResource(R.drawable.star_disabled);
                flightExperience_star5.setImageResource(R.drawable.star_disabled);
                flightExperience_rating = 3;
            }
        });

        // Star 4
        flightExperience_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightExperience_star1.setImageResource(R.drawable.star_enabled);
                flightExperience_star2.setImageResource(R.drawable.star_enabled);
                flightExperience_star3.setImageResource(R.drawable.star_enabled);
                flightExperience_star4.setImageResource(R.drawable.star_enabled);
                flightExperience_star5.setImageResource(R.drawable.star_disabled);
                flightExperience_rating = 4;
            }
        });

        // Star 5
        flightExperience_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightExperience_star1.setImageResource(R.drawable.star_enabled);
                flightExperience_star2.setImageResource(R.drawable.star_enabled);
                flightExperience_star3.setImageResource(R.drawable.star_enabled);
                flightExperience_star4.setImageResource(R.drawable.star_enabled);
                flightExperience_star5.setImageResource(R.drawable.star_enabled);
                flightExperience_rating = 5;
            }
        });
    }


    private void foodExperience() {
        foodExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
        foodExperience_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foodExperience_InputLayout.getVisibility() == View.GONE) {
                    foodExperience_InputLayout.setVisibility(View.VISIBLE);
                    foodExperience_btn.setImageResource(R.drawable.ic_tik_check);
                    foodExperience_selected = true;
                } else {
                    foodExperience_InputLayout.setVisibility(View.GONE);
                    foodExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
                    foodExperience_selected = false;
                }
            }
        });

        // Star 1
        foodExperience_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodExperience_star1.setImageResource(R.drawable.star_enabled);
                foodExperience_star2.setImageResource(R.drawable.star_disabled);
                foodExperience_star3.setImageResource(R.drawable.star_disabled);
                foodExperience_star4.setImageResource(R.drawable.star_disabled);
                foodExperience_star5.setImageResource(R.drawable.star_disabled);
                foodExperience_rating = 1;
            }
        });

        // Star 2
        foodExperience_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodExperience_star1.setImageResource(R.drawable.star_enabled);
                foodExperience_star2.setImageResource(R.drawable.star_enabled);
                foodExperience_star3.setImageResource(R.drawable.star_disabled);
                foodExperience_star4.setImageResource(R.drawable.star_disabled);
                foodExperience_star5.setImageResource(R.drawable.star_disabled);
                foodExperience_rating = 2;
            }
        });

        // Star 3
        foodExperience_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodExperience_star1.setImageResource(R.drawable.star_enabled);
                foodExperience_star2.setImageResource(R.drawable.star_enabled);
                foodExperience_star3.setImageResource(R.drawable.star_enabled);
                foodExperience_star4.setImageResource(R.drawable.star_disabled);
                foodExperience_star5.setImageResource(R.drawable.star_disabled);
                foodExperience_rating = 3;
            }
        });

        // Star 4
        foodExperience_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodExperience_star1.setImageResource(R.drawable.star_enabled);
                foodExperience_star2.setImageResource(R.drawable.star_enabled);
                foodExperience_star3.setImageResource(R.drawable.star_enabled);
                foodExperience_star4.setImageResource(R.drawable.star_enabled);
                foodExperience_star5.setImageResource(R.drawable.star_disabled);
                foodExperience_rating = 4;
            }
        });

        // Star 5
        foodExperience_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodExperience_star1.setImageResource(R.drawable.star_enabled);
                foodExperience_star2.setImageResource(R.drawable.star_enabled);
                foodExperience_star3.setImageResource(R.drawable.star_enabled);
                foodExperience_star4.setImageResource(R.drawable.star_enabled);
                foodExperience_star5.setImageResource(R.drawable.star_enabled);
                foodExperience_rating = 5;
            }
        });


    }




    private void limousineExperience() {
        limousineExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
        limousineExperience_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(limousineExperience_InputLayout.getVisibility() == View.GONE) {
                    limousineExperience_InputLayout.setVisibility(View.VISIBLE);
                    limousineExperience_btn.setImageResource(R.drawable.ic_tik_check);
                    limousineExperience_selected = true;
                } else {
                    limousineExperience_InputLayout.setVisibility(View.GONE);
                    limousineExperience_btn.setImageResource(R.drawable.ic_tik_uncheck);
                    limousineExperience_selected = false;
                }
            }
        });

        // Star 1
        limousineExperience_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limousineExperience_star1.setImageResource(R.drawable.star_enabled);
                limousineExperience_star2.setImageResource(R.drawable.star_disabled);
                limousineExperience_star3.setImageResource(R.drawable.star_disabled);
                limousineExperience_star4.setImageResource(R.drawable.star_disabled);
                limousineExperience_star5.setImageResource(R.drawable.star_disabled);
                limousineExperience_rating = 1;
            }
        });

        // Star 2
        limousineExperience_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limousineExperience_star1.setImageResource(R.drawable.star_enabled);
                limousineExperience_star2.setImageResource(R.drawable.star_enabled);
                limousineExperience_star3.setImageResource(R.drawable.star_disabled);
                limousineExperience_star4.setImageResource(R.drawable.star_disabled);
                limousineExperience_star5.setImageResource(R.drawable.star_disabled);
                limousineExperience_rating = 2;
            }
        });

        // Star 3
        limousineExperience_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limousineExperience_star1.setImageResource(R.drawable.star_enabled);
                limousineExperience_star2.setImageResource(R.drawable.star_enabled);
                limousineExperience_star3.setImageResource(R.drawable.star_enabled);
                limousineExperience_star4.setImageResource(R.drawable.ic_tik_uncheck);
                limousineExperience_star5.setImageResource(R.drawable.ic_tik_uncheck);
                limousineExperience_rating = 3;
            }
        });

        // Star 4
        limousineExperience_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limousineExperience_star1.setImageResource(R.drawable.star_enabled);
                limousineExperience_star2.setImageResource(R.drawable.star_enabled);
                limousineExperience_star3.setImageResource(R.drawable.star_enabled);
                limousineExperience_star4.setImageResource(R.drawable.star_enabled);
                limousineExperience_star5.setImageResource(R.drawable.star_disabled);
                limousineExperience_rating = 4;
            }
        });

        // Star 5
        limousineExperience_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limousineExperience_star1.setImageResource(R.drawable.star_enabled);
                limousineExperience_star2.setImageResource(R.drawable.star_enabled);
                limousineExperience_star3.setImageResource(R.drawable.star_enabled);
                limousineExperience_star4.setImageResource(R.drawable.star_enabled);
                limousineExperience_star5.setImageResource(R.drawable.star_enabled);
                limousineExperience_rating = 5;
            }
        });

    }


}
