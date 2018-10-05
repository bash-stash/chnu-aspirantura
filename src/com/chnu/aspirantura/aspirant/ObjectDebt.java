package com.chnu.aspirantura.aspirant;

public class ObjectDebt {
    private int startYear;
    private int endYear;

    private int startCourse;
    private int endCourse;

    private int startSemestr;
    private int endSemestr;

    public ObjectDebt(int startYear, int endYear, int startCourse, int endCourse, int startSemestr, int endSemestr) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.startCourse = startCourse;
        this.endCourse = endCourse;
        this.startSemestr = startSemestr;
        this.endSemestr = endSemestr;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getStartCourse() {
        return startCourse;
    }

    public int getEndCourse() {
        return endCourse;
    }

    public int getStartSemestr() {
        return startSemestr;
    }

    public int getEndSemestr() {
        return endSemestr;
    }
}


