package com.example.safecarrier;

public class MyItem {
    String file;
    String time;

    public MyItem(String file, String time) {
        this.file = file;
        this.time = time;
    }

    public MyItem() {}

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
