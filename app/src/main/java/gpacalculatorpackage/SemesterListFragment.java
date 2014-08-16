package gpacalculatorpackage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created on 7/14/2014.
 */
public class SemesterListFragment extends ListFragment {

    private static final String CURRENTKEYINDEX = "currentkeyindex";

    private Button prevButton;
    private Button nextButton;
    private TextView cumulativeGPA;
    private TextView cumulativeCredits;

    private onButtonPressedListener buttonListener;
    private ArrayList<Semester> semesters;
    private Callbacks callbacks;

    private SharedPreferences sharedPreferences;
    public static int currentSemesterIndex;

    public interface Callbacks {
        void onSemesterSelected(Semester semester);
    }

    public interface onButtonPressedListener {
        public void onPrevButtonPressed(String fromWhere);

        public void onNextButtonPressed(int currentSemesterNumber);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (Callbacks) activity;
            buttonListener = (onButtonPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interfaces");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        buttonListener = null;
        callbacks = null;
    }

    public void updateUI() {
        ((SemesterAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        semesters = SemesterArrayList.get(getActivity()).getSemesters();
        SemesterAdapter adapter = new SemesterAdapter(semesters);

        setListAdapter(adapter);
        setRetainInstance(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_semester_list, container, false);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            registerForContextMenu(listView);
        else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.semester_list_item_context, menu);
                    return true;
                }

                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_semester:
                            SemesterAdapter adapter = (SemesterAdapter) getListAdapter();
                            SemesterArrayList semesterArrayList =
                                    SemesterArrayList.get(getActivity());

                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    if ((semesterArrayList.getSemesters()
                                            .indexOf(adapter.getItem(i))) == currentSemesterIndex) {
                                        nextButton.setEnabled(false);
                                        nextButton.setTextColor(getResources()
                                                .getColor(R.color.grayed_out));
                                    } else if ((semesterArrayList.getSemesters()
                                            .indexOf(adapter.getItem(i))) < currentSemesterIndex) {
                                      currentSemesterIndex--;
                                    }
                                    semesterArrayList.deleteSemester(adapter.getItem(i));
                                }
                            }
                            if (semesterArrayList.getSemesters().isEmpty()) {
                                nextButton.setEnabled(false);
                                nextButton.setTextColor(getResources().getColor(R.color.grayed_out));
                            }

                            mode.finish();
                            adapter.notifyDataSetChanged();
                            cumulativeGPA.setText(Double.toString(SemesterArrayList
                                    .get(getActivity()).getCumulativeGPA()));
                            cumulativeCredits.setText(Integer.toString(SemesterArrayList
                                    .get(getActivity()).getCumulativeCredits()));
                            return true;
                        default:
                            return false;
                    }
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {

                }
            });

        }

        cumulativeGPA = (TextView) view.findViewById(R.id.cumulative_gpa_list);
        cumulativeGPA.setText(Double.toString(SemesterArrayList
                .get(getActivity()).getCumulativeGPA()));

        cumulativeCredits = (TextView) view.findViewById(R.id.cumulative_credits_list);
        cumulativeCredits.setText(Integer.toString(SemesterArrayList
                .get(getActivity()).getCumulativeCredits()));

        nextButton = (Button) view.findViewById(R.id.next_button);
        if (semesters.size() == 0) {
            nextButton.setEnabled(false);
            nextButton.setTextColor(getResources().getColor(R.color.grayed_out));
        } else {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonListener.onNextButtonPressed(currentSemesterIndex);
                }
            });
        }


        prevButton = (Button) view.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onPrevButtonPressed("semester list");
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENTKEYINDEX, currentSemesterIndex);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentSemesterIndex = sharedPreferences.getInt(CURRENTKEYINDEX, 0);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int pos, long id) {
        Semester semester = ((SemesterAdapter) getListAdapter()).getItem(pos);
        callbacks.onSemesterSelected(semester);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((SemesterAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_semester_list, menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_semester:
                Semester semester = new Semester();

                SemesterArrayList.get(getActivity()).addSemester(semester);
                SemesterArrayList.get(getActivity()).getSemester(semester.getId())
                        .setTitle("Semester " + (semesters.indexOf(semester) + 1));
                ((SemesterAdapter) getListAdapter()).notifyDataSetChanged();
                SemesterArrayList.get(getActivity()).saveSemesters();
                return true;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menInfo) {
        getActivity().getMenuInflater().inflate(R.menu.semester_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        SemesterAdapter adapter = (SemesterAdapter) getListAdapter();
        Semester semester = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_semester:
                if (SemesterArrayList.get(getActivity())
                        .getSemester(semester.getId()).equals(semesters.get(currentSemesterIndex))
                        || semesters.isEmpty()) {
                    nextButton.setEnabled(false);
                    nextButton.setTextColor(getResources().getColor(R.color.grayed_out));
                }
                SemesterArrayList.get(getActivity()).deleteSemester(semester);
                adapter.notifyDataSetChanged();

                cumulativeGPA.setText(Double.toString(SemesterArrayList
                        .get(getActivity()).getCumulativeGPA()));
                cumulativeCredits.setText(Integer.toString(SemesterArrayList
                        .get(getActivity()).getCumulativeCredits()));
                SemesterArrayList.get(getActivity()).saveSemesters();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private class SemesterAdapter extends ArrayAdapter<Semester> {
        public SemesterAdapter(ArrayList<Semester> semesters) {
            super(getActivity(), android.R.layout.simple_list_item_1, semesters);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView)
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_semester, null);

            Semester s = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.semester_list_item_titleTextView);
            titleTextView.setText(s.getTitle());
            TextView gpaTextView =
                    (TextView) convertView.findViewById(R.id.semester_list_item_gpaTextView);
            gpaTextView.setText(Double.toString(s.getGPA()));
            TextView creditsTextView =
                    (TextView) convertView.findViewById(R.id.semester_list_item_creditsTextView);
            creditsTextView.setText(Integer.toString(s.getCredits()));

            return convertView;
        }
    }

}
