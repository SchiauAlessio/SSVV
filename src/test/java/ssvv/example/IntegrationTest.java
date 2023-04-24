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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Tema tema = new Tema("1", "Description", 12, 12);
        Mockito.when(mockStudentRepo.findOne("1")).thenReturn(student);
        Mockito.when(mockTemaRepo.findOne("1")).thenReturn(tema);

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

        Tema tema = new Tema("1", "Description", 12, 12);
        service.addTema(tema);

        Nota nota = new Nota("1", "1", "1", 10D, LocalDate.now());
        double result3 = service.addNota(nota, "Good job!");

        assertEquals(nota, notaRepo.findOne("1"));
        assertEquals(10.0, result3, 0.01);
    }
}
