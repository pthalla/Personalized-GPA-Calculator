package gpacalculatorpackage;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;


import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 7/16/2014.
 */
public class SemesterPagerActivity extends FragmentActivity implements SemesterFragment.Callbacks {
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        final ArrayList<Semester> semesters = SemesterArrayList.get(this).getSemesters();

        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return semesters.size();
            }
            @Override
            public Fragment getItem(int pos) {
                UUID semesterId =  semesters.get(pos).getId();
                return SemesterFragment.newInstance(semesterId);
            }
        });

        UUID semesterId = (UUID)getIntent().getSerializableExtra(SemesterFragment
                .EXTRA_SEMESTER_ID);
        for (int i = 0; i < semesters.size(); i++) {
            if (semesters.get(i).getId().equals(semesterId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public void onSemesterUpdated(Semester semester) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SemesterListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_entering, R.anim.slide_right_exiting);
        finish();
    }

}
