p1) Pagination
  Repository

Extend JpaRepository:

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment(String department, Pageable pageable);

}
Service
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public Page<Employee> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

  public Page<Employee> getDepartment(
            String department,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByDepartment(
                department,
                pageable
        );
    }
}
Controller
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public Page<Employee> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getEmployees(page, size);
    }

   @GetMapping
    public Page<Employee> getDepartment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam() String department) {

        return service.getDepartment(page, size,department);
    }
}
GET /employees?page=0&size=5
  GET /employees?department=IT&page=0&size=2
