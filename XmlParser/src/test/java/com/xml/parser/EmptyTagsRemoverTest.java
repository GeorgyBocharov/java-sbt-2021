package com.xml.parser;

import com.util.ResourceFileDataLoader;

import com.xml.parser.impl.EmptyTagsViaRecursiveRemover;
import com.xml.parser.impl.EmptyTagsViaRegexRemover;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.stream.Stream;

public class EmptyTagsRemoverTest {

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedFiles")
    public void forRegexRemoverReturnXmlWithoutEmptyTagsIfSuccess(
            String fileName,
            String expectedFileName
    ) throws IOException {
        checkEmptyTagsRemoval(fileName, expectedFileName, new EmptyTagsViaRegexRemover());
    }

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedFiles")
    public void forRecursiveRemoverReturnXmlWithoutEmptyTagsIfSuccess(
            String fileName,
            String expectedFileName
    ) throws IOException {
        checkEmptyTagsRemoval(fileName, expectedFileName, new EmptyTagsViaRecursiveRemover());
    }

    @ParameterizedTest
    @ValueSource(strings = "xml/data/empty.xml")
    public void forRegexRemoverReturnEmptyStringIfEmptyTagsProvided(String fileName) throws IOException {
        checkEmptyResult(fileName, new EmptyTagsViaRegexRemover());
    }

    @ParameterizedTest
    @ValueSource(strings = "xml/data/empty.xml")
    public void forRecursiveRemoverReturnEmptyStringIfEmptyTagsProvided(String fileName) throws IOException {
        checkEmptyResult(fileName, new EmptyTagsViaRecursiveRemover());
    }

    private void checkEmptyResult(String fileName, EmptyTagsRemover emptyTagsRemover) throws IOException {
        String xmlString = ResourceFileDataLoader.loadFileFromTestResources(fileName);

        String actualString = emptyTagsRemover.removeEmptyTags(xmlString);

        Assertions.assertNotNull(actualString);
        Assertions.assertTrue(actualString.isEmpty());
    }

    private void checkEmptyTagsRemoval(String fileName, String expectedFileName, EmptyTagsRemover emptyTagsRemover) throws IOException {
        String xmlString = ResourceFileDataLoader.loadFileFromTestResources(fileName);
        String expectedXmlString = ResourceFileDataLoader.loadFileFromTestResources(expectedFileName);

        String actualString = emptyTagsRemover.removeEmptyTags(xmlString);

        Assertions.assertNotNull(actualString);
        Assertions.assertNotNull(expectedXmlString);
        Assertions.assertEquals(replaceWhitespaces(expectedXmlString), replaceWhitespaces(actualString));
    }

    private static String replaceWhitespaces(String xmlString) {
        return xmlString.replaceAll(">[\\s\r\n]*<", "><");
    }

    private static Stream<Arguments> provideInputAndExpectedFiles() {
        return Stream.of(
                Arguments.of("xml/data/first.xml", "xml/expected/first.xml"),
                Arguments.of("xml/data/second.xml", "xml/expected/second.xml")
        );
    }

}
