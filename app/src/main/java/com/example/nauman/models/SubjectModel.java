package com.example.nauman.models;

/**
 * Created by nauman on 22-Nov-17.
 */

public class SubjectModel {


    private String  SubjectName;
    private String  SubjectObjective;
    private String  SubjectTestChapters;
    private String  SubjectImage;
    private String  SubjectExplanation;
    private String  SubjectReferenceBooks;
    private int  TotalTest;
    private int  TotalMcqs;
    private int  SubId;
    private String  SubjectCode;
    private int catid;

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getSubjectObjective() {
        return SubjectObjective;
    }

    public void setSubjectObjective(String subjectObjective) {
        SubjectObjective = subjectObjective;
    }

    public String getSubjectTestChapters() {
        return SubjectTestChapters;
    }

    public void setSubjectTestChapters(String subjectTestChapters) {
        SubjectTestChapters = subjectTestChapters;
    }

    public String getSubjectImage() {
        return SubjectImage;
    }

    public void setSubjectImage(String subjectImage) {
        SubjectImage = subjectImage;
    }

    public String getSubjectExplanation() {
        return SubjectExplanation;
    }

    public void setSubjectExplanation(String subjectExplanation) {
        SubjectExplanation = subjectExplanation;
    }

    public String getSubjectReferenceBooks() {
        return SubjectReferenceBooks;
    }

    public void setSubjectReferenceBooks(String subjectReferenceBooks) {
        SubjectReferenceBooks = subjectReferenceBooks;
    }

    public int getTotalTest() {
        return TotalTest;
    }

    public void setTotalTest(int totalTest) {
        TotalTest = totalTest;
    }

    public int getTotalMcqs() {
        return TotalMcqs;
    }

    public void setTotalMcqs(int totalMcqs) {
        TotalMcqs = totalMcqs;
    }

    public int getSubId() {
        return SubId;
    }

    public void setSubId(int subId) {
        SubId = subId;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        SubjectCode = subjectCode;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }
}
