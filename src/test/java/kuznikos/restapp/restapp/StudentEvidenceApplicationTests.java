package kuznikos.restapp.restapp;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Faculty;
import kuznikos.restapp.restapp.model.Student;
import kuznikos.restapp.restapp.service.CourseService;
import kuznikos.restapp.restapp.service.FacultyService;
import kuznikos.restapp.restapp.service.StudentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentEvidenceApplicationTests {


    private final StudentService studentService;
    private final CourseService courseService;
    private final FacultyService facultyService;

    @Autowired
    public StudentEvidenceApplicationTests(StudentService studentService, CourseService courseService, FacultyService facultyService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.facultyService = facultyService;
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addEntitiesToDatabase() {

        Student student = new Student(1L, "Kostya", "Kuznietsov", 21, "980618", "asdasdas", null, null);
        studentService.save(student);
        Assert.assertEquals(1, studentService.findAll().size());
        Course course = new Course(2L, "Programovani PA1", "PA1", null,null );
        courseService.save(course);
        Assert.assertEquals(1, courseService.findAll().size());
        Faculty faculty = new Faculty(3L, "Fac Inf Tech", "FIT", null, null);
        facultyService.save(faculty);
        Assert.assertEquals(1, facultyService.findAll().size());
    }
    @Test
    public void testRelationStudentFaculty() throws Exception{

        addEntitiesToDatabase();
        mockMvc.perform(post("/rest/students-faculty/{id}",3).contentType(MediaType.APPLICATION_JSON).content("[1]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/studentFaculty/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.facultyAbbr", is("FIT")));
        mockMvc.perform(get("/rest/facultyStudents/{id}",3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Kostya")));
    }
    @Test
    public void testRelationStudentCourse() throws Exception{

        addEntitiesToDatabase();
        mockMvc.perform(post("/rest/students-course/{id}",2).contentType(MediaType.APPLICATION_JSON).content("[1]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/studentCourses/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseAbbr", is("PA1")));
        mockMvc.perform(get("/rest/courseStudents/{id}",2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Kostya")));
    }
    @Test
    public void testRelationCourseFaculty() throws Exception {

        addEntitiesToDatabase();
        mockMvc.perform(post("/rest/courses-faculty/{id}",3).contentType(MediaType.APPLICATION_JSON).content("[2]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/facultyCourses/{id}",3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseAbbr", is("PA1")));
        mockMvc.perform(get("/rest/courseFaculty/{id}",2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.facultyAbbr", is("FIT")));
    }
    @Test
    public void testDelete() throws Exception {

        addEntitiesToDatabase();
        mockMvc.perform(delete("/rest/faculty/{id}",3).contentType(MediaType.APPLICATION_JSON).content("[2]"))
                .andExpect(status().isOk());
        Assert.assertEquals(0, facultyService.findAll().size());
        mockMvc.perform(delete("/rest/course/{id}",2).contentType(MediaType.APPLICATION_JSON).content("[2]"))
                .andExpect(status().isOk());
        Assert.assertEquals(0, courseService.findAll().size());
        mockMvc.perform(delete("/rest/student/{id}",1).contentType(MediaType.APPLICATION_JSON).content("[2]"))
                .andExpect(status().isOk());
        Assert.assertEquals(0, studentService.findAll().size());
    }
    @Test
    public void testFull() throws Exception {

        addEntitiesToDatabase();
        Student student = new Student(4L, "Petr", "Kolac", 22, "12345", "asdnfsdf", null, null);
        studentService.save(student);
        mockMvc.perform(get("/rest/students/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        Course course = new Course(5L, "Ekonomicke principy", "EMP", null,null );
        courseService.save(course);
        mockMvc.perform(get("/rest/courses/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        Faculty faculty = new Faculty(6L, "Fac Electrotech", "FEL", null, null);
        facultyService.save(faculty);
        mockMvc.perform(get("/rest/faculties/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mockMvc.perform(post("/rest/courses-faculty/{id}",3).contentType(MediaType.APPLICATION_JSON).content("[2, 5]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/facultyCourses/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseAbbr", is("EMP")))
                .andExpect(jsonPath("$[1].courseAbbr", is("PA1")));
        mockMvc.perform(get("/rest/courseFaculty/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyAbbr", is("FIT")));
        mockMvc.perform(get("/rest/courseFaculty/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyAbbr", is("FIT")));
        mockMvc.perform(post("/rest/courses-faculty/{id}", 6).contentType(MediaType.APPLICATION_JSON).content("[2]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/courseFaculty/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyAbbr", is("FEL")));
        mockMvc.perform(delete("/rest/faculty/{id}",6))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/faculties/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(delete("/rest/course/{id}",5))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/courses/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(delete("/rest/student/{id}",1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rest/students/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
