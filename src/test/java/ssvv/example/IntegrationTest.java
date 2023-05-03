package ssvv.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IntegrationTest {
    private Service service;
    private TemaXMLRepo temaRepo;

    private StudentXMLRepo studentRepo;

    private NotaXMLRepo notaRepo;

    @Before
    public void setUp() throws Exception {
        File temeFile = new File("src/test/java/ssvv/example/files/testTeme.xml");
        FileWriter fileWriter = new FileWriter(temeFile);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<inbox>\n</inbox>");
        fileWriter.close();

        File studentsFile = new File("src/test/java/ssvv/example/files/testStudenti.xml");
        fileWriter = new FileWriter(studentsFile);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<inbox>\n</inbox>");
        fileWriter.close();

        File noteFile = new File("src/test/java/ssvv/example/files/testNote.xml");
        fileWriter = new FileWriter(noteFile);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<inbox>\n</inbox>");
        fileWriter.close();

        studentRepo = new StudentXMLRepo("src/test/java/ssvv/example/files/testStudenti.xml");
        temaRepo = new TemaXMLRepo("src/test/java/ssvv/example/files/testTeme.xml");
        notaRepo = new NotaXMLRepo("src/test/java/ssvv/example/files/testNote.xml");
        service = new Service(studentRepo, new StudentValidator(), temaRepo, new TemaValidator(), notaRepo, new NotaValidator(studentRepo, temaRepo));
    }

    private Service createMockService() {
        Student student = new Student("1", "John", 932, "em@em.em");
        Tema tema = new Tema("1", "Description", 13, 13);

        StudentXMLRepo mockStudentRepo = Mockito.mock(StudentXMLRepo.class);
        TemaXMLRepo mockTemaRepo = Mockito.mock(TemaXMLRepo.class);
        
        when(mockStudentRepo.save(any(Student.class))).thenReturn(null);
        when(mockStudentRepo.findOne("1")).thenReturn(student);

        when(mockTemaRepo.save(any(Tema.class))).thenReturn(null);
        when(mockTemaRepo.findOne("1")).thenReturn(tema);

        // Create Service with mock repos

        return new Service(mockStudentRepo, new StudentValidator(), mockTemaRepo, new TemaValidator(), notaRepo, new NotaValidator(mockStudentRepo, mockTemaRepo));
    }

    @Test
    public void addValidStudentShouldAddStudentToRepository() {
        Student student = new Student("1", "John", 932, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addValidAssignmentShouldAddAssignmentToRepository() {
        Tema tema = new Tema("1", "Description", 1, 1);
        Tema result = service.addTema(tema);
        assertNull(result);
        assertEquals(tema, temaRepo.findOne("1"));
    }

    @Test
    public void addValidGradeShouldAddGradeToRepository() {
        // Create mock objects
        StudentXMLRepo mockStudentRepo = Mockito.mock(StudentXMLRepo.class);
        TemaXMLRepo mockTemaRepo = Mockito.mock(TemaXMLRepo.class);

        // Set up mock behavior
        Student student = new Student("1", "John", 932, "student@gmail.com");
        Tema tema = new Tema("1", "Description", 13, 13);
        when(mockStudentRepo.findOne("1")).thenReturn(student);
        when(mockTemaRepo.findOne("1")).thenReturn(tema);

        // Create Service with mock repos
        Service mockService = new Service(mockStudentRepo, new StudentValidator(), mockTemaRepo, new TemaValidator(), notaRepo, new NotaValidator(mockStudentRepo, mockTemaRepo));

        // Now add the nota
        Nota nota = new Nota("1", "1", "1", 10D, LocalDate.now());
        double result = mockService.addNota(nota, "Good job!");
        assertEquals(10.0, result, 0.01);
        assertEquals(nota, notaRepo.findOne("1"));
    }

    @Test
    public void addValidTema_Student_GradeShouldAddAllToRepository() {
        Student student = new Student("1", "John", 932, "student@gmail.com");
        service.addStudent(student);

        Tema tema = new Tema("1", "Description", 13, 13);
        service.addTema(tema);

        Nota nota = new Nota("1", "1", "1", 10D, LocalDate.now());
        double result3 = service.addNota(nota, "Good job!");

        assertEquals(nota, notaRepo.findOne("1"));
        assertEquals(10.0, result3, 0.01);
    }

    @Test
    public void testAddStudentValid() {
        Student student = new Student("1", "John", 932, "student@gmail.com");

        Service mockService = createMockService();

        Student addedStudent = mockService.addStudent(student);

        assertNull(addedStudent);
    }

    @Test
    public void testAddStudent_AddAssignment() {
        Service service = createMockService();
        Student student = new Student("1", "John Doe", 937, "st@gmail.com");
        service.addStudent(student);

        Tema tema = new Tema("1", "Assignment description", 4, 2);
        Tema addedTema = service.addTema(tema);

        assertNull(addedTema);
    }

    @Test
    public void testAddStudent_AddAssignment_AddGrade() {
        Service service = createMockService();

        Student student = new Student("1", "John Doe", 937, "email@gmail");
        service.addStudent(student);

        Tema tema = new Tema("1", "Assignment description", 14, 14);
        service.addTema(tema);

        Nota nota = new Nota("1", "1", "1", 10D, LocalDate.now());
        double result = service.addNota(nota, "Good job!");
        assertEquals(10.0, result, 0.01);
    }
}
