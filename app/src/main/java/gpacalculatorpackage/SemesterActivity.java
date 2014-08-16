package gpacalculatorpackage;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created on 7/16/2014.
 */
public class SemesterActivity extends SingleFragmentActivity implements SemesterFragment.Callbacks,
        SemesterListFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        UUID semesterId = (UUID)getIntent()
                .getSerializableExtra(SemesterFragment.EXTRA_SEMESTER_ID);
        return SemesterFragment.newInstance(semesterId);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SemesterListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_entering, R.anim.slide_right_exiting);
        finish();
    }

    public void onSemesterUpdated(Semester semester) {

    }

    public void onSemesterSelected(Semester semester) {

    }
}
