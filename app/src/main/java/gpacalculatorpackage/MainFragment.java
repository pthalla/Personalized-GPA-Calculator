package gpacalculatorpackage;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class MainFragment extends Fragment {

    private RadioButton currentGPAbutton;
    private RadioButton goalGPAbutton;
    private Button nextButton;
    private Button prevButton;

    RadioButtonSaveState saveState;
    onButtonPressedListener buttonListener;

    public interface onButtonPressedListener {
        public void onNextButtonPressed(String option);
        public void onPrevButtonPressed(String fromWhere);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (onButtonPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onButtonPressedListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        buttonListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveState = RadioButtonSaveState.get(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        currentGPAbutton = (RadioButton) view.findViewById(R.id.currentGPA_button);
        currentGPAbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveState.setState("semester list");
            }
        });

        goalGPAbutton = (RadioButton) view.findViewById(R.id.goalGPA_button);
        goalGPAbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveState.setState("gpa goal");
            }
        });

        if (saveState.getState().equals("semester list"))
            currentGPAbutton.setChecked(true);
        else if (saveState.getState().equals("gpa goal"))
            goalGPAbutton.setChecked(true);

        prevButton = (Button) view.findViewById(R.id.prev_button);
        prevButton.setEnabled(false);

        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onNextButtonPressed(saveState.getState());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return true;
        }
    }

}
