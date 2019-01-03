package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Objects;

public class PassengerListActivity extends AppCompatActivity {

    private ActivityPassengerListBinding activityPassengerBinding;
    public static List<AddGuestRequestData> mGuestRequestDataList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPassengerBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_passenger_list);

        // get the number of guests
        int numOfGuests = Objects.requireNonNull(getIntent().getExtras()).getInt("numOfGuests");

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
    }

    private void makeGuestList(int numOfGuests) {
        for (int i = 0; i < numOfGuests; i++) {
            mGuestRequestDataList.add(new AddGuestRequestData());
        }
    }

    public static void addGuestFromAdapter(){

    }
}
