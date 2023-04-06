package com.tugasakhir.veinred.data;

public class DataImageLocal {

    String title;
    String path;
    long size;

    public DataImageLocal(String title, String path, long size) {
        this.title = title;
        this.path = path;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DataImageLocal{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
