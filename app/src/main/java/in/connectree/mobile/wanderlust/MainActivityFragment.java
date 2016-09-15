package in.connectree.mobile.wanderlust;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public MainActivityFragment() {
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rootView.findViewById(R.id.newTrip_textView).setOnClickListener(this);
        rootView.findViewById(R.id.oldTrip_textView).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.newTrip_textView:
                startNewTrip();
                break;
            case R.id.oldTrip_textView:
                viewOldTrip();
                break;
        }
    }

    private void startNewTrip() {
        Intent intent = new Intent(getActivity(), NewTripActivity.class);
        startActivity(intent);
    }

    private void viewOldTrip(){
        Intent intent = new Intent(getActivity(), OldTripsActivity.class);
        startActivity(intent);
    }
}
