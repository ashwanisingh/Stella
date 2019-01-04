package com.ns.stellarjet.booking;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.guestrequest.AddGuestRequestData;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PassengersListAdapter;
import com.ns.stellarjet.databinding.ActivityPassengerListBinding;
import com.ns.stellarjet.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class PassengerListActivity extends AppCompatActivity {

    private ActivityPassengerListBinding activityPassengerBinding;
    public static List<AddGuestRequestData> mGuestRequestDataList  = new ArrayList<>();
    private boolean isOnlySelfTravelling = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPassengerBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_passenger_list);

        activityPassengerBinding.buttonPassengerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get the number of guests
//        int numOfGuests = Objects.requireNonNull(getIntent().getExtras()).getInt("numOfGuests");
        int numOfGuests = 2;

        // change the self and guests name
        if(numOfGuests == 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guest));
        }else if(numOfGuests > 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self_guests));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guests));
        }

        makeGuestList(numOfGuests);

        /* set RecyclerView */
        PassengersListAdapter mPassengersAdapter = new PassengersListAdapter(
                HomeActivity.sUserData.getContacts() ,
                numOfGuests
        );

        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengersAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutFrozen(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                PassengerListActivity.this ,
                RecyclerView.VERTICAL ,
                false
        );
        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengersAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutManager(layoutManager);

        activityPassengerBinding.textViewPassengerSelf.setOnClickListener(v -> {
            isOnlySelfTravelling  =true;
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_button_background));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerListActivity.this , android.R.color.white)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(
                    ContextCompat.getColor(PassengerListActivity.this , R.color.colorLoginButton)
            );
            if(isOnlySelfTravelling && numOfGuests ==1){
                activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
                activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
            }
        });

        activityPassengerBinding.textViewPassengerGuests.setOnClickListener(v -> {
            if(isOnlySelfTravelling){
                isOnlySelfTravelling  =false;
                activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select));
                activityPassengerBinding.textViewPassengerSelf.setTextColor(
                        ContextCompat.getColor(PassengerListActivity.this , R.color.colorLoginButton)
                );
                activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_button_background));
                activityPassengerBinding.textViewPassengerGuests.setTextColor(ContextCompat.getColor(PassengerListActivity.this ,
                        android.R.color.white));
                if(numOfGuests == 1){
                    activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
                    activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
                }
            }
        });
    }

    private void makeGuestList(int numOfGuests) {
        for (int i = 0; i < numOfGuests; i++) {
            mGuestRequestDataList.add(new AddGuestRequestData());
        }
    }

    public static void addGuestFromAdapter(){

    }
}
