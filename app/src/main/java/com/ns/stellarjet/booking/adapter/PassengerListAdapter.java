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
import com.ns.stellarjet.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.PassengerInfoViewHolder> {

    private List<Contact> items;
    private int numOfGuests;
    private onConfirmButtonEnableStateListener mOnConfirmButtonEnableStateListener;
    private onConfirmButtonDisableStateListener mOnConfirmButtonDisableStateListener;
    private List<String> mGuestNamesList = new ArrayList<>();
    public static List<AddGuestRequestData> mGuestRequestDataList  = new ArrayList<>();
    private List<Integer> mCompeletionList = new ArrayList<>();
    private List<String> mSelectedNameList= new ArrayList<>();
    public  static List<String> mSelectedPhoneNumberList= new ArrayList<>();
    public static PassengerInfoViewHolder mPassengerInfoViewHolder;
    private boolean isSelfTravelling;

    public PassengerListAdapter(
            onConfirmButtonEnableStateListener onConfirmButtonEnableStateListenerParams,
            onConfirmButtonDisableStateListener onConfirmButtonDiaableStateListenerParams,
            List<Contact> itemsParams ,
            int numOfGuestsParam ,
            boolean isSelfTravellingParams) {
        mOnConfirmButtonEnableStateListener = onConfirmButtonEnableStateListenerParams;
        mOnConfirmButtonDisableStateListener = onConfirmButtonDiaableStateListenerParams;
        this.items = itemsParams;
        numOfGuests = numOfGuestsParam;
        makeGuestRequestDataList(numOfGuestsParam);
        makeGuestNamesList();
        isSelfTravelling = isSelfTravellingParams;
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

    public static void changeSelfInfo(Context mContext , boolean isSelfTravelling){
        if(isSelfTravelling){
            mPassengerInfoViewHolder.mPassengerTitleTextView.setText(mContext.getResources().getString(R.string.info_passenger_self));
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setText(HomeActivity.sUserData.getName());
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setText(HomeActivity.sUserData.getPhone());
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setEnabled(false);
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setAlpha(0.4f);
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setEnabled(false);
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setAlpha(0.4f);
            setSelfInfo();
        }else {
            mPassengerInfoViewHolder.mPassengerTitleTextView.setText(mContext.getResources().getString(R.string.info_passenger_text) + " 1");
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setText("");
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setText("");
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setEnabled(true);
            mPassengerInfoViewHolder.mPassengerNameAutoCompleteTextView.setAlpha(1.0f);
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setEnabled(true);
            mPassengerInfoViewHolder.mPassengerSelfMobileNumberEditText.setAlpha(1.0f);
            mGuestRequestDataList.get(0).setGuestName("");
            mGuestRequestDataList.get(0).setGuestMobileNUmber("");
            mGuestRequestDataList.get(0).setGuestId("");
            mGuestRequestDataList.get(0).setGuestStatus("");
            mSelectedPhoneNumberList.remove(HomeActivity.sUserData.getPhone());
        }
    }

    private static void setSelfInfo(){
        mGuestRequestDataList.get(0).setGuestName(HomeActivity.sUserData.getName());
        mGuestRequestDataList.get(0).setGuestMobileNUmber(HomeActivity.sUserData.getPhone());
        mGuestRequestDataList.get(0).setGuestId("");
        mGuestRequestDataList.get(0).setGuestStatus("");
        mSelectedPhoneNumberList.add(HomeActivity.sUserData.getPhone());
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
            if(isSelfTravelling && position ==0 ){
                mPassengerTitleTextView.setText("Self");
                mPassengerNameAutoCompleteTextView.setText(HomeActivity.sUserData.getName());
                mPassengerSelfMobileNumberEditText.setText(HomeActivity.sUserData.getPhone());
                mPassengerNameAutoCompleteTextView.setEnabled(false);
                mPassengerNameAutoCompleteTextView.setAlpha(0.4f);
                mPassengerSelfMobileNumberEditText.setEnabled(false);
                mPassengerSelfMobileNumberEditText.setAlpha(0.4f);
                setSelfInfo();
            }else if(!isSelfTravelling && position ==0){
                mPassengerTitleTextView.setText("Passenger 1");
                mPassengerNameAutoCompleteTextView.setText("");
                mPassengerSelfMobileNumberEditText.setText("");
                mPassengerNameAutoCompleteTextView.setEnabled(true);
                mPassengerNameAutoCompleteTextView.setAlpha(1.0f);
                mPassengerSelfMobileNumberEditText.setEnabled(true);
                mPassengerSelfMobileNumberEditText.setAlpha(1.0f);
                mGuestRequestDataList.get(0).setGuestName("");
                mGuestRequestDataList.get(0).setGuestMobileNUmber("");
                mGuestRequestDataList.get(0).setGuestId("");
                mGuestRequestDataList.get(0).setGuestStatus("");
                mSelectedPhoneNumberList.remove(HomeActivity.sUserData.getPhone());
            }else if(position!=0){
                mPassengerTitleTextView.setText(
                        itemView.getContext().getResources().getString(R.string.info_passenger_text) +" "+(position+1));
            }

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
                    String mobileNumber = makeFilledData(selectedName , position , itemView.getContext());
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
                    if(s.length() == 0){
                        try {
                            mPassengerSelfMobileNumberEditText.setEnabled(false);
                            mPassengerSelfMobileNumberEditText.setAlpha(0.5f);
                            mSelectedPhoneNumberList.remove(mPassengerSelfMobileNumberEditText.getText().toString());
                            mPassengerSelfMobileNumberEditText.setText("");
                        }catch (IndexOutOfBoundsException e){
                            Log.d("PassengerListAdapter", "afterTextChanged: " + e);
                        }
                    }else if(s.length()>=3){
                        mPassengerSelfMobileNumberEditText.setEnabled(true);
                        mPassengerSelfMobileNumberEditText.setAlpha(1.0f);
                    }else if(s.length()<=3){
                        mPassengerSelfMobileNumberEditText.setEnabled(false);
                        mPassengerSelfMobileNumberEditText.setAlpha(0.5f);
                    }
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
                    if(s.length() == 10){
                        if(mSelectedPhoneNumberList.contains(s.toString())){
                            mGuestRequestDataList.get(position).setGuestName("");
                            mGuestRequestDataList.get(position).setGuestMobileNUmber("");
                            mGuestRequestDataList.get(position).setGuestId("");
                            mGuestRequestDataList.get(position).setGuestStatus("");
                            /*if(mCompeletionList.contains(position)){
                                mCompeletionList.remove(position);
                            }*/
                            Toast.makeText(itemView.getContext(), "Passenger already selected", Toast.LENGTH_SHORT).show();
                            mOnConfirmButtonDisableStateListener.disableButton(false);
                            mPassengerNameAutoCompleteTextView.setText("");
                            mPassengerSelfMobileNumberEditText.setText("");
                        }else {
                            mSelectedPhoneNumberList.add(s.toString());
                            try{
                                mGuestRequestDataList.get(position).setGuestMobileNUmber(s.toString());
                                mGuestRequestDataList.get(position).setGuestName(mPassengerNameAutoCompleteTextView.getText().toString());
                                mGuestRequestDataList.get(position).setGuestStatus(null);
                            }catch (IndexOutOfBoundsException e){
                                Log.d("PassengerListAdapter", "afterTextChanged: " +e);
                            }

                            /*if(mCompeletionList.contains(position)){
                                mCompeletionList.remove(position);
                            }*/
//                            mCompeletionList.add(position);
                        }
                        if(mSelectedPhoneNumberList.size() == numOfGuests){
                            Log.d("", "afterTextChanged: " + mGuestRequestDataList);
                            validateGuests();
                        }
                    }else {
                        mOnConfirmButtonDisableStateListener.disableButton(false);
                    }
                }
            });
        }
    }

    private void makeGuestNamesList(){
        for (int i = 0; i < items.size(); i++) {
            mGuestNamesList.add(items.get(i).getName());
        }
    }

    private void makeGuestRequestDataList(int size){
        for (int i = 0; i < size; i++) {
            mGuestRequestDataList.add(new AddGuestRequestData());
        }
    }

    private String makeFilledData(String name , int passengerPosition , Context context){
        String mobileNumber = "";
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equalsIgnoreCase(name)){
                mGuestRequestDataList.get(passengerPosition).setGuestName(name);
                mGuestRequestDataList.get(passengerPosition).setGuestMobileNUmber(items.get(i).getPhone());
                mGuestRequestDataList.get(passengerPosition).setGuestId(String.valueOf(items.get(i).getId()));
                mobileNumber = items.get(i).getPhone();
            }
        }
        if(mSelectedPhoneNumberList.contains(mobileNumber)){
            mGuestRequestDataList.get(passengerPosition).setGuestName("");
            mGuestRequestDataList.get(passengerPosition).setGuestMobileNUmber("");
            mGuestRequestDataList.get(passengerPosition).setGuestId("");
            mGuestRequestDataList.get(passengerPosition).setGuestStatus("");
            Toast.makeText(context, "Passenger already selected", Toast.LENGTH_SHORT).show();
            mOnConfirmButtonDisableStateListener.disableButton(false);
            return "";
        }
//        validateGuests(passengerPosition , name , mobileNumber);
        return mobileNumber;
    }

    private void validateGuests(){
        Log.d("passengers", "validateGuests: " + items.size());
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            String name = mGuestRequestDataList.get(i).getGuestName();
            String mobileNumber = mGuestRequestDataList.get(i).getGuestMobileNUmber();
            if(!isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("add");
            }else if(isNamePresent(name) && isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("");
            }else if(!isNamePresent(name) && isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("edit");
            }
        }
        mOnConfirmButtonEnableStateListener.isButtonEnabled(true , mGuestRequestDataList );
    }

    private boolean isNamePresent(String name){
        boolean result = false;
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equalsIgnoreCase(name)){
                result = true;
            }
        }
        return result;
    }

    private boolean isMobileNumberPresent(String mobileNumber){
        boolean result = false;
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getPhone().equalsIgnoreCase(mobileNumber)){
                result = true;
            }
        }
        return result;
    }
}