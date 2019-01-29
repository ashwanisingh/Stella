package com.ns.stellarjet.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.PrimaryUser;
import com.ns.stellarjet.R;

import java.util.List;

public class PrimaryUsersAdapter extends RecyclerView.Adapter<PrimaryUsersAdapter.PlaceViewHolder> {

    private List<PrimaryUser> items;
    private onPrimaryUsersSelectClickListener mOnPrimaryUsersSelectClickListener;

    public PrimaryUsersAdapter(onPrimaryUsersSelectClickListener onPrimaryUsersSelectClickListenerParams, List<PrimaryUser> itemsParams) {
        mOnPrimaryUsersSelectClickListener = onPrimaryUsersSelectClickListenerParams;
        this.items = itemsParams;
    }

    /**
     * The interface that receives callbacks when a place is clicked.
     */
    public interface onPrimaryUsersSelectClickListener {
        void onPlaceSelected(String placeName, int placeId);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_city, viewGroup, false);
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {
        placeViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView mPlaceTextView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            // binds the UI with adapter
            mPlaceTextView = itemView.findViewById(R.id.textView_place_row);
        }

        public void bind(final int position){
            mPlaceTextView.setText(items.get(position).getName());
            mPlaceTextView.setOnClickListener(v -> mOnPrimaryUsersSelectClickListener.onPlaceSelected(
                    items.get(position).getName() ,
                    items.get(position).getId()));
        }
    }
}
