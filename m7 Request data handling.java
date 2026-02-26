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
