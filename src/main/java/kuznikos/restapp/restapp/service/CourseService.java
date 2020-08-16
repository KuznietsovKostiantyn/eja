package kuznikos.restapp.restapp.service;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Student;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> showCourses();
    void deleteCourse(Long id);
    void save(Course course);
    Optional<Course> findById(Long id);
    void addStudentToCourse(Long id, Student student);
    List<Course> findAll();

    void deleteStudentFromCourse(Student st, Course course);

    void deleteCourseFromFaculty(Course course);
/*    List<Student> findAllBySurname(String surname);
    List<Student> findAllByCourses(Long id);
    void deleteAllFromCourse(Long id);
    Set<Course> findStudentCourses(Long id);*/
}
