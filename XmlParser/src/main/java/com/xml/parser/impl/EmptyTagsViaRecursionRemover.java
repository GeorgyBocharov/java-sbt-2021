package com.xml.parser.impl;

import com.xml.parser.EmptyTagsRemover;

public class EmptyTagsViaRecursionRemover implements EmptyTagsRemover {

    @Override
    public String removeEmptyTags(String xml) {
        return checkTag(xml, 0).stringBuilder.toString();
    }

    private Pair checkTag(String xml, int i) {
        int start = i;
        i = xml.indexOf('>', start);
        if (xml.charAt(i - 1) == '/') {
            return new Pair(xml.indexOf('<', i), true);
        }
        StringBuilder builder = new StringBuilder(xml.substring(start, i + 1));
        String nodeName = xml.substring(start + 1, i);
        i = skipSpaces(xml, i + 1);
        boolean empty = true;
        if (xml.charAt(i) != '<') {
            empty = false;
            int valueStart = i;
            i = xml.indexOf('<', i);
            builder.append(xml.substring(valueStart, i).trim());
        }
        while (!(xml.charAt(i + 1) == '/' && xml.substring(i + 2, xml.indexOf('>', i + 2)).equals(nodeName))) {
            Pair innerNode = checkTag(xml, i);
            if (!innerNode.empty) {
                builder.append(innerNode.stringBuilder);
            }
            empty = empty && innerNode.empty;
            i = xml.indexOf('<', innerNode.end);
        }

        int end = xml.indexOf('>', i + 2) + 1;
        builder.append(xml, i, end);
        return new Pair(end, empty, builder);
    }

    private int skipSpaces(String xml, int i) {
        while (xml.charAt(i) == ' ' || xml.charAt(i) == '\n' || xml.charAt(i) == '\t') {
            i++;
        }
        return i;
    }

    private static class Pair {
        int end;
        boolean empty;
        StringBuilder stringBuilder;

        public Pair(int end, boolean empty) {
            this.end = end;
            this.empty = empty;
        }

        public Pair(int end, boolean empty, StringBuilder stringBuilder) {
            this.end = end;
            this.empty = empty;
            if (!empty) {
                this.stringBuilder = stringBuilder;
            }
        }
    }
}
