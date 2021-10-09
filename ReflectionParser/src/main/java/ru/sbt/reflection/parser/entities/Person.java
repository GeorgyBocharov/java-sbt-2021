package ru.sbt.reflection.parser.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class Person {
    private Long id;
    private String name;
    private String lastName;
    @JsonProperty("isMale")
    private boolean isMale;
    private Person parent;
}

