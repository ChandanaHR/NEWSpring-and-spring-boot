//Text field handling
<form action="/saveName" method="post">
Name:
<input type="text" name="name">
<input type="submit">
</form>
@PostMapping("/saveName")
@ResponseBody
public String saveName(@RequestParam String name) {

    return "Name received: " + name;
}

//Password field handling
<form action="/login" method="post">
Password:
<input type="password" name="password">
<input type="submit">
</form>
    @PostMapping("/login")
@ResponseBody
public String login(@RequestParam String password) {

    return "Password received: " + password;
}

//Radio button handling
<form action="/gender" method="post">
Gender:
<input type="radio" name="gender" value="Male"> Male
<input type="radio" name="gender" value="Female"> Female
<input type="submit">
</form>
    @PostMapping("/gender")
@ResponseBody
public String gender(@RequestParam String gender) {

    return "Selected Gender: " + gender;
}

//Checkbox handling
<form action="/skills" method="post">
Skills:
<input type="checkbox" name="skills" value="Java"> Java
<input type="checkbox" name="skills" value="Python"> Python
<input type="checkbox" name="skills" value="SQL"> SQL
<input type="submit">
</form>
    @PostMapping("/skills")
@ResponseBody
public String skills(@RequestParam List<String> skills) {

    return "Skills: " + skills;
}

//Dropdown handling
<form action="/country" method="post">

Country:

<select name="country">

<option value="India">India</option>
<option value="USA">USA</option>
<option value="UK">UK</option>

</select>

<input type="submit">

</form>
    @PostMapping("/country")
@ResponseBody
public String country(@RequestParam String country) {

    return "Selected country: " + country;
}

//Multiple selection dropdown
<form action="/courses" method="post">
Courses:
<select name="courses" multiple>
<option value="Java">Java</option>
<option value="Python">Python</option>
<option value="SQL">SQL</option>
</select>
<input type="submit">
</form>
    @PostMapping("/courses")
@ResponseBody
public String courses(@RequestParam List<String> courses) {

    return "Selected courses: " + courses;
}
//Date input handling
<form action="/dob" method="post">
Date of Birth:
<input type="date" name="dob">
<input type="submit">
</form>
    @PostMapping("/dob")
@ResponseBody
public String dob(@RequestParam String dob) {

    return "DOB: " + dob;
}

//File upload handling
<form action="/upload" method="post" enctype="multipart/form-data">
Upload File:
<input type="file" name="file">
<input type="submit">
</form>
    @PostMapping("/upload")
@ResponseBody
public String uploadFile(@RequestParam("file") MultipartFile file) {

    return "Uploaded file: " + file.getOriginalFilename();
}
//Get file details
@PostMapping("/upload")
@ResponseBody
public String fileDetails(@RequestParam("file") MultipartFile file) {

    String name = file.getOriginalFilename();
    long size = file.getSize();
    String type = file.getContentType();

    return "Name: " + name + 
           "<br>Size: " + size + 
           "<br>Type: " + type;
}
//View file content
//If the file is .txt, .csv, .json, you can read and display content.
@PostMapping("/upload")
@ResponseBody
public String readFile(@RequestParam("file") MultipartFile file) throws Exception {

    String content = new String(file.getBytes());

    return "File Content:<br>" + content;
}
//Download file but not from frontend
@GetMapping("/download")
public ResponseEntity<byte[]> download() throws Exception {

    Path path = Paths.get("C:/uploads/file.pdf");

    byte[] data = Files.readAllBytes(path);

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=file.pdf")
            .body(data);
}
//View pdf in browser but not from frontend
@GetMapping("/viewPdf")
public ResponseEntity<byte[]> viewPdf() throws Exception {

    Path path = Paths.get("C:/uploads/sample.pdf");

    byte[] data = Files.readAllBytes(path);

    return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .body(data);
}
