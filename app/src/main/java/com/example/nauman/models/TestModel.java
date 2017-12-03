package com.example.nauman.models;

/**
 * Created by nauman on 23-Nov-17.
 */

public class TestModel {
    private int Testid;



    private String TestName;
    private  String TestDesc;
    private  String TestImage;
    private  String TestInstructions;
    private int TestPassingPercentage;
    private int TestAttempts;
    private int Ismultiple;
    private int TestTotalQuestions;
    private int IsNegativeMarks;
    private int TestType;
    private int SpecMarks;
    private int Totalmcqs;
    public int getTotalmcqs() {
        return Totalmcqs;
    }

    public void setTotalmcqs(int totalmcqs) {
        Totalmcqs = totalmcqs;
    }


    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }
    public int getTestid() {
        return Testid;
    }

    public void setTestid(int testid) {
        Testid = testid;
    }

    public String getTestDesc() {
        return TestDesc;
    }

    public void setTestDesc(String testDesc) {
        TestDesc = testDesc;
    }

    public String getTestImage() {
        return TestImage;
    }

    public void setTestImage(String testImage) {
        TestImage = testImage;
    }

    public String getTestInstructions() {
        return TestInstructions;
    }

    public void setTestInstructions(String testInstructions) {
        TestInstructions = testInstructions;
    }

    public int getTestPassingPercentage() {
        return TestPassingPercentage;
    }

    public void setTestPassingPercentage(int testPassingPercentage) {
        TestPassingPercentage = testPassingPercentage;
    }

    public int getTestAttempts() {
        return TestAttempts;
    }

    public void setTestAttempts(int testAttempts) {
        TestAttempts = testAttempts;
    }

    public int getIsmultiple() {
        return Ismultiple;
    }

    public void setIsmultiple(int ismultiple) {
        Ismultiple = ismultiple;
    }

    public int getTestTotalQuestions() {
        return TestTotalQuestions;
    }

    public void setTestTotalQuestions(int testTotalQuestions) {
        TestTotalQuestions = testTotalQuestions;
    }

    public int getIsNegativeMarks() {
        return IsNegativeMarks;
    }

    public void setIsNegativeMarks(int isNegativeMarks) {
        IsNegativeMarks = isNegativeMarks;
    }

    public int getTestType() {
        return TestType;
    }

    public void setTestType(int testType) {
        TestType = testType;
    }

    public int getSpecMarks() {
        return SpecMarks;
    }

    public void setSpecMarks(int specMarks) {
        SpecMarks = specMarks;
    }
}
