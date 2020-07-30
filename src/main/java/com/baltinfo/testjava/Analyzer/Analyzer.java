package com.baltinfo.testjava.Analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Analyzer {

    private List<Map<String, Group>> columnWordsToGroups; //3 columns for every unique non-\"\" value mapping to group
    private Map<Integer, Group> finalGroups; //finalGroups to write in file
    
    private static int LINE_LENGTH = 3;

    public Analyzer(){
        columnWordsToGroups = new ArrayList<>();
        for(int i = 0; i < LINE_LENGTH; i++){
            columnWordsToGroups.add(new HashMap<>());
        }
        finalGroups = new HashMap<>();
    }

    /**
     * Adding new line in group ot create new group with this line
     * @param line
     * @return true, if adding was successful; false, if the line it not of desirable format
     */
    public boolean addLine(String line){
        String[] lineParts = line.split(";");
        if((lineParts.length  != LINE_LENGTH) || isEmptyLineParts(lineParts))
            return false;

        List<Group> groupsToMerge = new ArrayList<>();
        String[] newWords = new String[3];
        for(int i = 0; i < LINE_LENGTH; i++){
            if(!lineParts[i].equals("\"\"")){
                if(columnWordsToGroups.get(i).containsKey(lineParts[i])){// To get group by a column word
                    Group curGroup = columnWordsToGroups.get(i).get(lineParts[i]);
                    if (finalGroups.containsKey(curGroup.getGroupId())) //If group wasn't delete as a subgroup
                        groupsToMerge.add(curGroup); //Groups, where coincidences were found
                }
                else{
                    newWords[i] = lineParts[i]; //Save the word in column 'i' in future method mergeOrCreateGroup
                }
            }
        }
        mergeOrCreateGroup(line, groupsToMerge, newWords);
        return true;
    }

    /**
     * Merging subgroups to a group, or create new group
     * @param line line to add
     * @param groupsToMerge groups, where words of columns were found
     * @param newWords new words to add in columns
     */
    private void mergeOrCreateGroup(String line, List<Group> groupsToMerge, String[] newWords){
        Group finalGroup;
        if(!groupsToMerge.isEmpty()){
            finalGroup = groupsToMerge.get(0); // to write in 1-st final group
            for (Group group: groupsToMerge) {
                if(group != finalGroup){ //If group to merge exists
                    finalGroup.addAll(group); //Merge groups if there more than 1 coincidences
                    finalGroups.remove(group.getGroupId()); //Remove subgroup
                }
            }
            finalGroup.addLine(line); //Adding new line to finalGroup
            finalGroups.put(finalGroup.getGroupId(), finalGroup);
        }
        else{
            finalGroup = new Group(line);
            finalGroups.put(finalGroup.getGroupId(), finalGroup);
        }
        for(int j = 0; j < LINE_LENGTH; j++){
            if (newWords[j] != null)
                columnWordsToGroups.get(j).put(newWords[j], finalGroup);
        }
    }

    /**
     *
     * @return count of groups with size > 1
     */
    public long getMultiGroups(){
        return finalGroups.values().stream()
                .filter(g -> g.size() > 1)
                .count();
    }

    public int getGroups(){
        return finalGroups.values().size();
    }

    /**
     *
     * @return true if line equals to "";"";""
     */
    public boolean isEmptyLineParts(String[] lineParts){
        return lineParts[0].equals("\"\"")
                && lineParts[1].equals("\"\"")
                && lineParts[2].equals("\"\"");
    }


    /**
     *
     * @return sorted groups by their size where first >= last
     */
    public List<Group> sortedGroups(){
        return finalGroups.values().stream()
                .sorted((g1, g2) -> Integer.compare(g2.size(), g1.size()))
                .collect(Collectors.toList());
    }
}
