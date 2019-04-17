package com.ns.stellarjet.drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ns.networking.model.Booking;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

                /*if(1==1) {
                    Toast.makeText(FeedbackActivity.this, "Under Progress", Toast.LENGTH_LONG).show();
                    return;
                }*/

                Progress progress = Progress.getInstance();
                progress.showProgress(FeedbackActivity.this);

                String token = SharedPreferencesHelper.getUserToken(FeedbackActivity.this);
                String booking_id = ""+mBooking.getBooking_id();
                String passenger_id = ""+mBooking.getUser();
                String passenger_type = "1";

                if(mBooking.getBooked_by_user_type().equalsIgnoreCase("primary")) {
                    passenger_type = "1";
                } else if(mBooking.getBooked_by_user_type().equalsIgnoreCase("secondary")) {
                    passenger_type = "2";
                }


                String flightExpId = "1";
                String flightExpRating = ""+flightExperience_rating;
                String flightExpDesc = flightExperience_editText.getText().toString();

                String foodExpId = "2";
                String foodExpRating = ""+foodExperience_rating;
                String foodExpDesc = foodExperience_editText.getText().toString();

                String limousineExpId = "3";
                String limousineExpRating = ""+limousineExperience_rating;
                String limousineExpDesc = limousineExperience_editText.getText().toString();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("token", token);
                jsonObject.addProperty("booking_id", booking_id);
                jsonObject.addProperty("passenger_id", passenger_id);
                jsonObject.addProperty("passenger_type", passenger_type);

                JsonArray jsonArray = new JsonArray();

                JsonObject flightExpObject = new JsonObject();
                flightExpObject.addProperty("ID", flightExpId);
                flightExpObject.addProperty("RATING", flightExpRating);
                flightExpObject.addProperty("DESCRIPTION", flightExpDesc);

                JsonObject foodExpObject = new JsonObject();
                foodExpObject.addProperty("ID", foodExpId);
                foodExpObject.addProperty("RATING", foodExpRating);
                foodExpObject.addProperty("DESCRIPTION", foodExpDesc);

                JsonObject limousineExpObject = new JsonObject();
                limousineExpObject.addProperty("ID", limousineExpId);
                limousineExpObject.addProperty("RATING", limousineExpRating);
                limousineExpObject.addProperty("DESCRIPTION", limousineExpDesc);

                jsonArray.add(flightExpObject);
                jsonArray.add(foodExpObject);
                jsonArray.add(limousineExpObject);

                jsonObject.add("feedback", jsonArray);

                Call<JsonElement> call = RetrofitAPICaller.getInstance(FeedbackActivity.this).getStellarJetAPIs()
                        .feedback("application/json", jsonObject);

                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.i("", "");
                        progress.hideProgress();
                        if(response.code() == 200) {
                            StellarJetUtils.showToast(FeedbackActivity.this, "Your feedback is saved successfully.");
                            finish();
                        } else {
                            InputStream inputStream = response.errorBody().byteStream();
                            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder total = new StringBuilder();
                            try {
                                for (String line; (line = r.readLine()) != null; ) {
                                    total.append(line).append('\n');
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            StellarJetUtils.showSimpleDialog(FeedbackActivity.this, total.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Log.i("", "");
                        progress.hideProgress();
                        StellarJetUtils.showSimpleDialog(FeedbackActivity.this, "Please try again.");
                    }
                });





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
