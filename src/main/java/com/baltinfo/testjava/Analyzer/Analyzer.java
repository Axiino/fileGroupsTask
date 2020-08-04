package com.baltinfo.testjava.Analyzer;

import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {

    private List<Map<String, Group>> columnWordsToGroups; //3 columns for every unique non-\"\" value mapping to group
    private Map<Integer, Group> finalGroups; //finalGroups to write in file
    private Map<Integer, Group> subGroups;// to resolve merged groups
    
    private static int LINE_LENGTH = 3;

    public Analyzer(){
        columnWordsToGroups = new ArrayList<>();
        for(int i = 0; i < LINE_LENGTH; i++){
            columnWordsToGroups.add(new HashMap<>());
        }
        finalGroups = new HashMap<>();
        subGroups = new HashMap<>();
    }

    /**
     * Adding new line in group or create new group with this line
     * @param line
     * @return true, if adding was successful; false, if the line it not of desirable format
     */
    public boolean addLine(String line){
        String[] lineParts = line.split(";" , -1); //";" -- for 1st csv
        if((lineParts.length != LINE_LENGTH)  || isEmptyLinePartsInBigFile(lineParts) || isEmptyLineParts(lineParts)) //|| isEmptyLineParts(lineParts) -- for 1st csv
            return false;

        List<Group> groupsToMerge = new ArrayList<>();
        String[] newWords = new String[3];
        for(int i = 0; i < LINE_LENGTH; i++){
            if(!lineParts[i].isEmpty() && !lineParts[i].equals("\"\"")){ //!lineParts[i].equals("\"\"") -- for 1st csv
                if(columnWordsToGroups.get(i).containsKey(lineParts[i])){// To get group by a column word
                    Group curGroup = getExactGroup(columnWordsToGroups.get(i).get(lineParts[i])); //get group from subgroup
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
                    subGroups.put(group.getGroupId(), finalGroup);
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
     * Get group, if there are any subgroups
     * @param group current group
     * @return finalGroup
     */
    private Group getExactGroup(Group group){
        while(subGroups.containsKey(group.getGroupId())){
            group = subGroups.get(group.getGroupId());
        }
        return group;
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
    private boolean isEmptyLineParts(String[] lineParts){
        return lineParts[0].equals("\"\"")
                && lineParts[1].equals("\"\"")
                && lineParts[2].equals("\"\"");
    }

    /**
     *
     * @return sorted groups by their size where first >= last
     */
    public List<Group> sortedGroups(){
        Comparator<Group> c = Comparator.comparing(s -> -s.size());
        c = c.thenComparing((o1, o2) -> o1.toString().compareTo(o2.toString()));
        return finalGroups.values().stream()
                .sorted(c) //(g1, g2) -> Integer.compare(g2.size(), g1.size())
                .collect(Collectors.toList());
    }

    /**
     * @return true if line equals to ;;
     */
    private boolean isEmptyLinePartsInBigFile(String[] lineParts){
        return lineParts[0].equals("")
                && lineParts[1].equals("")
                && lineParts[2].equals("");
    }

}
