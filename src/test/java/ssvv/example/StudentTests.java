package ssvv.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import ssvv.example.domain.Student;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StudentTests {
    private Service service;
    private StudentXMLRepo studentRepo;

    @Before
    public void setUp() throws Exception {
        File file = new File("src\\test\\java\\ssvv\\example\\files\\testStudenti.xml");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <inbox>
                </inbox>""");
        fileWriter.close();
        studentRepo = new StudentXMLRepo("src\\test\\java\\ssvv\\example\\files\\testStudenti.xml");
        TemaXMLRepo temaRepo = new TemaXMLRepo("src\\test\\java\\ssvv\\example\\files\\testTeme.xml");
        NotaXMLRepo notaRepo = new NotaXMLRepo("src\\test\\java\\ssvv\\example\\files\\testNote.xml");
        service = new Service(studentRepo, new StudentValidator(), temaRepo, new TemaValidator(), notaRepo, new NotaValidator(studentRepo, temaRepo));
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before all");
    }

    @Test
    public void addStudentShouldAddStudentToRepository() {
        Student student = new Student("1", "John", 932, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForInvalidStudent() {
        Student student = new Student("", "John", 932,"");
        service.addStudent(student);
    }
}
