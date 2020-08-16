package kuznikos.restapp.restapp.service;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Student;
import kuznikos.restapp.restapp.repos.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@NoArgsConstructor
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> showStudents(){
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudent(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> findAllBySurname(String surname) {
        return studentRepository.findAllBySurname(surname);
    }

    @Override
    public List<Student> findAllByCourses(Long id) {
        return studentRepository.findStudentByCourses(id);
    }

    @Override
    public void deleteAllFromCourse(Course course, Set<Student> students) {

        for (Student st: students){
            st.deleteCourse(course);
            save(st);
        }
    }

    @Override
    public Set<Course> findStudentCourses(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent())
            return student.get().getCourses();
        return new HashSet<>();
    }

    @Override
    public void deleteStudentFromCourse(Student st, Course course) {
        st.getCourses().remove(course);
    }

    @Override
    public void deleteStudentFromFaculty(Student student) {
        studentRepository.findById(student.getId()).get().setStudentFaculty(null);
    }


}
