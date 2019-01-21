package com.ns.stellarjet.drawer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ns.stellarjet.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagersBottomFragment extends BottomSheetDialogFragment {

    private View mRootView;
    private TextView mManagersTextView;
    private TextView mDeactivateTextView;
    private TextView mDeleteTextView;
    private TextView mCancelTextView;

    public ManagersBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =  inflater.inflate(R.layout.layout_managers_bottom_sheet, container, false);
        mManagersTextView = mRootView.findViewById(R.id.textView_bottomsheet_managers_name);
        mDeactivateTextView = mRootView.findViewById(R.id.textView_botttomsheet_managers_deactivate);
        mDeleteTextView = mRootView.findViewById(R.id.textView_botttomsheet_managers_delete);
        mCancelTextView = mRootView.findViewById(R.id.textView_botttomsheet_managers_cancel);

        mCancelTextView.setOnClickListener(v -> {
            dismiss();
//            Objects.requireNonNull(getActivity()).onBackPressed();
        });

        return mRootView;
    }

}
