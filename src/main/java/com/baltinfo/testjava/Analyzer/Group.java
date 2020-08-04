package com.baltinfo.testjava.Analyzer;

import java.util.*;

public class Group {

    private static int GROUP_COUNTER = 0;
    private int groupId;

    public Set<String> getLines() {
        return lines;
    }

    private Set<String> lines;

    public Group(String lineParts){
        groupId = ++GROUP_COUNTER;
        lines = new TreeSet<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return groupId == group.groupId &&
                Objects.equals(lines, group.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, lines);
    }
}
