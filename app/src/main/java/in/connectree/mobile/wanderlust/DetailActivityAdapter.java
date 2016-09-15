package in.connectree.mobile.wanderlust;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vidit on 02/04/16.
 */
public class DetailActivityAdapter extends RecyclerView.Adapter<DetailActivityAdapter.ViewHolder> {

    private long current;
    public DetailActivityAdapter(long curr){
        current = curr;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView destination, eta, rating, timeSpent;
        public ViewHolder(final View v) {
            super(v);
            destination= (TextView)v.findViewById(R.id.destination_textView);
            eta = (TextView) v.findViewById(R.id.eta_textView);
            rating = (TextView) v.findViewById(R.id.rating_textView);
            timeSpent = (TextView) v.findViewById(R.id.timeSent_textView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (v.findViewById(R.id.description_linearLayout).getVisibility() == View.GONE)
                        v.findViewById(R.id.description_linearLayout).setVisibility(View.VISIBLE);
                    else
                        v.findViewById(R.id.description_linearLayout).setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public DetailActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject jsonObject = null;
        try {
            jsonObject = NewTripActivityFragment.tripList.getJSONObject(position);
            holder.destination.setText(jsonObject.getString("name"));
        }catch (JSONException e){
            holder.destination.setText("");
        }
        try {
            holder.rating.setText(jsonObject.getString("rating"));
        }catch (JSONException e){
            holder.rating.setText("");
        }
        try {
            holder.timeSpent.setText(String.valueOf((int) jsonObject.getDouble("timeToSpend") / 60));
        }catch (JSONException e){
            holder.timeSpent.setText("");
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTimeInMillis((jsonObject.getLong("time")*1000) + current);
            holder.eta.setText(timeFormat.format(calendar.getTime()));

        } catch (JSONException e) {
            holder.eta.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return NewTripActivityFragment.tripList.length();
    }
}
