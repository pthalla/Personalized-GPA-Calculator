package gpacalculatorpackage;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created on 7/24/2014.
 */
public class GPAGoalActivity extends FragmentActivity implements
        MainFragment.onButtonPressedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null)
                return;

            GPAGoalFragment gpaGoalFragment = new GPAGoalFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, gpaGoalFragment).commit();
        }
    }

    @Override
    public void onNextButtonPressed(String option) {

    }

    @Override
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
