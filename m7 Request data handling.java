//@RequestParam
@Controller
public class StudentController {

    @GetMapping("/student")
    @ResponseBody
    public String getStudent(
            @RequestParam String name,
            @RequestParam int age) {

        return "Name: " + name + ", Age: " + age;
    }
}

// @PathVariable
    @Controller
public class StudentController {

    @GetMapping("/student/{id}")
    @ResponseBody
    public String getStudentById(@PathVariable int id) {

        return "Student ID: " + id;
    }
}
// Multiple path variables
    @GetMapping("/student/{id}/course/{courseId}")
@ResponseBody
public String getDetails(
        @PathVariable int id,
        @PathVariable int courseId) {

    return "Student: " + id + ", Course: " + courseId;
}
