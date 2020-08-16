package kuznikos.restapp.restapp.controller;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Faculty;
import kuznikos.restapp.restapp.model.Student;
import kuznikos.restapp.restapp.service.CourseService;
import kuznikos.restapp.restapp.service.FacultyService;
import kuznikos.restapp.restapp.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final FacultyService facultyService;

    @GetMapping("/")
    public String showStudents(Model model){
        model.addAttribute("students", studentService.showStudents());
        model.addAttribute("courses", courseService.showCourses());
        model.addAttribute("faculties", facultyService.showFaculties());
        if (!model.containsAttribute("student"))
            model.addAttribute("student", new Student());
        return "index";
    }
    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("student") Student student) throws Exception {
        studentService.save(student);
        for (Course course: student.getCourses()) {
            assignStudentToCourse(student.getId(), course.getId());
        }
        if (student.getStudentFaculty() != null)
            assignStudentToFaculty(student.getId(), student.getStudentFaculty().getId());
        return "redirect:/";
    }
    @GetMapping("/editStudent")
    public String editStudent(@RequestParam Long id, Model model){
        model.addAttribute("student", studentService.findById(id));
        return showStudents(model);
    }
    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long id){
        Student student = studentService.findById(id).get();
        for (Course course: student.getCourses()) {
            courseService.deleteStudentFromCourse(student, course);
        }
        if (student.getStudentFaculty() != null) {
            facultyService.deleteStudentFromFaculty(student, student.getStudentFaculty());
        }
        studentService.deleteStudent(id);
        return "redirect:/";
    }
    @GetMapping("/courses")
    public String showCourses(Model model){
        model.addAttribute("courses", courseService.showCourses());
        model.addAttribute("faculties", facultyService.showFaculties());
        if (!model.containsAttribute("course"))
            model.addAttribute("course", new Course());
        return "courses";
    }
    @PostMapping("/addCourse")
    public String addCourse(@ModelAttribute("course") Course course) throws Exception {
        courseService.save(course);
        if(course.getCourseFaculty() != null)
            assignCourseToFaculty(course.getId(), course.getCourseFaculty().getId());
        return "redirect:/courses";
    }
    @GetMapping("/editCourse")
    public String editCourse(@RequestParam Long id, Model model){
        model.addAttribute("course", courseService.findById(id).get());
        return showCourses(model);
    }
    @GetMapping("/deleteCourse")
    public String deleteCourse(@RequestParam Long id){
        Course course = courseService.findById(id).get();
        Set<Student> students = courseService.findById(id).get().getStudents();
        studentService.deleteAllFromCourse(course, students);
        course.getStudents().removeAll(students);
        if (course.getCourseFaculty() != null) {
            facultyService.deleteCourseFromFaculty(course, course.getCourseFaculty());
            courseService.deleteCourseFromFaculty(course);
        }
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
    @GetMapping("/student-course/{id}")
    public String studentCourses(@PathVariable Long id, Model model){
        model.addAttribute("courses", studentService.findById(id).get().getCourses());
        model.addAttribute("student", studentService.findById(id).get());
        model.addAttribute("remain", courseService.findAll().stream().filter(course ->
                !studentService.findById(id).get().getCourses().contains(course)).collect(Collectors.toList()));
        return "student-courses";
    }
    @GetMapping("/student-course/{student_id}/{course_id}")
    public String assignStudentToCourse(@PathVariable Long student_id, @PathVariable Long course_id) throws Exception {

            Optional<Student> student = studentService.findById(student_id);
            if (!student.isPresent())
                throw new Exception("Student doesn't exist");
            Optional<Course> course = courseService.findById(course_id);
            if (!course.isPresent())
                throw new Exception("Course doesn't exist");
            course.get().setStudent(student.get());
            courseService.save(course.get());
        return "redirect:/student-course/"+ student_id;
    }
    @GetMapping("/deleteStudentFromCourse")
    public String deleteStudentFromCourse(@RequestParam Long student_id, @RequestParam Long course_id ) throws Exception{

        Optional<Course> course = courseService.findById(course_id);
        Optional<Student> student = studentService.findById(student_id);
        if (!student.isPresent())
            throw new Exception("Student doesn't exist");
        course.ifPresent(course1 -> studentService.deleteStudentFromCourse(student.get(), course1));
        studentService.save(student.get());
        course.ifPresent(course1 -> courseService.deleteStudentFromCourse(student.get(), course1));
        courseService.save(course.get());
        return "redirect:/student-course/"+ student_id;
    }
    @GetMapping("/faculties")
    public String showFaculties(Model model) {
        model.addAttribute("courses", courseService.showCourses());
        model.addAttribute("faculties", facultyService.showFaculties());
        if (!model.containsAttribute("faculty"))
            model.addAttribute("faculty", new Faculty());
        return "faculty";
    }

    @PostMapping("/addFaculty")
    public String addFaculty(@ModelAttribute("faculty") Faculty faculty) {
        facultyService.save(faculty);
        return "redirect:/faculties";
    }
    @PostMapping("/students-faculty/{student_id}/{faculty_id}")
    public void assignStudentToFaculty(@PathVariable Long student_id, @PathVariable Long faculty_id) throws Exception {

        Set<Student> studentSet = new HashSet<>();
        Optional<Student> student = studentService.findById(student_id);
        if (!student.isPresent())
            throw new Exception("Student doesn't exist");
        studentSet.add(student.get());
        Optional<Faculty> faculty = facultyService.findById(faculty_id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        student.get().setStudentFaculty(faculty.get());
        facultyService.addStudentsToFaculty(faculty_id, studentSet);
    }
    @GetMapping("courses-faculty/{course_id}/{faculty_id}")
    public String assignCourseToFaculty(@PathVariable Long course_id, @PathVariable Long faculty_id) throws Exception {

        Set<Course> courseSet = new HashSet<>();
        Optional<Course> course = courseService.findById(course_id);
        if (!course.isPresent())
            throw new Exception("Course doesn't exist");
        if (course.get().getCourseFaculty() != null)
            deleteCourseFromFaculty(course.get().getId(), course.get().getCourseFaculty().getId());
        courseSet.add(course.get());
        Optional<Faculty> faculty = facultyService.findById(faculty_id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        course.get().setCourseFaculty(faculty.get());
        facultyService.addCoursesToFaculty(faculty_id, courseSet);
        return "redirect:/faculty-courses/"+ faculty_id;
    }
    @PostMapping("/deleteStudentFromFaculty/{id}")
    public String deleteStudentFromFaculty(@RequestBody Set<Long> students, @PathVariable Long id ) throws Exception{

        Optional<Faculty> faculty = facultyService.findById(id);
        for (Long st: students) {
            Optional<Student> student = studentService.findById(st);
            if (!student.isPresent())
                throw new Exception("Student doesn't exist");
            if (!faculty.isPresent())
                throw new Exception("Faculty doesn't exist");
            studentService.deleteStudentFromFaculty(student.get());
            facultyService.deleteStudentFromFaculty(student.get(), faculty.get());
            studentService.save(student.get());
            facultyService.save(faculty.get());
        }
        return "redirect:/";
    }
    @GetMapping("/faculty-courses/{faculty_id}")
    public String showFacultyCourses(@PathVariable Long faculty_id, Model model){
        model.addAttribute("faculty", facultyService.findById(faculty_id).get());
        model.addAttribute("courses", facultyService.findById(faculty_id).get().getCourses());
        model.addAttribute("remain", courseService.findAll().stream().filter(course ->
                !facultyService.findById(faculty_id).get().getCourses().contains(course)).collect(Collectors.toList()));
        return "faculty-courses";
    }

    @GetMapping("/deleteCourseFromFaculty/{course_id}/{faculty_id}")
    public String deleteCourseFromFaculty(@PathVariable Long course_id, @PathVariable Long faculty_id ) throws Exception{

        Optional<Faculty> faculty = facultyService.findById(faculty_id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        Optional<Course> course = courseService.findById(course_id);
        if (!course.isPresent())
            throw new Exception("Course doesn't exist");
        courseService.deleteCourseFromFaculty(course.get());
        facultyService.deleteCourseFromFaculty(course.get(), faculty.get());
        courseService.save(course.get());
        facultyService.save(faculty.get());
        return "redirect:/";
    }
    @GetMapping("/deleteFaculty/{id}")
    public String deleteFaculty(@PathVariable Long id) throws Exception {

        Faculty faculty = facultyService.findById(id).get();
        Set<Student> students = facultyService.findById(id).get().getStudents();
        Set<Long> id_student = new HashSet<>();
        for (Student st: students)
            id_student.add(st.getId());
        deleteStudentFromFaculty(id_student, id);
        faculty.getStudents().removeAll(students);

        Set<Course> courses = facultyService.findById(id).get().getCourses();
        for (Course course: courses)
            courseService.deleteCourseFromFaculty(course);
        faculty.getCourses().removeAll(courses);
        facultyService.deleteFaculty(id);
        return "redirect:/faculties";
    }

}
