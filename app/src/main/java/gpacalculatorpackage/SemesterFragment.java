package gpacalculatorpackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class SemesterFragment extends Fragment {

    public static final String EXTRA_SEMESTER_ID = "gpacalculator.SEMESTER_ID";
    private static final String SCALE = "scale";

    private Semester semester;
    private TextView titleField;
    private TextView credits;
    private TextView gpa;
    private TextView cumulativeCredits;
    private TextView cumulativeGPA;
    private TextView gpaScale;
    private LinearLayout root;
    int check = 0;

    private Callbacks callbacks;

    private SemesterArrayList semesters;
    private ArrayList<LinearLayout> courseLayoutArray;
    private ArrayList<Course> courses;
    private ArrayList<String> courseNames;
    Scale scale;
    SharedPreferences sharedPreferences;

    private Button nextButton;
    private Button prevButton;

    public interface Callbacks {
        void onSemesterUpdated(Semester semester);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Callbacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public static SemesterFragment newInstance(UUID semesterId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);

        SemesterFragment fragment = new SemesterFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID semesterId = (UUID) getArguments().getSerializable(EXTRA_SEMESTER_ID);
        semester = SemesterArrayList.get(getActivity()).getSemester(semesterId);
        semesters = SemesterArrayList.get(getActivity());

        courses = SemesterArrayList.get(getActivity()).getSemester(semesterId).getCourses();

        if (courseLayoutArray == null)
            courseLayoutArray = SemesterArrayList.get(getActivity())
                    .getSemester(semesterId).getCourseLayoutArray();

        if (courseNames == null)
            courseNames =
                    SemesterArrayList.get(getActivity()).getSemester(semesterId).getCourseNames();

        scale = Scale.get(getActivity());

        setHasOptionsMenu(true);
    }

    private void deleteCourse(LinearLayout root) {
        root.removeView(courseLayoutArray.get(courseLayoutArray.size() - 1));
        courseLayoutArray.remove(courseLayoutArray.size() - 1);
    }

    private void newCourse(LinearLayout root) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.course_layout, root, false);
        final LinearLayout courseLinearLayout = (LinearLayout) view.findViewById(R.id.course);
        courseLayoutArray.add(courseLinearLayout);
        root.addView(courseLinearLayout);

        Spinner creditsSpinner = (Spinner) courseLinearLayout.findViewById(R.id.credits_spinner);
        ArrayAdapter<CharSequence> creditsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.credits_array, R.layout.spinner_item);
        creditsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_credits);
        creditsSpinner.setAdapter(creditsAdapter);
        creditsSpinner.setSelection(courses.get(courseLayoutArray
                .indexOf(courseLinearLayout)).getSelectedPosCredits());

        creditsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (check < courseLayoutArray.size())
                    check++;
                if (check == courseLayoutArray.size()) {
                    check++;
                    return;
                }
                if (check > courseLayoutArray.size()) {
                    Course course = courses.get(courseLayoutArray.indexOf(courseLinearLayout));
                    course.setSelectedPosCredits(i);
                    course.resetCredits();
                    course.addToCredits(course.creditToCredit((String)
                            adapterView.getItemAtPosition(i)));

                    int creditsTempText = 0;
                    double creditsTempCalculation = 0;
                    double gradeCreditsTemp = 0;
                    for (Course c : courses) {
                        creditsTempText += c.getCreditsForText();
                        creditsTempCalculation += c.getCreditsForCalculation();
                        gradeCreditsTemp += c.getGradeCredits();
                    }

                    credits.setText(Integer.toString(creditsTempText));
                    gpa.setText(Double.toString((int) (((gradeCreditsTemp)
                            / (creditsTempCalculation)) * 10000) / 10000.0));

                    cumulativeCredits.setText(Integer.toString(semesters.getCumulativeCredits()));
                    cumulativeGPA.setText(Double.toString(semesters.getCumulativeGPA()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Spinner gradesSpinner = (Spinner) courseLinearLayout.findViewById(R.id.grades_spinner);
        ArrayAdapter<CharSequence> gradesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grades_array, R.layout.spinner_item);
        gradesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_grades);
        gradesSpinner.setAdapter(gradesAdapter);
        gradesSpinner.setSelection(courses.get(courseLayoutArray
                .indexOf(courseLinearLayout)).getSelectedPosGrade());
        gradesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (check < courseLayoutArray.size())
                    check++;
                if (check == courseLayoutArray.size()) {
                    check++;
                    return;
                }
                if (check > courseLayoutArray.size()) {
                    Course course = courses.get(courseLayoutArray.indexOf(courseLinearLayout));
                    course.setSelectedPosGrade(i);
                    course.resetGrade();
                    course.addToGrade(course.gradeToValue(((String)
                            adapterView.getItemAtPosition(i)), scale.getScaleString()));

                    double creditsTempCalculation = 0;
                    double gradeCreditsTemp = 0;
                    for (Course c : courses) {
                        creditsTempCalculation += c.getCreditsForCalculation();
                        gradeCreditsTemp += c.getGradeCredits();
                    }

                    gpa.setText(Double.toString((int) (((gradeCreditsTemp)
                            / (creditsTempCalculation)) * 10000) / 10000.0));

                    cumulativeGPA.setText(Double.toString(semesters.getCumulativeGPA()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        root = (LinearLayout) view.findViewById(R.id.root);

        titleField = (TextView) view.findViewById(R.id.semester_title);
        titleField.setText("Semester " + (semesters.getSemesters().indexOf(semester) + 1));
        semester.setTitle(titleField.getText().toString());
        callbacks.onSemesterUpdated(semester);

        gpaScale = (TextView) view.findViewById(R.id.gpa_scale);
        //gpaScale.setTextColor(R.color.grayed_out);

        credits = (TextView) view.findViewById(R.id.semester_credits);
        credits.setText(Integer.toString(semester.getCredits()));
        credits.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence c, int i, int i2, int i3) {
                semester.setCredits(Integer.parseInt(c.toString()));
                callbacks.onSemesterUpdated(semester);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        gpa = (TextView) view.findViewById(R.id.semester_gpa);
        gpa.setText(Double.toString(semester.getGPA()));
        gpa.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence c, int i, int i2, int i3) {
                semester.setGPA(Double.parseDouble(c.toString()));
                callbacks.onSemesterUpdated(semester);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cumulativeCredits = (TextView) view.findViewById(R.id.cumulative_credits);
        cumulativeCredits.setText(Integer.toString(semesters.getCumulativeCredits()));
        cumulativeCredits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                semesters.setCumulativeCredits(Integer.parseInt(charSequence.toString()));
                semester.setCumulativeCredits(Integer.parseInt(charSequence.toString()));
                callbacks.onSemesterUpdated(semester);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cumulativeGPA = (TextView) view.findViewById(R.id.cumulative_gpa);
        cumulativeGPA.setText(Double.toString(semesters.getCumulativeGPA()));
        cumulativeGPA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                semesters.setCumulativeGPA(Double.parseDouble(charSequence.toString()));
                semester.setCumulativeGPA(Double.parseDouble(charSequence.toString()));
                callbacks.onSemesterUpdated(semester);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        nextButton.setTextColor(getResources().getColor(R.color.grayed_out));

        prevButton = (Button) view.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SemesterListActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_right_entering,
                        R.anim.slide_right_exiting);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        semesters.saveSemesters();
        EditText courseNameET;
        String courseNameString;
        for (LinearLayout courseLayout : courseLayoutArray) {
            courseNameET = (EditText) courseLayout.findViewById(R.id.course_name);
            courseNameString = courseNameET.getText().toString();
            courseNames.set(courseLayoutArray.indexOf(courseLayout), courseNameString);
        }

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCALE, scale.getScaleString());
        editor.commit();
    }


    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        scale.setScaleString(sharedPreferences.getString(SCALE, ""));

        if (scale.getScaleString().equals("dumb scaling")) {
            gpaScale.setText("(Scale: A- = 3.7)");
        } else {
            gpaScale.setText("(Scale: A- = 4.0)");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SemesterArrayList.get(getActivity()).saveSemesters();
        for (int i = 0; i < semester.getNumberOfCourses(); i++)
            deleteCourse(root);

        semesters.saveSemesters();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (semester.getNumberOfCourses() == 0) {
            courses.add(new Course());
            newCourse(root);
            semester.setNumberOfCourses(semester.getNumberOfCourses() + 1);
            courseNames.add(courses.get(courses.size() - 1).getName());
        } else
            for (int i = 0; i < semester.getNumberOfCourses(); i++)
                newCourse(root);

        String courseNameString;
        for (LinearLayout courseLayout : courseLayoutArray) {
            final EditText courseNameET = (EditText) courseLayout.findViewById(R.id.course_name);
            courseNameString = courseNames.get(courseLayoutArray.indexOf(courseLayout));
            courseNameET.setText(courseNameString);

            courseNameET.setOnEditorActionListener(new  TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    boolean handled = false;
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        courseNameET.clearFocus();
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        handled = true;
                    }
                    return handled;
                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_semester, menu);
        MenuItem item = menu.findItem(R.id.switch_button);
        if (scale.getScaleString().equals("dumb scaling"))
            item.setTitle("Switch GPA scale (A- = 4.0)");
        else
            item.setTitle("Switch GPA scale (A- = 3.7)");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getActivity(), SemesterListActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_right_entering,
                        R.anim.slide_right_exiting);
                getActivity().finish();
                return true;
            case R.id.menu_item_new_course:
                courses.add(new Course());
                newCourse(root);
                semester.setNumberOfCourses(semester.getNumberOfCourses() + 1);
                courseNames.add(courses.get(courses.size() - 1).getName());
                return true;
            case R.id.menu_item_delete_course:
                if (semester.getNumberOfCourses() > 1) {
                    courses.remove(courses.get(courses.size() - 1));
                    deleteCourse(root);
                    courseNames.remove(courseNames.get(courseNames.size() - 1));
                    semester.setNumberOfCourses(semester.getNumberOfCourses() - 1);
                    check--;

                    int creditsTempText = 0;
                    double creditsTempCalculation = 0;
                    double gradeCreditsTemp = 0;
                    for (Course c : courses) {
                        creditsTempText += c.getCreditsForText();
                        creditsTempCalculation += c.getCreditsForCalculation();
                        gradeCreditsTemp += c.getGradeCredits();
                    }
                    credits.setText(Integer.toString(creditsTempText));
                    gpa.setText(Double.toString((int) (((gradeCreditsTemp)
                            / (creditsTempCalculation)) * 10000) / 10000.0));

                    cumulativeCredits.setText(Integer.toString(semesters.getCumulativeCredits()));
                    cumulativeGPA.setText(Double.toString(semesters.getCumulativeGPA()));

                    return true;
                }
            case R.id.switch_button:
                if (scale.getScaleString().equals("dumb scaling")) {
                    scale.setScaleString("smart scaling");
                    item.setTitle("Switch GPA scale (A- = 3.7)");
                } else {
                    scale.setScaleString("dumb scaling");
                    item.setTitle("Switch GPA scale (A- = 4.0)");
                }
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
