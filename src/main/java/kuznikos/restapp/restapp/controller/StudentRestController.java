package kuznikos.restapp.restapp.controller;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Faculty;
import kuznikos.restapp.restapp.model.Student;
import kuznikos.restapp.restapp.service.CourseService;
import kuznikos.restapp.restapp.service.FacultyService;
import kuznikos.restapp.restapp.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/rest")
@AllArgsConstructor
public class StudentRestController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final FacultyService facultyService;

    @GetMapping("/students")
    public Iterable<Student> showStudents() {
        return studentService.showStudents();
    }
    @PostMapping("/student")
    public ResponseEntity addStudent(@RequestBody Student student) {

        studentService.save(student);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/student/{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/courses")
    public Iterable<Course> showCourses() {
        return courseService.showCourses();
    }
    @PostMapping("/course")
    public ResponseEntity addCourse(@RequestBody Course course) {

        courseService.save(course);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/course/{id}")
    public ResponseEntity deleteCourse(@PathVariable Long id) {

        Course course = courseService.findById(id).get();
        Set<Student> students = courseService.findById(id).get().getStudents();
        studentService.deleteAllFromCourse(course, students);
        course.getStudents().removeAll(students);
        if (course.getCourseFaculty() != null )
            facultyService.deleteCourseFromFaculty(course, course.getCourseFaculty());
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/faculties")
    public Iterable<Faculty> showFaculties() {
        return facultyService.showFaculties();
    }
    @PostMapping("/faculty")
    public ResponseEntity addFaculty(@RequestBody Faculty faculty) {

        facultyService.save(faculty);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/faculty/{id}")
    public ResponseEntity deleteFaculty(@PathVariable Long id) throws Exception {

        Faculty faculty = facultyService.findById(id).get();
        Set<Student> students = facultyService.findById(id).get().getStudents();
        Set<Long> id_student = new HashSet<>();
        for (Student st: students)
            id_student.add(st.getId());
        deleteStudentFromFaculty(id_student, id);
        faculty.getStudents().removeAll(students);

        Set<Course> courses = facultyService.findById(id).get().getCourses();
        Set<Long> id_course = new HashSet<>();
        for (Course cr: courses)
            id_course.add(cr.getId());
        deleteCourseFromFaculty(id_course, id);
        faculty.getCourses().removeAll(courses);
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/students-faculty/{id}")
    public ResponseEntity assignStudentToFaculty(@RequestBody Set<Long> students, @PathVariable Long id) throws Exception {

        Set<Student> studentSet = new HashSet<>();
        for (Long i : students) {
            Optional<Student> student = studentService.findById(i);
            if (!student.isPresent())
                throw new Exception("Student doesn't exist");
            studentSet.add(student.get());
            Optional<Faculty> faculty = facultyService.findById(id);
            if (!faculty.isPresent())
                throw new Exception("Faculty doesn't exist");
            student.get().setStudentFaculty(faculty.get());
        }
        facultyService.addStudentsToFaculty(id, studentSet);
        return ResponseEntity.ok().build();
    }
    @PostMapping("courses-faculty/{id}")
    public ResponseEntity assignCourseToFaculty(@RequestBody Set<Long> courses, @PathVariable Long id) throws Exception {

        Set<Course> courseSet = new HashSet<>();
        for (Long i : courses) {
            Optional<Course> course = courseService.findById(i);
            if (!course.isPresent())
                throw new Exception("Course doesn't exist");
            if (course.get().getCourseFaculty() != null)
                deleteCourseFromFaculty(courses, course.get().getCourseFaculty().getId());
            courseSet.add(course.get());
            Optional<Faculty> faculty = facultyService.findById(id);
            if (!faculty.isPresent())
                throw new Exception("Faculty doesn't exist");
            course.get().setCourseFaculty(faculty.get());
        }
        facultyService.addCoursesToFaculty(id, courseSet);
        return ResponseEntity.ok().build();
    }
    @PostMapping("students-course/{id}")
    public ResponseEntity assignStudentToCourse(@RequestBody Set<Long> students, @PathVariable Long id) throws Exception {

        for (Long i : students) {
            Optional<Student> student = studentService.findById(i);
            if (!student.isPresent())
                throw new Exception("Student doesn't exist");
            Optional<Course> course = courseService.findById(id);
            if (!course.isPresent())
                throw new Exception("Course doesn't exist");
            student.get().setCourse(course.get());
            course.get().setStudent(student.get());
            studentService.save(student.get());
            courseService.save(course.get());
    /*            courseService.addStudentToCourse(id, student.get());*/
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/deleteCourseFromFaculty/{id}")
    public void deleteCourseFromFaculty(@RequestBody Set<Long> courses, @PathVariable Long id ) throws Exception{

        Optional<Faculty> faculty = facultyService.findById(id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        for (Long cr: courses) {
            Optional<Course> course = courseService.findById(cr);
            if (!course.isPresent())
                throw new Exception("Course doesn't exist");
            courseService.deleteCourseFromFaculty(course.get());
            facultyService.deleteCourseFromFaculty(course.get(), faculty.get());
            courseService.save(course.get());
            facultyService.save(faculty.get());
        }
    }
    @PostMapping("/deleteStudentFromCourse/{id}")
    public void deleteStudentFromCourse(@RequestBody Set<Long> students, @PathVariable Long id ) throws Exception{

        Optional<Course> course = courseService.findById(id);
        for (Long st: students) {
            Optional<Student> student = studentService.findById(st);
            if (!student.isPresent())
                throw new Exception("Student doesn't exist");
            course.ifPresent(course1 -> studentService.deleteStudentFromCourse(student.get(), course1));
            studentService.save(student.get());
            course.ifPresent(course1 -> courseService.deleteStudentFromCourse(student.get(), course1));
            courseService.save(course.get());
        }
    }
    @PostMapping("/deleteStudentFromFaculty/{id}")
    public void deleteStudentFromFaculty(@RequestBody Set<Long> students, @PathVariable Long id ) throws Exception{

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
    }
    @GetMapping("/studentCourses/{id}")
    public Iterable<Course> showStudentCourses(@PathVariable Long id) throws Exception{

        Optional<Student> student = studentService.findById(id);
        if (!student.isPresent())
            throw new Exception("Student doesn't exist");
        return student.get().getCourses();
    }
    @GetMapping("/facultyCourses/{id}")
    public Iterable<Course> showFacultyCourses(@PathVariable Long id) throws Exception{

        Optional<Faculty> faculty = facultyService.findById(id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        return faculty.get().getCourses();
    }
    @GetMapping("/facultyStudents/{id}")
    public Iterable<Student> showFacultyStudents(@PathVariable Long id) throws Exception{

        Optional<Faculty> faculty = facultyService.findById(id);
        if (!faculty.isPresent())
            throw new Exception("Faculty doesn't exist");
        return faculty.get().getStudents();
    }
    @GetMapping("/studentFaculty/{id}")
    public Faculty showStudentFaculty(@PathVariable Long id) throws Exception{

        Optional<Student> student = studentService.findById(id);
        if (!student.isPresent())
            throw new Exception("Student doesn't exist");
        return student.get().getStudentFaculty();
    }
    @GetMapping("/courseFaculty/{id}")
    public Faculty showCourseFaculty(@PathVariable Long id) throws Exception{

        Optional<Course> course = courseService.findById(id);
        if (!course.isPresent())
            throw new Exception("Course doesn't exist");
        return course.get().getCourseFaculty();
    }
    @GetMapping("/courseStudents/{id}")
    public Set<Student> showCourseStudents(@PathVariable Long id) throws Exception {

        Optional<Course> course = courseService.findById(id);
        if (!course.isPresent())
            throw new Exception("Course doesn't exist");
        return course.get().getStudents();
    }

}
