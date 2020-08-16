package kuznikos.restapp.restapp.service;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Faculty;
import kuznikos.restapp.restapp.model.Student;
import kuznikos.restapp.restapp.repos.FacultyRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@NoArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    @Autowired
    FacultyRepository facultyRepository;

    @Override
    public List<Faculty> showFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public void addStudentsToFaculty(Long id, Set<Student> students) {
        for (Student st: students) {
            facultyRepository.findById(id).get().addStudent(st);
        }
    }

    @Override
    public void save(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public void addCoursesToFaculty(Long id, Set<Course> courses) {
        Faculty faculty = facultyRepository.findById(id).get();
        for (Course c: courses)
            faculty.addCourse(c);
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteCourseFromFaculty(Course course, Faculty faculty) {
        faculty.getCourses().remove(course);
    }

    @Override
    public void deleteStudentFromFaculty(Student student, Faculty faculty) {
        faculty.getStudents().remove(student);
        save(faculty);
    }
}
