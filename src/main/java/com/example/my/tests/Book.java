package com.example.my.tests;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public final class Book implements Serializable {
    public String title;
    public String filename;

    public Book(String title, String filename)
    {
        this.title = title;
        this.filename = filename;
    }

    public String readFile() throws IOException {
        File file = new File(this.filename); // CWE 73?!
        return FileUtils.readFileToString(file, "UTF-8");
    }

    @Override
    public String toString() {
        return "Book [title=" + this.title + ", filename=" + this.filename + "]";
    }
}
