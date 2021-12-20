package com.xml.parser.impl;

import com.xml.parser.EmptyTagsRemover;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmptyTagsViaRegexRemover implements EmptyTagsRemover {

    private final Pattern emptySimpleTag = Pattern.compile("\\s*<\\w+/>");
    private final Pattern emptyComplexTag = Pattern.compile("<\\w+>\\s*</\\w+>");

    @Override
    public String removeEmptyTags(String xml) {
        int lengthBefore;
        xml = replaceMatchingToPattern(xml, emptySimpleTag);
        do {
            lengthBefore = xml.length();
            xml = replaceMatchingToPattern(xml, emptyComplexTag);
        } while (xml.length() != lengthBefore);
        return xml;
    }

    private String replaceMatchingToPattern(String xml, Pattern pattern) {
        Matcher matcher = pattern.matcher(xml);
        xml = matcher.replaceAll("");
        return xml;
    }
}
