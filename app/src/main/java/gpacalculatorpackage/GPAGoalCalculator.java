package gpacalculatorpackage;

import android.util.Log;
import android.widget.EditText;

/**
 * Created on 8/6/2014.
 */
public class GPAGoalCalculator {

    private int currentCumulativeCredits;
    private double currentCumulativeGPA;
    private double desiredGPA;
    private String result = "";
    private int count;

    public void calculate() {
        if (currentCumulativeCredits > 999 || currentCumulativeCredits < 1) {
            result = "Error! Current cumulative credits\n must be within 1 - 999.";
            return;
        }
        if (currentCumulativeGPA < 0.0 || currentCumulativeGPA > 4.0) {
            result = "Error! Current cumulative GPA\nmust be within 0.0 - 4.0.";
            return;
        }
        if (desiredGPA < 0.0 || desiredGPA > 4.0) {
            result = "Error! Desired GPA must\nbe within 0.0 - 4.0.";
            return;
        }
        if (currentCumulativeGPA >= desiredGPA) {
            result = "Error! Desired GPA must be higher\nthan current cumulative GPA";
            return;
        }

        if (desiredGPA < 0.7) {
            for (double GPA = 4.0; GPA > 0.6; GPA -= 0.1) {
                for (int credits = 1; credits <= 999 - currentCumulativeCredits; credits++) {
                    double newGPA = (((currentCumulativeGPA * currentCumulativeCredits) +
                            (GPA * credits)) / (currentCumulativeCredits + credits));

                    if (newGPA >= desiredGPA) {
                        double GPARounded = GPA;
                        GPARounded = GPARounded * 100;
                        GPARounded = Math.round(GPARounded);
                        GPARounded = GPARounded / 100;

                        if (GPARounded <= 0.7) {
                            result += credits + " credits with a " + GPARounded + " GPA\n";
                            count++;
                        } else
                            result += credits + " credits with a " + GPARounded + " GPA\n\n"
                                    + "  --or--\n\n";
                        break;

                    } else if (credits == 999 - currentCumulativeCredits && count == 0 &&
                            !result.equals("")) {
                        result = result.substring(0, result.length() - 8);
                        return;
                    }
                }
            }
        } else {
            for (double GPA = 4.0; GPA > desiredGPA; GPA -= 0.1) {
                for (int credits = 1; credits <= 999 - currentCumulativeCredits; credits++) {
                    double newGPA = (((currentCumulativeGPA * currentCumulativeCredits)
                            + (GPA * credits)) / (currentCumulativeCredits + credits));

                    if (newGPA >= desiredGPA) {
                        double GPARounded = GPA;
                        GPARounded = GPARounded * 100;
                        GPARounded = Math.round(GPARounded);
                        GPARounded = GPARounded / 100;

                        double ifStatementGPA = GPARounded - 0.1;
                        ifStatementGPA = ifStatementGPA * 100;
                        ifStatementGPA = Math.round(ifStatementGPA);
                        ifStatementGPA = ifStatementGPA / 100;

                        if (ifStatementGPA <= desiredGPA) {
                            result += credits + " credits with a " + GPARounded + " GPA\n";
                            count++;

                        } else {
                            result += credits + " credits with a " + GPARounded + " GPA\n\n"
                                    + "  --or--\n\n";
                        }
                        break;

                    } else if (credits == 999 - currentCumulativeCredits && count == 0 &&
                            !result.equals("")) {
                        result = result.substring(0, result.length() - 8);
                        return;
                    }
                }
            }
        }
        if (result.equals(""))
            result = "This desired GPA cannot be obtained within the credits limit (999).";
    }

    private String toLetterGrade(double GPARounded) {
        if (GPARounded == 4.0)
            return "A+";
        else if (GPARounded == 3.9 || GPARounded == 3.8 || GPARounded == 3.7)
            return "A-";
        else if (GPARounded == 3.6 || GPARounded == 3.5 || GPARounded == 3.4 || GPARounded == 3.3)
            return "B+";
        else if (GPARounded == 3.2 || GPARounded == 3.1 || GPARounded == 3.0)
            return "B";
        else if (GPARounded == 2.9 || GPARounded == 2.8 || GPARounded == 2.7)
            return "B-";
        else if (GPARounded == 2.6 || GPARounded == 2.5 || GPARounded == 2.4 || GPARounded == 2.3)
            return "C+";
        else if (GPARounded == 2.2 || GPARounded == 2.1 || GPARounded == 2.0)
            return "C";
        else if (GPARounded == 1.9 || GPARounded == 1.8 || GPARounded == 1.7)
            return "C-";
        else if (GPARounded == 1.6 || GPARounded == 1.5 || GPARounded == 1.4 || GPARounded == 1.3)
            return "D+";
        else if (GPARounded == 1.2 || GPARounded == 1.1 || GPARounded == 1.0)
            return "D";
        else if (GPARounded == 0.9 || GPARounded == 0.8 || GPARounded == 0.7)
            return "D-";
        else
            return "F";
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDesiredGPA(double desiredGPA) {
        this.desiredGPA = desiredGPA;
    }

    public void setCurrentCumulativeGPA(double currentCumulativeGPA) {
        this.currentCumulativeGPA = currentCumulativeGPA;
    }

    public void setCurrentCumulativeCredits(int currentCumulativeCredits) {
        this.currentCumulativeCredits = currentCumulativeCredits;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
