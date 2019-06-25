package com.jeongwoochang.sleepapp.util.data;

public class Menu {
    private String title;
    private int imgRes;

    public Menu(String title, int imgRes) {
        this.title = title;
        this.imgRes = imgRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}
