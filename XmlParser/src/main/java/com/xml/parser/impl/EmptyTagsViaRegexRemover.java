package com.xml.parser.impl;

import com.xml.parser.EmptyTagsRemover;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmptyTagsViaRegexRemover implements EmptyTagsRemover {

    private final String[] patterns = new String[]{
            "\\s*<\\w+/>",
            "\\s*<\\w+></\\w+>",
            "\\s*<\\w+>\n*\\s*</\\w+>"
    };

    @Override
    public String removeEmptyTags(String xml) {
        for (String pattern : patterns) {
            Matcher matcher = Pattern.compile(pattern).matcher(xml);
            xml = matcher.replaceAll("");
        }
        return xml;
    }
}
