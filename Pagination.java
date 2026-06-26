//Offset-based pagination
//Model class
@Entity
public class Student {

    @Id
    private Integer id;

    private String name;

    public Student() {
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
//repository code
@Repository
public interface StudentRepository
        extends JpaRepository<Student, Integer> {
}
//Service code
@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Page<Student> getStudents(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return repository.findAll(pageable);
    }
}
//Controller code
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public Page<Student> getStudents(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size) {

        return service.getStudents(page, size);
    }
}



//Cursor based pagination
//Entity
@Entity
public class Student {

    @Id
    private Integer id;

    private String name;

    public Student() {
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
}

//Repository
//Instead of findall(pageable)
We create custom query.
    @Repository
public interface StudentRepository
        extends JpaRepository<Student,Integer> {

    List<Student> findTop5ByOrderByIdAsc();

    List<Student> findTop5ByIdGreaterThanOrderByIdAsc(
            Integer cursor);
}

//Service
@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public List<Student> getStudents(
            Integer cursor) {

        if(cursor == null) {

            return repository
                    .findTop5ByOrderByIdAsc();
        }

        return repository
                .findTop5ByIdGreaterThanOrderByIdAsc(
                        cursor);
    }
}

//Controller
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> getStudents(

            @RequestParam(required = false)
            Integer cursor) {

        return service.getStudents(cursor);
    }
}
