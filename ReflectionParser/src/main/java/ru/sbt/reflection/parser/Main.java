package ru.sbt.reflection.parser;

import ru.sbt.reflection.parser.entities.Person;
import ru.sbt.reflection.parser.factory.GeneratorFactory;
import ru.sbt.reflection.parser.generator.JsonGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Person father = new Person(2L, "Father", "Williams", true, null, Set.of("emailFirst", "emailSecond"));
        Person mother = new Person(2L, "Mother", "Williams", false, null, Set.of("momsSpaghetti"));
        Person person = new Person(1L, "John", null, true, List.of(father, mother), Collections.emptySet());

        try {
            JsonGenerator<Person> jsonGeneratorPerson = GeneratorFactory.createJsonGenerator(person);
            if (jsonGeneratorPerson == null) {
                return;
            }

            System.out.println(jsonGeneratorPerson.toJson(person));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
