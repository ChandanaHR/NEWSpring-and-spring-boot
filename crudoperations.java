//Frontend
//Add student
import { useState } from "react";

function AddStudent() {
  const [student, setStudent] = useState({ name: "", email: "" });

  const handleChange = (e) => {
    setStudent({ ...student, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    fetch("http://localhost:8080/students", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(student),
    })
      .then((res) => res.json())
      .then(() => alert("Student Added"));
  };

  return (
    <form onSubmit={handleSubmit}>
      <input name="name" onChange={handleChange} placeholder="Name" />
      <input name="email" onChange={handleChange} placeholder="Email" />
      <button type="submit">Add</button>
    </form>
  );
}
//Display data
import { useEffect, useState } from "react";

function ViewStudents() {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/students")
      .then((res) => res.json())
      .then((data) => setStudents(data));
  }, []);

  return (
    <div>
      {students.map((s) => (
        <div key={s.id}>
          {s.name} - {s.email}
        </div>
      ))}
    </div>
  );
}

export default ViewStudents;
//update
function updateStudent(student) {
  fetch("http://localhost:8080/students", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(student),
  })
    .then((res) => res.json())
    .then(() => alert("Updated"));
}
//DELETE
function deleteStudent(id) {
  fetch(`http://localhost:8080/students/${id}`, {
    method: "DELETE",
  }).then(() => alert("Deleted"));
}



// OR another code
import { useEffect, useState } from "react";

function App() {
  const [students, setStudents] = useState([]);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [id, setId] = useState(null); // for update

  // 🔹 READ (get all data)
  const getStudents = () => {
    fetch("http://localhost:8080/students")
      .then((res) => res.json())
      .then((data) => setStudents(data));
  };

  useEffect(() => {
    getStudents();
  }, []);

  // 🔹 ADD or UPDATE
  const handleSubmit = (e) => {
    e.preventDefault();

    const student = { id, name, email };

    const method = id ? "PUT" : "POST";

    fetch("http://localhost:8080/students", {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(student),
    })
      .then((res) => res.json())
      .then(() => {
        alert(id ? "Updated" : "Added");

        setName("");
        setEmail("");
        setId(null);

        getStudents(); // refresh list
      });
  };

  // 🔹 DELETE
  const deleteStudent = (id) => {
    fetch(`http://localhost:8080/students/${id}`, {
      method: "DELETE",
    }).then(() => {
      alert("Deleted");
      getStudents();
    });
  };

  // 🔹 EDIT (fill form)
  const editStudent = (s) => {
    setId(s.id);
    setName(s.name);
    setEmail(s.email);
  };

  return (
    <div>
      <h2>Simple CRUD App</h2>

      {/* FORM */}
      <form onSubmit={handleSubmit}>
        <input
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <button type="submit">
          {id ? "Update" : "Add"}
        </button>
      </form>

      <hr />

      {/* LIST */}
      {students.map((s) => (
        <div key={s.id}>
          {s.name} - {s.email}

          <button onClick={() => editStudent(s)}>Edit</button>
          <button onClick={() => deleteStudent(s.id)}>Delete</button>
        </div>
      ))}
    </div>
  );
}

export default App;

//Backend code
//Controller code
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    // CREATE
    @PostMapping
    public Student addStudent(@RequestBody Student s) {
        return service.save(s);
    }

    // READ ALL
    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Student getById(@PathVariable int id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping
    public Student update(@RequestBody Student s) {
        return service.update(s);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "Deleted Successfully";
    }
}

export default AddStudent;

//service code
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public Student save(Student s) {
        return repo.save(s);
    }

    public List<Student> getAll() {
        return repo.findAll();
    }

    public Student getById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Student update(Student s) {
        return repo.save(s);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }
}
