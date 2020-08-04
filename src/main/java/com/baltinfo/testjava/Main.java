package com.baltinfo.testjava;

import com.baltinfo.testjava.Analyzer.Analyzer;
import com.baltinfo.testjava.Analyzer.Group;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        long time = System.currentTimeMillis();

        //changed to lng-big.csv
        URI filePath = Main.class.getClassLoader().getResource("lng-big.csv").toURI();
        List<String> text = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

        int counter = 0;
        Analyzer analyzer = new Analyzer();

        System.out.println("Start...");
        for(String line: text){
            counter++;
            analyzer.addLine(line);
            //To see wrong lines in file, comment upper line and uncomment two lines below
            //if (!analyzer.addLine(line))
//                System.out.println("Wrong line = " + line + " with number == " + counter);
        }

        List<Group> resultGroups = analyzer.sortedGroups();

        int countMultiGroups = (int) analyzer.getMultiGroups();
        int groupsSize = analyzer.getGroups();

        System.out.println("Groups with more than 1 element: " + countMultiGroups);
        System.out.println("All groups: " + groupsSize);
        try(FileWriter writer = new FileWriter("bigCsvResults.txt", false)){
            writer.write("Групп с более чем 1 элементом: " + countMultiGroups + "\n");
            for (int k = 0; k < resultGroups.size(); k++) {
                writer.write("Группа " + k + 1 + "\n" + resultGroups.get(k) + "\n");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        System.out.println("The program finished in " + (System.currentTimeMillis() - time)/1000 + " s");
        System.out.println("End...");
    }
}
