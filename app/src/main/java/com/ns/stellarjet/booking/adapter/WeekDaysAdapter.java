package com.ns.stellarjet.booking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.stellarjet.R;

import java.util.List;

public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.PlaceViewHolder> {

    private List<String> items;

    public WeekDaysAdapter(List<String> itemsParams) {
        this.items = itemsParams;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_week_days, viewGroup, false);
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

    class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView mPlaceTextView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            // binds the UI with adapter
            mPlaceTextView = itemView.findViewById(R.id.textView_row_week_days);
        }

        void bind(final int position){
            mPlaceTextView.setText(items.get(position));
        }
    }
}
