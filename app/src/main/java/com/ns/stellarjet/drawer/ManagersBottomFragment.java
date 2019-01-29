package com.ns.stellarjet.drawer;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ns.networking.model.secondaryusers.DeactivateSecondaryUserResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagersBottomFragment extends BottomSheetDialogFragment {

    private String name;
    private int id;

    public ManagersBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.layout_managers_bottom_sheet, container, false);
        TextView mManagersTextView = mRootView.findViewById(R.id.textView_bottomsheet_managers_name);
        TextView mDeleteTextView = mRootView.findViewById(R.id.textView_botttomsheet_managers_delete);

        Bundle mBundle  = getArguments();
        if (mBundle != null) {
            name = mBundle.getString("SecondaryName");
            id = mBundle.getInt("SecondaryId");
        }

        mManagersTextView.setText(name);

        mDeleteTextView.setOnClickListener(v -> showDeleteConfirmationDialog());

        return mRootView;
    }

    private void deleteUser(){
        Progress progress = Progress.getInstance();
        progress.showProgress(getActivity());
        Call<DeactivateSecondaryUserResponse> deactivateSecondaryUserResponseCall = RetrofitAPICaller.getInstance(getActivity())
                .getStellarJetAPIs().deactivateSecondaryUser(
                        SharedPreferencesHelper.getUserToken(getActivity()),
                        id,
                        2
                );

        deactivateSecondaryUserResponseCall.enqueue(new Callback<DeactivateSecondaryUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeactivateSecondaryUserResponse> call,@NonNull Response<DeactivateSecondaryUserResponse> response) {
                progress.hideProgress();
                if(response.body() !=null){
                    Log.d("ManagersBottomFragment", "onResponse: " + response.body());
                    if(response.body().getResultcode() == 1){
                        Toast.makeText(getActivity(),
                                name + " Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeactivateSecondaryUserResponse> call,@NonNull Throwable t) {
                progress.hideProgress();
                Log.d("ManagersBottomFragment", "onFailure: " + t);
            }
        });
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setMessage("Are you sure want to delete " +name +" ?");
        alertDialogBuilder.setPositiveButton("Ok",
                (arg0, arg1) -> deleteUser());
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    getActivity().getResources().getColor(R.color.colorButtonNew)
            );
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    getActivity().getResources().getColor(R.color.colorButtonNew)
            );
        });
        alertDialog.show();
    }

}
