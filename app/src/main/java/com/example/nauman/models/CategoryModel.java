package com.example.nauman.models;

/**
 * Created by nauman on 19-Nov-17.
 */

public class CategoryModel {

    private int  catid;
    private String Tagline;
    private String  CatName;
    private String  CatPurpose;
    private String  CatDesc;
    private String  CatImg;
    private int  TotalTest;
    private int  TotalSub;

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getTagline() {
        return Tagline;
    }

    public void setTagline(String tagline) {
        Tagline = tagline;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public String getCatPurpose() {
        return CatPurpose;
    }

    public void setCatPurpose(String catPurpose) {
        CatPurpose = catPurpose;
    }

    public String getCatDesc() {
        return CatDesc;
    }

    public void setCatDesc(String catDesc) {
        CatDesc = catDesc;
    }

    public String getCatImg() {
        return CatImg;
    }

    public void setCatImg(String catImg) {
        CatImg = catImg;
    }

    public int getTotalTest() {
        return TotalTest;
    }

    public void setTotalTest(int totalTest) {
        TotalTest = totalTest;
    }

    public int getTotalSub() {
        return TotalSub;
    }

    public void setTotalSub(int totalSub) {
        TotalSub = totalSub;
    }

    public int getTotalMcqs() {
        return TotalMcqs;
    }

    public void setTotalMcqs(int totalMcqs) {
        TotalMcqs = totalMcqs;
    }

    private int  TotalMcqs;

}
