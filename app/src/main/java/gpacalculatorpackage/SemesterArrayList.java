package gpacalculatorpackage;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 7/16/2014.
 */
public class SemesterArrayList {
    private static final String FILENAME = "semesters.json";

    private ArrayList<Semester> semesters;
    private Semester currentSemester;
    private GPACalculatorJSONSerializer serializer;

    int cumulativeCredits;
    double cumulativeGPA;

    private static SemesterArrayList semesterArrayList;
    private Context appContext;

    private SemesterArrayList(Context appContext) {
        this.appContext = appContext;
        serializer = new GPACalculatorJSONSerializer(this.appContext, FILENAME);

        try {
            semesters = serializer.loadSemesters();
        } catch (Exception e) {
            semesters = new ArrayList<Semester>();
        }
    }

    public static SemesterArrayList get(Context c) {
        if (semesterArrayList == null)
            semesterArrayList = new SemesterArrayList(c.getApplicationContext());
        return semesterArrayList;
    }

    public Semester getSemester(UUID id) {
        for (Semester s : semesters)
            if (s.getId().equals(id))
                return s;
        return null;
    }

    public void addSemester(Semester s) {
        semesters.add(s);
        saveSemesters();
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public void deleteSemester(Semester s) {
        semesters.remove(s);
        saveSemesters();
    }

    public boolean saveSemesters() {
        try {
            serializer.saveSemesters(semesters);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

   public void setCumulativeCredits(int cumulativeCredits) {
        this.cumulativeCredits = cumulativeCredits;
   }

   public int getCumulativeCredits() {
        cumulativeCredits = 0;
        for (Semester s : semesters)
            cumulativeCredits += s.getCredits();
        return cumulativeCredits;
    }

    public void setCumulativeGPA(double cumulativeGPA) {
        this.cumulativeGPA = cumulativeGPA;
    }

    public double getCumulativeGPA() {
        double cumulativeGPA;
        double gradePoints = 0.0;
        int credits;
        int getCumulativeCredits = getCumulativeCredits();

        for (Semester s : semesters) {
            credits = s.getCredits();
            for (int i = 0; i < s.getCourses().size(); i++)
                if (s.getCourses().get(i).getGradeCredits() == 0) {
                    credits -= s.getCourses().get(i).getCreditsForText();
                    getCumulativeCredits -= s.getCourses().get(i).getCreditsForText();
                }

            gradePoints += (s.getGPA() * credits);
        }

        cumulativeGPA = gradePoints / getCumulativeCredits;
        cumulativeGPA = (int) (cumulativeGPA * 10000) / 10000.0;

        return cumulativeGPA;
    }

}
