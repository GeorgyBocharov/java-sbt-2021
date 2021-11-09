package ru.sbt.reflection.parser.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class Person {
    private Long id;
    private String name;
    private String lastName;
    private boolean male;
    private List<Person> parents;
    private Set<String> emails;
}

