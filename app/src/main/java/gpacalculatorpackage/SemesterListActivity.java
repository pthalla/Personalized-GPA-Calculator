package gpacalculatorpackage;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created on 7/16/2014.
 */
public class SemesterListActivity extends SingleFragmentActivity
        implements SemesterListFragment.Callbacks, SemesterFragment.Callbacks,
        SemesterListFragment.onButtonPressedListener{

    @Override
    protected Fragment createFragment() {
        return new SemesterListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    public void onSemesterSelected(Semester semester) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            SemesterListFragment.currentSemesterIndex =
                    SemesterArrayList.get(this).getSemesters().indexOf(semester);
            Intent i = new Intent(this, SemesterActivity.class);
            i.putExtra(SemesterFragment.EXTRA_SEMESTER_ID, semester.getId());
            startActivityForResult(i, 0);
            overridePendingTransition(R.anim.slide_left_entering, R.anim.slide_left_exiting);
            finish();

        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = SemesterFragment.newInstance(semester.getId());

            if (oldDetail != null)
                ft.remove(oldDetail);

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    public void onSemesterUpdated(Semester semester) {
        FragmentManager fm = getSupportFragmentManager();
        SemesterListFragment listFragment = (SemesterListFragment)
                fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }

    public void onNextButtonPressed(int currentSemesterNumber) {
        Intent i = new Intent(this, SemesterActivity.class);
        i.putExtra(SemesterFragment.EXTRA_SEMESTER_ID,
                SemesterArrayList.get(this).getSemesters().get(currentSemesterNumber).getId());
        startActivityForResult(i, 0);
        overridePendingTransition(R.anim.slide_left_entering, R.anim.slide_left_exiting);
        finish();
    }

    public void onPrevButtonPressed(String fromWhere) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_entering, R.anim.slide_right_exiting);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_entering, R.anim.slide_right_exiting);
        finish();
    }
}

