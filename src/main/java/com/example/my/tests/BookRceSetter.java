package com.example.my.tests;

import java.io.IOException;
import java.io.Serializable;

public final class BookRceSetter implements Serializable {
    public String title;
    public String filename;
    public String cmd;

    public BookRceSetter(String title, String filename, String cmd)
    {
        this.title = title;
        this.filename = filename;
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "Book [title=" + this.title + ", filename=" + this.filename + "]";
    }

    // vulnerable setter that can be attacked during deserialization
    public void setCmd(String cmd) throws IOException {
        this.cmd = cmd;

        // execute the set command, because.. why not?
        System.out.println("BookRceSetter - setter triggeeeeeeered");
        System.out.println("Command: " + this.cmd);
        Runtime.getRuntime().exec(this.cmd);
    }
}
