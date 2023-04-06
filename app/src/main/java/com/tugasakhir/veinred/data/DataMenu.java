package com.tugasakhir.veinred.data;

public class DataMenu {

    String title;
    int image;

    public DataMenu(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "DataMenu{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
