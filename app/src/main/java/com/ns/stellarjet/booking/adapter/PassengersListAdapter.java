package com.ns.stellarjet.booking.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ns.networking.model.Contact;
import com.ns.networking.model.guestrequest.AddGuestRequestData;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.PassengerListActivity;

import java.util.ArrayList;
import java.util.List;

public class PassengersListAdapter extends RecyclerView.Adapter<PassengersListAdapter.PassengerInfoViewHolder> {

    private List<Contact> items;
    private int numOfGuests;
    private onConfirmButtonEnableStateListener mOnConfirmButtonEnableStateListener;
    private onConfirmButtonDisableStateListener mOnConfirmButtonDisableStateListener;
    private List<String> mGuestNamesList = new ArrayList<>();
    private List<String> mSelectedPhoneNumberList= new ArrayList<>();
    public static PassengerInfoViewHolder mPassengerInfoViewHolder;

    public PassengersListAdapter(
            List<Contact> itemsParams ,
            int numOfGuestsParam ) {
        this.items = itemsParams;
        numOfGuests = numOfGuestsParam;
        makeGuestNamesList();
    }

    /**
     * The interface that enables the confirm button.
     */
    public interface onConfirmButtonEnableStateListener {
        void isButtonEnabled(boolean isEnabled, List<AddGuestRequestData> mGuestRequestDataList);
    }

    /**
     * The interface that disables the confirm button.
     */
    public interface onConfirmButtonDisableStateListener {
        void disableButton(boolean isEnabled);
    }


    @NonNull
    @Override
    public PassengerInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_passengers_list, viewGroup, false);
        return new PassengerInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerInfoViewHolder placeViewHolder, int i) {
        placeViewHolder.bind(i);
        if(i == 0){
            mPassengerInfoViewHolder = placeViewHolder;
        }
    }

    @Override
    public int getItemCount() {
        return numOfGuests;
    }

    public class PassengerInfoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView_passenger_self)
        TextView mPassengerTitleTextView;
        @BindView(R.id.autoComplete_passenger_self_name)
        AutoCompleteTextView mPassengerNameAutoCompleteTextView;
        @BindView(R.id.textView_passenger_self_mobile_number)
        TextView mPassengerTitleMobileNumberTextView;
        @BindView(R.id.editText_passenger_self_mobile_number)
        EditText mPassengerSelfMobileNumberEditText;
        @BindView(R.id.view_passenger_divider)
        View mPassengerDividerView;

        PassengerInfoViewHolder(View itemView) {
            super(itemView);
            // binds the UI with adapter
            ButterKnife.bind(this , itemView);
        }

        public void bind(final int position){
            mPassengerSelfMobileNumberEditText.setEnabled(false);
            mPassengerSelfMobileNumberEditText.setAlpha(0.5f);
            ArrayAdapter<String> mPassengerAdapter = new ArrayAdapter<String>(
                    itemView.getContext() ,
                    android.R.layout.select_dialog_item ,
                    mGuestNamesList
            );
            mPassengerNameAutoCompleteTextView.setThreshold(1);
            mPassengerNameAutoCompleteTextView.setAdapter(mPassengerAdapter);
            mPassengerNameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int namePosition, long id) {
                    String selectedName = mPassengerNameAutoCompleteTextView.getAdapter().getItem(namePosition).toString();
//                    Toast.makeText(itemView.getContext(), selectedName, Toast.LENGTH_SHORT).show();
                    PassengerListActivity.mGuestRequestDataList.get(position).setGuestName(selectedName);
                    String mobileNumber = makeFilledData(selectedName , position , itemView.getContext());
                    PassengerListActivity.mGuestRequestDataList.get(position).setGuestMobileNUmber(mobileNumber);
                    if(mobileNumber.isEmpty()){
                        mPassengerNameAutoCompleteTextView.setText("");
                        mPassengerSelfMobileNumberEditText.setText("");
                    }else {
                        mPassengerSelfMobileNumberEditText.setText(mobileNumber);
                    }
                }
            });
            mPassengerNameAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mPassengerSelfMobileNumberEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void makeGuestNamesList(){
        for (int i = 0; i < items.size(); i++) {
            mGuestNamesList.add(items.get(i).getName());
        }
    }

    private String makeFilledData(String name , int passengerPosition , Context context){
        String mobileNumber = "";
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equalsIgnoreCase(name)){
                mobileNumber = items.get(i).getPhone();
            }
        }
        return mobileNumber;
    }
}