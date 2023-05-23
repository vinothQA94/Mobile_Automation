package com.browserstack.run_first_test;

import CSVReader.Rows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvFileRead {
    public static List<String> CsvFIleRead() {
        Path fileName = Paths.get("C:\\Users\\Patil\\Downloads\\Directions.csv");
        List<String> Rowsa = null;
        // Now calling Files.readString() method to
        // read the file
        try {
            Rowsa = Files.readAllLines(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Rows rws=new Rows();
        rws.ParseData(Rowsa);
        return Rowsa;
    }




    public static void readCSVDynamic() {
        try(
                BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
        ) {
            for(CSVRecord record : parser) {
                System.out.println(record.get("First"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}