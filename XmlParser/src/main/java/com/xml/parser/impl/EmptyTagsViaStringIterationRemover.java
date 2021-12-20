package com.xml.parser.impl;

import com.xml.parser.EmptyTagsRemover;

import java.util.HashMap;
import java.util.Map;

public class EmptyTagsViaStringIterationRemover implements EmptyTagsRemover {

    @Override
    public String removeEmptyTags(String xml) {
        int len = xml.length();
        Map<Integer, Integer> skipMap = new HashMap<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            int i = 0;
            while (true) {
                int start = i = skipEmptyTags(skipMap, i);
                i = skipTillTagEnd(xml, i);
                if (i >= len - 1) {
                    break;
                }
                if (xml.charAt(start + 1) == '/') {
                    i = skipTillTagStart(xml, i);
                    if (i >= len - 1) {
                        break;
                    }
                    continue;
                }
                if (xml.charAt(i - 1) == '/') {
                    i = skipSpaces(xml, i + 1);
                    if (i >= len - 1) {
                        break;
                    }
                    skipMap.put(start, i);
                    changed = true;
                    continue;
                }
                i = skipSpaces(xml, i + 1);
                if (i >= len - 1) {
                    break;
                }
                boolean emptyTag = true;
                if (xml.charAt(i) != '<') {
                    emptyTag = false;
                    i = skipTillTagStart(xml, i);
                    if (i >= len - 1) {
                        break;
                    }
                } else if (xml.charAt(i) == '<') {
                    i = skipEmptyTags(skipMap, i);
                }
                if (xml.charAt(i) == '<' && xml.charAt(i + 1) == '/') {
                    i = skipTillTagEnd(xml, i);
                    if (i >= len - 1) {
                        break;
                    }
                    if (emptyTag) {
                        i = skipTillTagStart(xml, i);
                        if (i >= len - 1) {
                            break;
                        }
                        skipMap.put(start, i);
                        changed = true;
                    }
                }
                i = skipTillTagStart(xml, i);
                if (i >= len - 1) {
                    break;
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            i = skipEmptyTags(skipMap, i);
            builder.append(xml.charAt(i));
        }
        return builder.toString();
    }

    private static int skipEmptyTags(Map<Integer, Integer> skipMap, int i) {
        Integer midEnd = skipMap.get(i);
        while (midEnd != null) {
            i = midEnd;
            midEnd = skipMap.get(midEnd);
        }
        return i;
    }

    private static int skipSpaces(String xml, int i) {
        while (xml.charAt(i) == ' ' || xml.charAt(i) == '\n' || xml.charAt(i) == '\t') {
            i++;
        }
        return i;
    }

    private static int skipTillTagEnd(String xml, int i) {
        while (xml.charAt(i) != '>') {
            i++;
        }
        return i;
    }

    private static int skipTillTagStart(String xml, int i) {
        while (xml.charAt(i) != '<') {
            i++;
            if (i >= xml.length() - 1) {
                return xml.length() - 1;
            }
        }
        return i;
    }
}
