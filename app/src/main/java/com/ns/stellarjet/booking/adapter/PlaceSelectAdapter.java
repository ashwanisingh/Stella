package com.ns.stellarjet.booking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.City;
import com.ns.stellarjet.R;

import java.util.List;

public class PlaceSelectAdapter extends RecyclerView.Adapter<PlaceSelectAdapter.PlaceViewHolder> {

    private List<City> items;
    private onPlaceSelectClickListener mOnPlaceSelectClickListener;

    public PlaceSelectAdapter(onPlaceSelectClickListener onPlaceSelectClickListenerParams, List<City> itemsParams) {
        mOnPlaceSelectClickListener = onPlaceSelectClickListenerParams;
        this.items = itemsParams;
    }

    /**
     * The interface that receives callbacks when a place is clicked.
     */
    public interface onPlaceSelectClickListener {
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
            mPlaceTextView.setOnClickListener(v -> mOnPlaceSelectClickListener.onPlaceSelected(
                    items.get(position).getName() ,
                    items.get(position).getId()));
        }
    }
}
