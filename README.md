* How to Run:
  - Run via `mvn spring-boot:run`

* Routes:
  - Read `blub.txt` file: Visit `/file-test?file=blub.txt`
  - Serialize:
    - Get a serialized `Book` object: Visit `/serialize?doUrlEncode=false`
    - Get a serialized `Book` object that is also URL encoded: Visit `/serialize?doUrlEncode=true`
  - Deserialize:
    - `GET`: Visit `GET /deserialize?b=<base64book_UrlEncoded>`
    - `POST`: Visit `POST /deserialize` and provide the `object-to-deserialize` in the body

* Exploiting a `Gadget Chain` (that still works as of August 2022!):
  - As a PoC, the `pom.xml` contains `commons-fileupload:1.3.1` and `commons-io:2.4`
  - Thus, we can use the `FileUpload1` gadget chain from `ysoserial`: `java -jar ysoserial.jar FileUpload1 'write;/tmp;HACKEEED' | base64`
  - We send this to our `Spring Boot` app, which will create a randomly named file (e.g. `/tmp/upload_1e2897d1_aac7_4210_8911_57cbb6ac37c0_00000000.tmp`) in `/tmp` with the content `HACKEEED`

* We also created our own `Gadget Chain`..
  * General Description: 
    - We created a new class `BookRCE` that executes a command upon deserialization
    - We also create a separate mini app `Evil.java` where we serialize an instance of `BookRCE`, give it a command of our choosing, and then send the serialized base64-encoded string to `POST /deserialize`
  * Exploitation:
    - We first compile `Evil.java` via `javac BookRCE.java Evil.java`
    - Now, we `cd` into the `java` folder, and run `java com.example.my.tests.Evil`
      - This creates a file `naughty.ser` which contains a base64 string
      - Last, we send this base64 string to `POST /deserialize`
    - `PoC`:
      - We simply do a `curl` while the target of our `curl` has a `python webserver` running