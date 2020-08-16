package kuznikos.restapp.restapp.service;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentService {

    List<Student> showStudents();
    Optional<Student> getStudent(Long id);
    void deleteStudent(Long id);
    void save(Student student);
    List<Student> findAll();
    Optional<Student> findById(Long id);
    List<Student> findAllBySurname(String surname);
    List<Student> findAllByCourses(Long id);
    void deleteAllFromCourse(Course course, Set<Student> students);
    Set<Course> findStudentCourses(Long id);
    void deleteStudentFromCourse(Student st, Course course);

    void deleteStudentFromFaculty(Student student);
}
