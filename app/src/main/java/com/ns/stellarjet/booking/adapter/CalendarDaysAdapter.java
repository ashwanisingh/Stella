package com.ns.stellarjet.booking.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.stellarjet.R;

import java.util.Calendar;
import java.util.List;

public class CalendarDaysAdapter extends RecyclerView.Adapter<CalendarDaysAdapter.PlaceViewHolder> {

    private List<Calendar> items;
    private List<Calendar> mScheduledList;
    private int daysToPostPone ;
    private onDateSelectClickListener mOnDateSelectClickListener;
    private int lastCheckedPos = 0;

    public CalendarDaysAdapter( onDateSelectClickListener mOnDateSelectClickListenerParams,
                                List<Calendar> itemsParams,
                                List<Calendar> mScheduledCalendarListParams,
                                int daysToPostponeparams) {
        mOnDateSelectClickListener = mOnDateSelectClickListenerParams;
        this.items = itemsParams;
        daysToPostPone = daysToPostponeparams;
        mScheduledList = mScheduledCalendarListParams;
        lastCheckedPos = 0;
    }

    /**
     * The interface that receives callbacks when a place is clicked.
     */
    public interface onDateSelectClickListener {
        void onDateSelected(Calendar calendar);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_day, viewGroup, false);
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
        return items.size()+daysToPostPone;
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView mPlaceTextView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            // binds the UI with adapter
            mPlaceTextView = itemView.findViewById(R.id.textView_row_days);
        }

        void bind(final int position){
            final int index = ((position)-(daysToPostPone));
            if(position <daysToPostPone){
                mPlaceTextView.setText("");
                itemView.setVisibility(View.INVISIBLE);
                Log.d("CalendarDaysAdapter", "bind: if  " + position);
            }else {
                mPlaceTextView.setText(String.valueOf(items.get(index).get(Calendar.DAY_OF_MONTH)));
                Calendar calendar = items.get(index);
                /*Calendar currentCalendar = Calendar.getInstance();
                int currentDate = currentCalendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int inflatedDate = calendar.get(Calendar.DAY_OF_MONTH);
                int inflatedMonth = calendar.get(Calendar.MONTH);
                if(currentDate == inflatedDate && currentMonth == inflatedMonth){
                    itemView.setBackgroundResource(R.drawable.drawable_selected_circle);
                    mPlaceTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorDateSelectionBg));
                }*/
                if(!mScheduledList.contains(calendar)){
                    mPlaceTextView.setAlpha(0.4f);
                    mPlaceTextView.setEnabled(false);
                    mPlaceTextView.setOnClickListener(null);
                }
            }
            /*if(index!=0 && index ==  selectedPosition){
                if(!mPlaceTextView.getText().toString().isEmpty()){
                    itemView.setBackgroundResource(R.drawable.drawable_selected_circle);
                    mPlaceTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorDateSelectionBg));
                }
            }
            mPlaceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDateSelectClickListener.onDateSelected(items.get(index));
                    selectedPosition = index;
                    notifyDataSetChanged();
                }
            });*/
            if(position == lastCheckedPos){
                Log.d("CalendarDaysAdapter", "bind: if((position == lastCheckedPos)) " + position);
                itemView.setBackgroundResource(R.drawable.drawable_selected_circle);
                mPlaceTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorDateSelectionBg));
            }else {
                Log.d("CalendarDaysAdapter", "bind: else of if(position == lastCheckedPos)  " + position);
                itemView.setBackgroundResource(android.R.color.transparent);
                mPlaceTextView.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
                itemView.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(v -> {
                mOnDateSelectClickListener.onDateSelected(items.get(index));
                int prevPos = lastCheckedPos;
                lastCheckedPos = position;
                notifyItemChanged(prevPos);
                notifyItemChanged(lastCheckedPos);
                notifyDataSetChanged();
            });
        }
    }
}
