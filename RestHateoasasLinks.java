//Hateoas
package mvc.demo1;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/studentshateoas")
public class Hateoaslinks {
	@GetMapping("/{id}")
	public EntityModel<Student1> getStudent(@PathVariable Integer id) {
		Student1 student = new Student1(id, "Chandana");
		EntityModel<Student1> model = EntityModel.of(student);
		
		model.add(linkTo(methodOn(Hateoaslinks.class).getStudent(id)).withSelfRel());
		return model;
	}
}
//Student1
package mvc.demo1;

public class Student1 {
	private Integer id;
	private String name;
	
	public Student1() {
		
	}
	public Student1(Integer id, String name) {
		super();
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

2 ) next relation
	@GetMapping("/{id}")
public EntityModel<Student> getStudent(
        @PathVariable Integer id) {

    Student student =
            new Student(id, "Chandana");

    EntityModel<Student> model =
            EntityModel.of(student);

    model.add(
            linkTo(
                    methodOn(StudentController.class)
                            .getStudent(id)
            ).withSelfRel()
    );

    model.add(
            linkTo(
                    methodOn(StudentController.class)
                            .getStudent(id + 1)
            ).withRel("next")
    );

    return model;
}

3) previous relation
		@GetMapping("/{id}")
public EntityModel<Student> getStudent(
        @PathVariable Integer id) {

    Student student =
            new Student(id, "Rahul");

    EntityModel<Student> model =
            EntityModel.of(student);

    model.add(
            linkTo(
                    methodOn(StudentController.class)
                            .getStudent(id)
            ).withSelfRel()
    );

    model.add(
            linkTo(
                    methodOn(StudentController.class)
                            .getStudent(id - 1)
            ).withRel("previous")
    );

    return model;
}
