package kuznikos.restapp.restapp.service;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Faculty;
import kuznikos.restapp.restapp.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacultyService {

    List<Faculty> showFaculties();
    void deleteFaculty(Long id);
    void addStudentsToFaculty(Long id, Set<Student> students);
    void save(Faculty faculty);
    Optional<Faculty> findById(Long id);
    void addCoursesToFaculty(Long id, Set<Course> courses);
    List<Faculty> findAll();

    void deleteCourseFromFaculty(Course course, Faculty faculty);

    void deleteStudentFromFaculty(Student student, Faculty faculty);
}
