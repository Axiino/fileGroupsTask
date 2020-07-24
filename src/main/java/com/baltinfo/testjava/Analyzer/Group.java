package com.baltinfo.testjava.Analyzer;

import java.util.*;

public class Group {

    private static int GROUP_COUNTER = 0;
    private int groupId;

    public List<String> getLines() {
        return lines;
    }

    private List<String> lines;

    public Group(String lineParts){
        groupId = ++GROUP_COUNTER;
        lines = new ArrayList<>();
        lines.add(lineParts);
    }

    public int getGroupId() {
        return groupId;
    }

    /**
     * Adding line to group
     * @param lineParts
     */
    public void addLine(String lineParts){
        lines.add(lineParts);
    }

    public void addAll(Group group){
        lines.addAll(group.getLines());
    }

    public int size(){
        return lines.size();
    }

    @Override
    public String toString(){
         return String.join("\n", lines);
    }
}
