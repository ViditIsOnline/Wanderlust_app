package in.connectree.mobile.wanderlust;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.connectree.mobile.wanderlust.model.DatePickerFragment;
import in.connectree.mobile.wanderlust.model.TimePickerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewTripActivityFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = NewTripActivityFragment.class.getSimpleName();

    public NewTripActivityFragment() {}

    private View rootView;
    private TextView startDate, startTime, endDate, endTime;
    private EditText tripName;
    private Calendar c;
    private SimpleDateFormat dateFormat, timeFormat, parsingFormat;

    private CheckBox[] checkBoxes;
    private ProgressDialog mProgressDialog;

    public static JSONArray tripList;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_trip, container, false);

        checkBoxes = new CheckBox[18];

        tripName = (EditText) rootView.findViewById(R.id.tripName_editText);
        startDate = (TextView) rootView.findViewById(R.id.startDate_textView);
        startTime = (TextView) rootView.findViewById(R.id.startTime_textView);

        endDate = (TextView) rootView.findViewById(R.id.endDate_textView);
        endTime = (TextView) rootView.findViewById(R.id.endTime_textView);

        c = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("E d MMM, yyyy", Locale.ENGLISH);
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        parsingFormat = new SimpleDateFormat("E d MMM, yyyy" + "hh:mm a", Locale.ENGLISH);

        startDate.setText(dateFormat.format(c.getTime()));
        startTime.setText(timeFormat.format(c.getTime()));

        c.add(Calendar.DAY_OF_MONTH, 1);

        endDate.setText(dateFormat.format(c.getTime()));
        endTime.setText(timeFormat.format(c.getTime()));

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        rootView.findViewById(R.id.letsGo_Button).setOnClickListener(this);

        checkBoxes[0] = (CheckBox)rootView.findViewById(R.id.tag_boat_water_sports);
        checkBoxes[1] = (CheckBox) rootView.findViewById(R.id.tag_casinos_gambling);
        checkBoxes[2] = (CheckBox) rootView.findViewById(R.id.tag_classes_workshops);
        checkBoxes[3] = (CheckBox) rootView.findViewById(R.id.tag_concerts_shows);
        checkBoxes[4] = (CheckBox) rootView.findViewById(R.id.tag_events);
        checkBoxes[5] = (CheckBox) rootView.findViewById(R.id.tag_food_drink);
        checkBoxes[6] = (CheckBox) rootView.findViewById(R.id.tag_fun_games);
        checkBoxes[7] = (CheckBox) rootView.findViewById(R.id.tag_museums);
        checkBoxes[8] = (CheckBox) rootView.findViewById(R.id.tag_nature_parks);
        checkBoxes[9] = (CheckBox) rootView.findViewById(R.id.tag_nightlife);
        checkBoxes[10] = (CheckBox) rootView.findViewById(R.id.tag_outdoor_activities);
        checkBoxes[11] = (CheckBox) rootView.findViewById(R.id.tag_shopping);
        checkBoxes[12] = (CheckBox) rootView.findViewById(R.id.tag_sights_landmarks);
        checkBoxes[13] = (CheckBox) rootView.findViewById(R.id.tag_spas_wellness);
        checkBoxes[14] = (CheckBox) rootView.findViewById(R.id.tag_tours);
        checkBoxes[15] = (CheckBox) rootView.findViewById(R.id.tag_traveller);
        checkBoxes[16] = (CheckBox) rootView.findViewById(R.id.tag_water_amusement);
        checkBoxes[17] = (CheckBox) rootView.findViewById(R.id.tag_zoos_aquariums);

        return rootView ;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.startDate_textView:
                showDatePickerDialog(true);
                break;
            case R.id.endDate_textView:
                showDatePickerDialog(false);
                break;
            case R.id.startTime_textView:
                showTimePickerDialog(true);
                break;
            case R.id.endTime_textView:
                showTimePickerDialog(false);
                break;
            case R.id.letsGo_Button:
                showProgressDialog();
                sendRequest();
        }

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void sendRequest() {


        Calendar tempCal1 = Calendar.getInstance();
        Calendar tempCal2 = Calendar.getInstance();
        try {
            tempCal1.setTime(parsingFormat.parse(startDate.getText().toString()
                    + startTime.getText().toString()));
            tempCal2.setTime(parsingFormat.parse(endDate.getText().toString()
                    + endTime.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final long d1 = tempCal1.getTimeInMillis();
        final long d2 = tempCal2.getTimeInMillis();
        final long diff = (d2-d1)/1000;
        Log.d(TAG, String.valueOf(diff));

        ArrayList<String> tempList = new ArrayList<>();
        for (CheckBox checkBox: checkBoxes)
            if (checkBox.isChecked())
                tempList.add(checkBox.getText().toString());

        final StringBuilder sb = new StringBuilder();
        for (String s : tempList)
        {
            sb.append(s);
            sb.append("\t");
        }

        sb.deleteCharAt(sb.length()-1);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getActivity());
        String url ="http://wunderlust-c7579.appspot.com";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            tripList = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("current", d1);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        hideProgressDialog();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", tripName.getText().toString());
                params.put("T", String.valueOf(diff));

                params.put("interest", sb.toString());
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                params.put("latitude", String.valueOf(Double.longBitsToDouble(sharedPreferences.getLong("lat", 0))));
                params.put("longitude", String.valueOf(Double.longBitsToDouble(sharedPreferences.getLong("lon", 0))));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);
    }

    private void showTimePickerDialog(boolean starting) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("hour",c.get(Calendar.HOUR_OF_DAY));
        bundle.putInt("minute", c.get(Calendar.MINUTE));
        bundle.putBoolean("bool",starting);

        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(boolean starting) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year",c.get(Calendar.YEAR));
        bundle.putInt("month", c.get(Calendar.MONTH));
        bundle.putInt("day", c.get(Calendar.DAY_OF_MONTH));
        bundle.putBoolean("bool",starting);

        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
