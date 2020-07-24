package com.baltinfo.testjava;

import com.baltinfo.testjava.Analyzer.Analyzer;
import com.baltinfo.testjava.Analyzer.Group;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;


public class AnalyzerTest {

    private Analyzer analyzer;
    private String[] strings;

    @Before
    public void preTest(){
        analyzer = new Analyzer();
    }


    @Test
    public void doTestMerge2Groups(){
        String testMerge2Groups = "\"a\";\"d\";\"e\"\n" +
                "\"a\";\"g\";\"s\"\n" +
                "\"e\";\"b\";\"k\"\n" +
                "\"r\";\"b\";\"z\"\n" +
                "\"a\";\"b\";\"x\"";

        String answer = "Группа 1:\n" +
                "\"a\";\"d\";\"e\"\n" +
                "\"a\";\"g\";\"s\"\n" +
                "\"e\";\"b\";\"k\"\n" +
                "\"r\";\"b\";\"z\"\n" +
                "\"a\";\"b\";\"x\"";
        strings = testMerge2Groups.split("\n");
        for(String line : strings){
            analyzer.addLine(line);
        }
        List<Group> myList = analyzer.sortedGroups();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < myList.size(); i++){
            builder.append("Группа " + (i + 1) + ":\n" + myList.get(i));
        }
        Assert.assertEquals(answer, builder.toString());

    }

    @Test
    public void doTestWithWrongLine(){
        String testWrongLine = "\"a\";\"d\";\"e\"\n" +
                "\"a\";\"g\";\"s\"\n" +
                "\"e\";\"b\";\"k\"\n" +
                "\"r\";\"b\";\"z\"\n" +
                "\"a\";\"b\";\"x\"\n" +
                "\"\";\"\";\"\"\n" +
                "\"\"l;";

        String answer = "Группа 1:\n" +
                "\"a\";\"d\";\"e\"\n" +
                "\"a\";\"g\";\"s\"\n" +
                "\"e\";\"b\";\"k\"\n" +
                "\"r\";\"b\";\"z\"\n" +
                "\"a\";\"b\";\"x\"\n" +
                "Группа 2:\n" +
                "\"\";\"\";\"\"";
        strings = testWrongLine.split("\n");
        for(String line : strings){
            analyzer.addLine(line);
        }
        List<Group> myList = analyzer.sortedGroups();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < myList.size(); i++){
            builder.append("Группа " + (i + 1) + ":\n" + myList.get(i));
            if(i + 1 != myList.size())
                builder.append("\n");
        }
        Assert.assertEquals(answer, builder.toString());
    }


    @Test
    public void doTest3Groups(){
        String test3Groups = "\"a\";\"d\";\"e\"\n" +
                "\"c\";\"g\";\"f\"\n" +
                "\"x\";\"a\";\"s\"";

        String answer = "Группа 1:\n" +
                "\"a\";\"d\";\"e\"\n" +
                "Группа 2:\n" +
                "\"c\";\"g\";\"f\"\n" +
                "Группа 3:\n" +
                "\"x\";\"a\";\"s\"";
        strings = test3Groups.split("\n");
        for(String line : strings){
            analyzer.addLine(line);
        }
        List<Group> myList = analyzer.sortedGroups();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < myList.size(); i++){
            builder.append("Группа " + (i + 1) + ":\n" + myList.get(i));
            if(i + 1 != myList.size())
                builder.append("\n");
        }
        Assert.assertEquals(answer, builder.toString());
    }


    @Test
    public void doTestMerge3Groups(){
        String testMerge3Groups = "\"a\";\"d\";\"e\"\n" +
                "\"c\";\"g\";\"f\"\n" +
                "\"x\";\"x\";\"n\"\n" +
                "\"a\";\"l\";\"x\"\n" +
                "\"c\";\"g\";\"h\"\n" +
                "\"x\";\"m\";\"n\"\n" +
                "\"a\";\"g\";\"n\"";

        String answer = "Группа 1:\n" +
                "\"a\";\"d\";\"e\"\n" +
                "\"a\";\"l\";\"x\"\n" +
                "\"c\";\"g\";\"f\"\n" +
                "\"c\";\"g\";\"h\"\n" +
                "\"x\";\"x\";\"n\"\n" +
                "\"x\";\"m\";\"n\"\n" +
                "\"a\";\"g\";\"n\"";
        strings = testMerge3Groups.split("\n");
        for(String line : strings){
            analyzer.addLine(line);
        }
        List<Group> myList = analyzer.sortedGroups();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < myList.size(); i++){
            builder.append("Группа " + (i + 1) + ":\n" + myList.get(i));
            if(i + 1 != myList.size())
                builder.append("\n");
        }
        Assert.assertEquals(answer, builder.toString());
    }

    @Test
    public void doTest3GroupsWithEmpty(){
        String test3GroupsWithEmpty = "\"a\";\"d\";\"e\"\n" +
                "\"\";\"\";\"e\"\n" +
                "\"x\";\"x\";\"n\"\n" +
                "\"a\";\"d\";\"x\"\n" +
                "\"c\";\"g\";\"h\"\n" +
                "\"x\";\"h\";\"\"\n" +
                "\"\";\"h\";\"\"\n" +
                "\"c\";\"\";\"\"";

        String answer = "Группа 1:\n" +
                "\"a\";\"d\";\"e\"\n" +
                "\"\";\"\";\"e\"\n" +
                "\"a\";\"d\";\"x\"\n" +
                "Группа 2:\n" +
                "\"x\";\"x\";\"n\"\n" +
                "\"x\";\"h\";\"\"\n" +
                "\"\";\"h\";\"\"\n" +
                "Группа 3:\n" +
                "\"c\";\"g\";\"h\"\n" +
                "\"c\";\"\";\"\"";
        strings = test3GroupsWithEmpty.split("\n");
        for(String line : strings){
            analyzer.addLine(line);
        }
        List<Group> myList = analyzer.sortedGroups();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < myList.size(); i++){
            builder.append("Группа " + (i + 1) + ":\n" + myList.get(i));
            if(i + 1 != myList.size())
                builder.append("\n");
        }
        Assert.assertEquals(answer, builder.toString());
    }
}