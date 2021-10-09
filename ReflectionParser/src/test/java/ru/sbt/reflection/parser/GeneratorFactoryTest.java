package ru.sbt.reflection.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sbt.reflection.parser.entities.Person;
import ru.sbt.reflection.parser.factory.GeneratorFactory;
import ru.sbt.reflection.parser.generator.JsonGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneratorFactoryTest {


    private static final String PERSON_JSON = "resources/json/person.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateJsonGenerator() throws IOException {
        Person expectedPerson = mapper.readValue(loadFile(PERSON_JSON), Person.class);
        JsonGenerator<Person> personJsonGenerator = GeneratorFactory.createJsonGenerator(Person.class);
        Assertions.assertNotNull(personJsonGenerator);
        String personJson = personJsonGenerator.toJson(expectedPerson);
        Person actualPerson = mapper.readValue(personJson, Person.class);
        Assertions.assertEquals(expectedPerson, actualPerson);
    }

    String loadFile(String path) throws IOException {
        Path resourceDirectory = Paths.get("src","test", path);
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return Files.readString(Path.of(absolutePath));
    }

}
