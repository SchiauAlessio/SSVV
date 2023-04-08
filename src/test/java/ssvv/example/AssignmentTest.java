package ssvv.example;

import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Tema;
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

public class AssignmentTest {
    private Service service;
    private TemaXMLRepo temaRepo;

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/java/ssvv/example/files/testTeme.xml");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<inbox>\n</inbox>");
        fileWriter.close();
        StudentXMLRepo studentRepo = new StudentXMLRepo("src/test/java/ssvv/example/files/testStudenti.xml");
        temaRepo = new TemaXMLRepo("src/test/java/ssvv/example/files/testTeme.xml");
        NotaXMLRepo notaRepo = new NotaXMLRepo("src/test/java/ssvv/example/files/testNote.xml");
        service = new Service(studentRepo, new StudentValidator(), temaRepo, new TemaValidator(), notaRepo, new NotaValidator(studentRepo, temaRepo));
    }

    @Test
    public void addAssignmentShouldAddValidAssignmentToRepository() {
        //path 2
        Tema tema = new Tema("1", "Description", 1, 1);
        Tema result = service.addTema(tema);
        assertNull(result);
        assertEquals(tema, temaRepo.findOne("1"));
    }

    @Test(expected = ValidationException.class)
    public void addAssignmentShouldThrowValidationExceptionForNegativePrimire() {
        //path 3
        Tema tema = new Tema("1", "Description", 1, -1);
        service.addTema(tema);
    }

    @Test
    public void addExistingAssignmentShouldReturnExistingAssignment() {
        //path 1
        Tema tema = new Tema("1", "Description", 1, 1);
        service.addTema(tema);
        Tema result = service.addTema(tema);
        assertEquals(tema, result);
    }

    @Test(expected = ValidationException.class)
    public void addAssignmentShouldThrowValidationExceptionForNullId() {
        //path 6
        Tema tema = new Tema(null, "Description", 1, 1);
        service.addTema(tema);
    }

    @Test(expected = ValidationException.class)
    public void addAssignmentShouldThrowValidationExceptionForEmptyDescription() {
        //path 5
        Tema tema = new Tema("1", "", 1, 1);
        service.addTema(tema);
    }

    @Test(expected = ValidationException.class)
    public void addAssignmentShouldThrowValidationExceptionForDeadline0() {
        //path 4
        Tema tema = new Tema("1", "Description", 0, 1);
        service.addTema(tema);
    }

}
