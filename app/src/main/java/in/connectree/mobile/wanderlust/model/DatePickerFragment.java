package in.connectree.mobile.wanderlust.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.connectree.mobile.wanderlust.R;

/**
 * Created by vidit on 01/04/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    Bundle bundle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        bundle = getArguments();

        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int day = bundle.getInt("day");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("E d MMM, yyyy", Locale.US);

        if(bundle.getBoolean("bool"))
            ((TextView) getActivity().findViewById(R.id.startDate_textView)).
                setText(dateFormat.format(calendar.getTime()));
        else
            ((TextView) getActivity().findViewById(R.id.endDate_textView)).
                    setText(dateFormat.format(calendar.getTime()));
    }
}
