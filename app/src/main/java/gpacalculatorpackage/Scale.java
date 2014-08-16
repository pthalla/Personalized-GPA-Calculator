package gpacalculatorpackage;

import android.content.Context;

/**
 * Created on 8/15/2014.
 */
public class Scale {
    private String scaleString;

    private static Scale scale;
    private Context appContext;

    private Scale(Context appContext) {
        this.appContext = appContext;
        scaleString = "dumb scaling";
    }

    public static Scale get(Context c) {
        if (scale == null)
            scale = new Scale(c.getApplicationContext());
        return scale;
    }

    public String getScaleString() {
        return scaleString;
    }

    public void setScaleString(String scaleString) {
        this.scaleString = scaleString;
    }


}
