package ru.sbt.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class JsonValue {
    private Object result;
    private int endIndex;
    private boolean lastInArray = false;
}
