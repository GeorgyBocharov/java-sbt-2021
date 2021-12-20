package com.xml.parser.impl;

import com.xml.parser.EmptyTagsRemover;

public class EmptyTagsViaRecursiveRemover implements EmptyTagsRemover {

    @Override
    public String removeEmptyTags(String xml) {
        return checkTag(xml, 0).stringBuilder.toString();
    }

    private Tag checkTag(String xml, int i) {
        int start = i;
        i = xml.indexOf('>', start);
        if (xml.charAt(i - 1) == '/') {
            return new Tag(xml.indexOf('<', i));
        }
        StringBuilder builder = new StringBuilder(xml.substring(start, i + 1));
        String nodeName = xml.substring(start + 1, i);
        int valueEnd = xml.indexOf('<', i + 1);
        boolean empty = writeValue(i + 1, valueEnd, xml, builder);
        i = valueEnd;
        while (!isClosingTag(xml, i, nodeName)) {
            Tag innerNode = checkTag(xml, i);
            if (!innerNode.empty) {
                builder.append(innerNode.stringBuilder);
            }
            empty = empty && innerNode.empty;
            i = xml.indexOf('<', innerNode.end);
        }

        int end = xml.indexOf('>', i + 2) + 1;
        builder.append(xml, i, end);
        return new Tag(end, empty, builder);
    }

    private boolean isClosingTag(String xml, int i, String nodeName) {
        return xml.charAt(i + 1) == '/' && xml.substring(i + 2, xml.indexOf('>', i + 2)).equals(nodeName);
    }

    private boolean writeValue(int start, int end, String xml, StringBuilder builder) {
        String value = xml.substring(start, end).trim();
        if (value.isEmpty()) {
            return true;
        }
        builder.append(value);
        return false;
    }

    private static class Tag {
        private final int end;
        private final boolean empty;
        private final StringBuilder stringBuilder;

        public Tag(int end) {
            this.end = end;
            this.empty = true;
            this.stringBuilder = new StringBuilder();
        }

        public Tag(int end, boolean empty, StringBuilder stringBuilder) {
            this.end = end;
            this.empty = empty;
            if (!empty) {
                this.stringBuilder = stringBuilder;
            } else {
                this.stringBuilder = new StringBuilder();
            }
        }
    }
}
