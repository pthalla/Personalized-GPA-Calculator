package gpacalculatorpackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GPAGoalFragment extends Fragment {

    private static final String CURRENTCUMULATIVECREDITS = "currentcumulativecredits";
    private static final String CURRENTCUMULATIVEGPA = "currentcumulativegpa";
    private static final String DESIREDGPA = "desiredgpa";
    private static final String RESULT = "result";

    private EditText currentCumulativeCreditsEditText;
    private EditText currentCumulativeGPAEditText;
    private EditText desiredGPAEditText;
    private Button calculate;
    private Button clear;
    private TextView result;
    private View dummyView;
    private Button nextButton;
    private Button prevButton;

    private int currentCumulativeCredits;
    private double currentCumulativeGPA;
    private double desiredGPA;


    private GPAGoalCalculator gpaGoalCalculator;
    MainFragment.onButtonPressedListener buttonListener;

    @Override
    public void onResume() {
        super.onResume();
        dummyView.requestFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        gpaGoalCalculator = new GPAGoalCalculator();

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentCumulativeCredits = settings.getInt(CURRENTCUMULATIVECREDITS, 0);

        currentCumulativeGPA = Double.longBitsToDouble(settings.getLong(CURRENTCUMULATIVEGPA, 0));
        desiredGPA = Double.longBitsToDouble(settings.getLong(DESIREDGPA, 0));

        gpaGoalCalculator.setResult(settings.getString(RESULT, ""));
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gpa_goal, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        dummyView = view.findViewById(R.id.dummy_view);
        result = (TextView) view.findViewById(R.id.result);
        result.setText(gpaGoalCalculator.getResult());

        currentCumulativeCreditsEditText =
                (EditText) view.findViewById(R.id.current_cumulative_credits);
        if (currentCumulativeCredits != 0)
            currentCumulativeCreditsEditText.setText(Integer.toString(currentCumulativeCredits));
        currentCumulativeCreditsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    currentCumulativeCredits =
                            Integer.parseInt(charSequence.toString());
                    gpaGoalCalculator.setCurrentCumulativeCredits(currentCumulativeCredits);
                } catch (Exception e) {
                    currentCumulativeCredits = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        currentCumulativeCreditsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    currentCumulativeCreditsEditText.clearFocus();
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    handled = true;
                    dummyView.requestFocus();
                }
                return handled;
            }
        });

        currentCumulativeGPAEditText = (EditText) view.findViewById(R.id.current_cumulative_gpa);
        if (currentCumulativeGPA != 0)
            currentCumulativeGPAEditText.setText(Double.toString(currentCumulativeGPA));
        currentCumulativeGPAEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    currentCumulativeGPA =
                            Double.parseDouble(currentCumulativeGPAEditText.getText().toString());
                    gpaGoalCalculator.setCurrentCumulativeGPA(currentCumulativeGPA);
                } catch (Exception e) {
                    currentCumulativeGPA = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        currentCumulativeGPAEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    currentCumulativeGPAEditText.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    handled = true;
                    dummyView.requestFocus();
                }
                return handled;
            }
        });

        desiredGPAEditText = (EditText) view.findViewById(R.id.desired_gpa);
        if (desiredGPA != 0)
            desiredGPAEditText.setText(Double.toString(desiredGPA));
        desiredGPAEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    desiredGPA =
                            Double.parseDouble(desiredGPAEditText.getText().toString());
                    gpaGoalCalculator.setDesiredGPA(desiredGPA);
                } catch (Exception e) {
                    desiredGPA = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        desiredGPAEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    desiredGPAEditText.clearFocus();
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    handled = true;
                    dummyView.requestFocus();
                }
                return handled;
            }
        });

        calculate = (Button) view.findViewById(R.id.calculate_gpa_goal_button);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currentCumulativeCreditsEditText.hasFocus() ||
                            currentCumulativeGPAEditText.hasFocus() ||
                            desiredGPAEditText.hasFocus()) {
                        currentCumulativeCreditsEditText.clearFocus();
                        currentCumulativeGPAEditText.clearFocus();
                        desiredGPAEditText.clearFocus();

                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(calculate.getWindowToken(), 0);

                    }

                    currentCumulativeCredits =
                            Integer.parseInt(currentCumulativeCreditsEditText.getText().toString());
                    currentCumulativeGPA =
                            Double.parseDouble(currentCumulativeGPAEditText.getText().toString());
                    desiredGPA = Double.parseDouble(desiredGPAEditText.getText().toString());

                    gpaGoalCalculator.setCurrentCumulativeCredits(currentCumulativeCredits);
                    gpaGoalCalculator.setCurrentCumulativeGPA(currentCumulativeGPA);
                    gpaGoalCalculator.setDesiredGPA(desiredGPA);

                    gpaGoalCalculator.setResult("");
                    gpaGoalCalculator.setCount(0);
                    gpaGoalCalculator.calculate();

                    result.setText(gpaGoalCalculator.getResult());
                } catch (Exception e) {
                    result.setText("Error! One or more of the fields are empty!");
                }
            }
        });

        clear = (Button) view.findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCumulativeCreditsEditText.hasFocus() ||
                        currentCumulativeGPAEditText.hasFocus() ||
                        desiredGPAEditText.hasFocus()) {
                    currentCumulativeCreditsEditText.clearFocus();
                    currentCumulativeGPAEditText.clearFocus();
                    desiredGPAEditText.clearFocus();

                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(clear.getWindowToken(), 0);

                }

                result.setText("");
                gpaGoalCalculator.setResult("");
                currentCumulativeCreditsEditText.setText("");
                currentCumulativeGPAEditText.setText("");
                desiredGPAEditText.setText("");
            }
        });

        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setEnabled(false);

        prevButton = (Button) view.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onPrevButtonPressed("gpa goal");
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (MainFragment.onButtonPressedListener) activity;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_right_entering,
                        R.anim.slide_right_exiting);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(CURRENTCUMULATIVECREDITS, currentCumulativeCredits);
        editor.putLong(CURRENTCUMULATIVEGPA, Double.doubleToRawLongBits(currentCumulativeGPA));
        editor.putLong(DESIREDGPA, Double.doubleToRawLongBits(desiredGPA));

        editor.putString(RESULT, gpaGoalCalculator.getResult());

        editor.commit();
    }

}
