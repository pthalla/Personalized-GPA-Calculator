package gpacalculatorpackage;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created on 7/16/2014.
 */
public class GPACalculatorJSONSerializer {
    private Context context;
    private String filename;

    public GPACalculatorJSONSerializer(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    public ArrayList<Semester> loadSemesters() throws IOException, JSONException {
        ArrayList<Semester> semesters = new ArrayList<Semester>();
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                jsonString.append(line);

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++)
                semesters.add(new Semester(array.getJSONObject(i)));

        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null)
                reader.close();
        }

        return semesters;
    }

    public void saveSemesters(ArrayList<Semester> semesters) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Semester s : semesters)
            array.put(s.toJSON());

        Writer writer = null;

        try {
            OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}

