package com.example.my.tests;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class HomeController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/file-test")
	public String cwe73Test(@RequestParam(name = "file") String filename) throws IOException {
		File file = new File(filename); // CWE 73 TP
		String content = FileUtils.readFileToString(file, "UTF-8");

		return "File content: " + content; // CWE 80 TP
	}

	@RequestMapping("/serialize")
	public String serialize(@RequestParam(name = "doUrlEncode") boolean doUrlEncode) throws UnsupportedEncodingException {
		Book myBook = new Book("A cool book!", "blub.txt");
		String myBookSerialized = serializeBook(myBook);

		// we do URL encoding here because, currently, the `/deserialize` route accepts the serialized object in the
		// query string (and base64 may contain e.g. `+`)
		if (doUrlEncode) {
			myBookSerialized = URLEncoder.encode(myBookSerialized, StandardCharsets.UTF_8.toString());
		}

		return myBookSerialized;
	}

	@RequestMapping("/deserializeUrlEncoded")
	public String deserialize(@RequestParam(name = "b") String bookBase64) {
		System.out.println("Received: " + bookBase64);
		Book myBook = deserializeBook(bookBase64);

		return myBook.toString();
	}

	@PostMapping("/deserialize")
	public String deserializePost(@RequestBody String bookBase64) {
		System.out.println("Received: " + bookBase64);
		Book myBook = deserializeBook(bookBase64);

		return myBook.toString();
	}

	@RequestMapping("/deserialize-and-get-file")
	public String deserializeAndGetFile(@RequestParam(name = "b") String bookBase64) throws IOException {
		System.out.println("Received: " + bookBase64);
		Book myBook = deserializeBook(bookBase64);

		// get the file from property `filename`
		String fileContent = myBook.readFile();

		return fileContent;
	}

	/**
	 * Serialiazes a `Book` object and returns it as base64 string
	 */
	private String serializeBook(Book myBook) {
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

	/**
	 * Deserialiazes a base64 string back into a `Book` object and returns it
	 */
	private Book deserializeBook(String base64SerializedBook) {
		Book someBook = null;

		try {
			byte[] data = Base64.getDecoder().decode(base64SerializedBook);

			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(data)
			);
			someBook = (Book) ois.readObject();
			ois.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return someBook;
	}
}
