package gpacalculatorpackage;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 7/14/2014.
 */
public class Semester {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_CREDITS = "credits";
    private static final String JSON_GPA = "gpa";
    private static final String JSON_CUMULATIVECREDITS = "cumulativecredits";
    private static final String JSON_CUMULATIVEGPA = "cumulativegpa";
    private static final String JSON_NUMBEROFCOURSES = "numberofcourses";
    private static final String JSON_COURSES = "courses";
    private static final String JSON_COURSENAMES = "coursenames";

    private UUID id;
    private String title;
    private int credits;
    private double GPA;
    private int cumulativeCredits;
    private double cumulativeGPA;
    private int numberOfCourses;
    private ArrayList<Course> courses;
    private ArrayList<LinearLayout> courseLayoutArray;
    private ArrayList<String> courseNames;

    private Gson gson = new Gson();
    private String coursesAsString;
    private String courseNamesAsString;

    public Semester() {
        id = UUID.randomUUID();
        courses = new ArrayList<Course>();
        courseLayoutArray = new ArrayList<LinearLayout>();
        courseNames = new ArrayList<String>();
    }

    public Semester(JSONObject json) throws JSONException {
        id = UUID.fromString(json.getString(JSON_ID));
        title = json.getString(JSON_TITLE);
        credits = json.getInt(JSON_CREDITS);
        GPA = json.getDouble(JSON_GPA);
        cumulativeCredits = json.getInt(JSON_CUMULATIVECREDITS);
        cumulativeGPA = json.getDouble(JSON_CUMULATIVEGPA);
        numberOfCourses = json.getInt(JSON_NUMBEROFCOURSES);

        coursesAsString = json.getString(JSON_COURSES);
        Type coursesType = new TypeToken<ArrayList<Course>>(){}.getType();
        courses = gson.fromJson(coursesAsString, coursesType);

        courseNamesAsString = json.getString(JSON_COURSENAMES);
        Type courseNamesType = new TypeToken<ArrayList<String>>(){}.getType();
        courseNames = gson.fromJson(courseNamesAsString, courseNamesType);

        courseLayoutArray = new ArrayList<LinearLayout>();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id.toString());
        json.put(JSON_TITLE, title);
        json.put(JSON_CREDITS, credits);
        json.put(JSON_GPA, GPA);
        json.put(JSON_CUMULATIVECREDITS, cumulativeCredits);
        json.put(JSON_CUMULATIVEGPA, cumulativeGPA);
        json.put(JSON_NUMBEROFCOURSES, numberOfCourses);

        coursesAsString = gson.toJson(courses);
        json.put(JSON_COURSES, coursesAsString);

        courseNamesAsString = gson.toJson(courseNames);
        json.put(JSON_COURSENAMES, courseNamesAsString);

        return json;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public double getGPA() {
        return GPA;
    }

    public void setCumulativeCredits(int cumulativeCredits) {
        this.cumulativeCredits = cumulativeCredits;
    }

    public void setCumulativeGPA(double cumulativeGPA) {
        this.cumulativeGPA = cumulativeGPA;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<LinearLayout> getCourseLayoutArray() {
        return courseLayoutArray;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }

    public ArrayList<String> getCourseNames() {
        return courseNames;
    }

}
