package gpacalculatorpackage;
import android.content.Context;

/**
 * Created on 7/24/2014.
 */
public class RadioButtonSaveState {
    private String state;

    private static RadioButtonSaveState saveState;
    private Context appContext;

    private RadioButtonSaveState(Context appContext) {
        this.appContext = appContext;
        state = "semester list";
    }

    public static RadioButtonSaveState get(Context c) {
        if (saveState == null)
            saveState = new RadioButtonSaveState(c.getApplicationContext());
        return saveState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
