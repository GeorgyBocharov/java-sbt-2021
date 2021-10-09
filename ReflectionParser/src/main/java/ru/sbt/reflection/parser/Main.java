package ru.sbt.reflection.parser;

import ru.sbt.reflection.parser.entities.Person;
import ru.sbt.reflection.parser.factory.GeneratorFactory;
import ru.sbt.reflection.parser.generator.JsonGenerator;


public class Main {
    public static void main(String[] args) {
        Person person = new Person(1L, "John", null, true, null);
        Person father = new Person(2L, "Father", "Williams", true, null);
        person.setParent(father);

        try {
            JsonGenerator<Person> jsonGeneratorPerson = GeneratorFactory.createJsonGenerator(Person.class);
            if (jsonGeneratorPerson == null) {
                return;
            }
            System.out.println(jsonGeneratorPerson.toJson(person));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
