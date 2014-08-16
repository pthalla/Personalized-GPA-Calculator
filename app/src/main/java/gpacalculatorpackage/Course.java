package gpacalculatorpackage;

/**
 * Created on 7/1/2014.
 */
public class Course {
    private double credits = 0.0;
    private double grade = 0.0;
    private int selectedPosCredits = 0;
    private int selectedPosGrade = 0;
    private String name = "";

    public double getCredits() {
        return credits;
    }

    public double getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSelectedPosCredits(int selectedPosCredits) {
        this.selectedPosCredits = selectedPosCredits;
    }

    public void setSelectedPosGrade(int selectedPosGrade) {
        this.selectedPosGrade = selectedPosGrade;
    }

    public int getSelectedPosCredits() {
        return selectedPosCredits;
    }

    public int getSelectedPosGrade() {
        return selectedPosGrade;
    }

    public void addToGrade(double grade) {
        this.grade += grade;
    }

    public void addToCredits(double credits) {
        this.credits += credits;
    }

    public void resetCredits() {
        credits = 0.0;
    }

    public void resetGrade() {
        grade = 0.0;
    }

    public double getCreditsForCalculation() {
        if (grade == 0)
            return 0;
        else
            return credits;
    }

    public int getCreditsForText() {
        return (int) credits;
    }

    public double getGradeCredits() {
        double val = credits * grade;
        val = val * 10000;
        val = Math.round(val);
        val = val / 10000;
        return val;
    }

    public double gradeToValue(String grade, String scale) {
        if (scale.equals("dumb scaling")) {
            if (grade.equals("A+"))
                return 4.0;
            else if (grade.equals("A"))
                return 4.0;
            else if (grade.equals("A-"))
                return 3.7;
            else if (grade.equals("B+"))
                return 3.3;
            else if (grade.equals("B"))
                return 3.0;
            else if (grade.equals("B-"))
                return 2.7;
            else if (grade.equals("C+"))
                return 2.3;
            else if (grade.equals("C"))
                return 2.0;
            else if (grade.equals("C-"))
                return 1.7;
            else if (grade.equals("D+"))
                return 1.3;
            else if (grade.equals("D"))
                return 1.0;
            else if (grade.equals("D-"))
                return 0.7;
            else
                return 0.0;
        } else {
            if (grade.equals("A+"))
                return 4.0;
            else if (grade.equals("A"))
                return 4.0;
            else if (grade.equals("A-"))
                return 4.0;
            else if (grade.equals("B+"))
                return 3.0;
            else if (grade.equals("B"))
                return 3.0;
            else if (grade.equals("B-"))
                return 3.0;
            else if (grade.equals("C+"))
                return 2.0;
            else if (grade.equals("C"))
                return 2.0;
            else if (grade.equals("C-"))
                return 2.0;
            else if (grade.equals("D+"))
                return 1.0;
            else if (grade.equals("D"))
                return 1.0;
            else if (grade.equals("D-"))
                return 1.0;
            else
                return 0.0;
        }

    }

    public double creditToCredit(String credit) {
        if (credit.equals("1"))
            return 1.0;
        else if (credit.equals("2"))
            return 2.0;
        else if (credit.equals("3"))
            return 3.0;
        else if (credit.equals("4"))
            return 4.0;
        else if (credit.equals("5"))
            return 5.0;
        else if (credit.equals("6"))
            return 6.0;
        else if (credit.equals("7"))
            return 7.0;
        else if (credit.equals("8"))
            return 8.0;
        else if (credit.equals("9"))
            return 9.0;
        else if (credit.equals("10"))
            return 10.0;
        else
            return 0.0;

    }

}