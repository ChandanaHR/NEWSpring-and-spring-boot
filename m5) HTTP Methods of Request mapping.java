GET: 
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public String getUsers() {
        return "User List";
    }
}
http://localhost:8080/api/users

POST: REQUEST
    Create User.java in eclipse
    package com.example.mvc1;

public class User {
	private String name;
	private String email;
	
	public User() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
Create controller
	package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")   // React Vite port
public class UserController {

    private List<User> users = new ArrayList<>();

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }
}
React code
	import { useState, useEffect } from "react";

function App() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [users, setUsers] = useState([]);

  // Load users
  const loadUsers = () => {
    fetch("http://localhost:8080/api/users")
      .then(res => res.json())
      .then(data => setUsers(data));
  };

  // Add user (POST)
  const addUser = () => {
    fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        name: name,
        email: email
      })
    })
    .then(res => res.json())
    .then(data => {
      setUsers([...users, data]);
      setName("");
      setEmail("");
    });
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Add User</h1>

      <input
        type="text"
        placeholder="Enter Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <br /><br />

      <input
        type="email"
        placeholder="Enter Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <br /><br />

      <button onClick={addUser}>Submit</button>

      <hr />

      <h2>User List</h2>
      <button onClick={loadUsers}>Load Users</button>

      <ul>
        {users.map((user, index) => (
          <li key={index}>
            {user.name} - {user.email}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;

put request
	package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")   // React Vite port
public class UserController {

    private List<User> users = new ArrayList<>();

    // 🔹 CREATE USER (POST)
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    // 🔹 GET ALL USERS
    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    // 🔹 UPDATE USER (PUT)
    @PutMapping("/users/{index}")
    public User updateUser(@PathVariable int index,
                           @RequestBody User updatedUser) {

        if (index >= 0 && index < users.size()) {
            users.set(index, updatedUser);
            return updatedUser;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // 🔹 DELETE USER (Optional)
    @DeleteMapping("/users/{index}")
    public String deleteUser(@PathVariable int index) {

        if (index >= 0 && index < users.size()) {
            users.remove(index);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
}
React code
	import { useState } from "react";

function App() {

  const [users, setUsers] = useState([]);
  const [editIndex, setEditIndex] = useState(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  // Load Users
  const loadUsers = () => {
    fetch("http://localhost:8080/api/users")
      .then(res => res.json())
      .then(data => setUsers(data));
  };

  // Select user for editing
  const editUser = (index) => {
    setEditIndex(index);
    setName(users[index].name);
    setEmail(users[index].email);
  };

  // UPDATE USER (PUT)
  const updateUser = () => {
    fetch(`http://localhost:8080/api/users/${editIndex}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        name: name,
        email: email
      })
    })
    .then(res => res.json())
    .then(updatedUser => {
      const updatedList = [...users];
      updatedList[editIndex] = updatedUser;
      setUsers(updatedList);
      setEditIndex(null);
      setName("");
      setEmail("");
    });
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>User List</h2>
      <button onClick={loadUsers}>Load Users</button>

      <ul>
        {users.map((user, index) => (
          <li key={index}>
            {user.name} - {user.email}
            <button onClick={() => editUser(index)}>Edit</button>
          </li>
        ))}
      </ul>

      <hr />

      <h3>Edit User</h3>

      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Name"
      />

      <br /><br />

      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
      />

      <br /><br />

      <button onClick={updateUser}>Update</button>
    </div>
  );
}

export default App;
