package gpacalculatorpackage;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
        MainFragment.onButtonPressedListener {

    private int backStackCount = 0;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null)
                return;

            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, mainFragment).commit();
        }
    }

    @Override
    public void onNextButtonPressed(String option) {
        if (option.equals("semester list")) {
            Intent i = new Intent(this, SemesterListActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_left_entering, R.anim.slide_left_exiting);
            finish();
        } else if (option.equals("gpa goal")) {
            Intent i = new Intent(this, GPAGoalActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_left_entering, R.anim.slide_left_exiting);
            finish();
            backStackCount = 1;
        }
    }

    @Override
    public void onPrevButtonPressed(String fromWhere) {
        if (fromWhere.equals("semester list")) {
            MainFragment mainFragment = new MainFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, mainFragment);
            if (backStackCount == 2)
                backStackCount = 1;
            else if (backStackCount == 1)
                backStackCount = 0;

            transaction.commit();
        } else if (fromWhere.equals("courses")) {
            SemesterListFragment semesterListFragment = new SemesterListFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, semesterListFragment);
            if (backStackCount == 2)
                backStackCount = 1;
            else if (backStackCount == 1)
                backStackCount = 0;

            transaction.commit();
        } else if (fromWhere.equals("gpa goal")) {
            MainFragment mainFragment = new MainFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, mainFragment);
            if (backStackCount == 2)
                backStackCount = 1;
            else if (backStackCount == 1)
                backStackCount = 0;

            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (backStackCount == 1) {
            super.onBackPressed();
            backStackCount = 0;
        } else if (backStackCount == 3) {
            finish();
        } else {
            backStackCount = 3;
            toast = Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT);
            toast.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backStackCount = 0;
                }
            }, 2000);
        }
    }

    @Override
    protected void onPause() {
        if (toast != null)
            toast.cancel();
        super.onPause();
    }

}
