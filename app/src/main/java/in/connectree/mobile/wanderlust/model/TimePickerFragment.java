package in.connectree.mobile.wanderlust.model;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.connectree.mobile.wanderlust.R;

/**
 * Created by vidit on 01/04/16.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private Bundle bundle;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        bundle = getArguments();

        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

        if(bundle.getBoolean("bool"))
            ((TextView) getActivity().findViewById(R.id.startTime_textView)).
                    setText(timeFormat.format(calendar.getTime()));
        else
            ((TextView) getActivity().findViewById(R.id.endTime_textView)).
                    setText(timeFormat.format(calendar.getTime()));
    }
}
