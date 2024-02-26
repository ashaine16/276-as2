package as2.as2.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import as2.as2.models.Student;
import as2.as2.models.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/students/view")
    public String viewStudents(Model model) {
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "students/viewStudents";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newstudent, HttpServletResponse response) {
        String newStudentName = newstudent.get("name");
        double newStudentWeight = Double.parseDouble(newstudent.get("weight"));
        double newStudentHeight = Double.parseDouble(newstudent.get("height"));
        String newStudentHairColor = newstudent.get("hairColor");
        double newStudentGpa = Double.parseDouble(newstudent.get("gpa"));
        String newStudentProgram = newstudent.get("program");
        int newStudentAge = Integer.parseInt(newstudent.get("age"));
        studentRepo.save(new Student(newStudentName, newStudentWeight, newStudentHeight, newStudentHairColor, newStudentGpa, newStudentProgram, newStudentAge));
        response.setStatus(201);
        return "students/addedStudent";
    }

    @GetMapping("/students/add")
    public String addStudentRedirect() {
        return "redirect:/add.html";
    }

    @GetMapping("/students/editList")
    public String listStudents(Model model) {
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "students/editList";
    }    

    //edit student based on name and age
    @PostMapping("/students/edit")
    public String showEditForm(@RequestParam String name, @RequestParam int age, Model model) {
        List<Student> students = studentRepo.findByNameAndAge(name, age);
        if(!students.isEmpty()) {
            Student student = students.get(0);
            model.addAttribute("student", student);
            return "students/editStudent";
        } else {
            return "students/error";
        }
    }

    //post mapping for editing student
    @PostMapping("/students/update")
    public String editStudent(@RequestParam String name, @RequestParam int age, @RequestParam Map<String, String> newstudent, HttpServletResponse response) {
        List<Student> students = studentRepo.findByNameAndAge(name, age);
        if(!students.isEmpty()) {
            Student student = students.get(0);
            student.setName(newstudent.get("name"));
            student.setWeight(Double.parseDouble(newstudent.get("weight")));
            student.setHeight(Double.parseDouble(newstudent.get("height")));
            student.setHairColor(newstudent.get("hairColor"));
            student.setGpa(Double.parseDouble(newstudent.get("gpa")));
            student.setProgram(newstudent.get("program"));
            student.setAge(Integer.parseInt(newstudent.get("age")));
            studentRepo.save(student);
            response.setStatus(201);
            return "students/updatedStudent";
        } else {
            return "students/error";
        }
    }

    @GetMapping("/students/deleteList")
    public String showDeleteForm(Model model) {
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "/students/deleteList";
    }

    //post mapping delete based on name and age
    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam String name, @RequestParam int age) {
        List<Student> students = studentRepo.findByNameAndAge(name, age);
        if(!students.isEmpty()) {
            Student student = students.get(0);
            studentRepo.delete(student);
            return "students/removedStudent";
        } else {
            return "students/error";
        }
    }
}

