package ssvv.example;

import org.junit.Before;
import org.junit.Test;
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
        File file = new File("src/test/java/ssvv/example/files/testStudenti.xml");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<inbox>\n</inbox>");
        fileWriter.close();
        studentRepo = new StudentXMLRepo("src/test/java/ssvv/example/files/testStudenti.xml");
        TemaXMLRepo temaRepo = new TemaXMLRepo("src/test/java/ssvv/example/files/testTeme.xml");
        NotaXMLRepo notaRepo = new NotaXMLRepo("src/test/java/ssvv/example/files/testNote.xml");
        service = new Service(studentRepo, new StudentValidator(), temaRepo, new TemaValidator(), notaRepo, new NotaValidator(studentRepo, temaRepo));
    }

    @Test
    public void addStudentShouldAddStudentToRepository() {
        Student student = new Student("1", "John", 932, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryLowGroup() {
        Student student = new Student("1", "John", 0, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryLowGroup1() {
        Student student = new Student("1", "John", 1, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryMaxIntGroup() {
        Student student = new Student("1", "John", Integer.MAX_VALUE, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryMaxIntMinus1Group() {
        Student student = new Student("1", "John", Integer.MAX_VALUE-1, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryShortName() {
        Student student = new Student("1", "J", 0, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test
    public void addStudentShouldAddStudentToRepositoryShortEmail() {
        Student student = new Student("1", "John", 937, "s");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForInvalidStudent() {
        Student student = new Student("", "", -1,"");
        service.addStudent(student);
    }

    @Test
    public void addExistingStudentShouldReturnExistingStudent() {
        Student student = new Student("1", "John", 932, "student@gmail.com");
        Student result = service.addStudent(student);
        assertNull(result);
        assertEquals(student, studentRepo.findOne("1"));
        Student result2 = service.addStudent(student);
        assertEquals(student, result2);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForInvalidId() {
        Student student = new Student("", "John", 932,"student@gmail.com");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForNullId() {
        Student student = new Student(null, "John", 932,"student@gmail.com");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForInvalidName() {
        Student student = new Student("1", "", 932,"student@gmail.com");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForNullName() {
        Student student = new Student("1", null, 932,"student@gmail.com");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForNegativeGroup() {
        Student student = new Student("1", "John", -1,"student@gmail.com");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForInvalidEmail() {
        Student student = new Student("1", "John", 932,"");
        service.addStudent(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudentShouldThrowValidationExceptionForNullEmail() {
        Student student = new Student("1", "John", 932, null);
        service.addStudent(student);
    }
}
