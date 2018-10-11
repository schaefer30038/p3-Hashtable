////////////////////////////////////////////////////////////////////////////////
// Semester:         CS400 Spring 2018
// PROJECT:          cs400_p3 (Performance Analysis of Data Structures:
//                   Hashtables)
// FILES:            HashTable.java, AnalysisTest.java, HashTableADT.java,
//                   PerformanceAnalysisHash.java, PerformanceAnalysis.java
//                   
//
//
// USER:             Andrew Schaefer (acschaefer@wisc.edu)
//
// Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs:             no known bugs
//
//
//////////////////////////// 80 columns wide ///////////////////////////////////


import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;


/*
 * This class reads what files to collect data on from the provided data_details.txt
 * Stores time and memory inside the newly created file "PerformanceAnalysisReport.txt". 
 * Later on it prints the contents of the file to display the information. Set working 
 * directory to ${workspace_loc:p3/src}
 */
public class PerformanceAnalysisHash implements PerformanceAnalysis {

    // The input data from each file is stored in this/ per file
    private ArrayList<String> inputData;
    HashTable table;
    TreeMap map;
    
    // these variables are used to calculate the time and memory for each method
    Runtime runtime = Runtime.getRuntime();
    long startTime;
    long endTime;
    long startMemory;
    long endMemory;
    
    // time and memory of hashtable and treemap is stored here
    long[]time;
    long[]memory;
    
    // keeps track of the FileName being read for output purposes
    String fileName;
    
    // these help record the data into a newly created text file. The text file
    // is meant to provide more flexibility when recording down information
    PrintStream file;
    PrintStream console;
    
    
    /*
     * default constructor that reads the data_details.txt file. Reads the names of
     * the other .txt files in data_details.txt. Automatically reads and
     * stores the data in PerformanceAnalysisReport.txt. Automatically calls the 
     * compareDataStructures() method to compare all the text files provided in the 
     * data_details.txt file. AnalysisTest.java should not call compareDataStructures()
     * for this reason
     */
    public PerformanceAnalysisHash(){
        
        try {
            addHeader();
            FileInputStream fileStream = new FileInputStream("../data/data_details.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
            String line;    // stores the whole line
            String[] parts; // splits the line by the comma to get the filename
            
            // in case file has no lines
            if ((line = reader.readLine()) == null) {
                System.out.println("ERROR: file is empty!");
                reader.close();
                return;
            }
            

            parts = line.split(",");
            // checks to see if the file indicates the right format
            if (!(parts[0].equals("filepath"))) {
                System.out.println("ERROR: file has the wrong format");
                reader.close();
                return;
            }
            
            String filePath = parts[1];
            
            // reads until all lines have been read
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                fileName = parts[0];
                loadData(filePath + "/" + fileName);
                
                // new time and memory arrays are reinitialized for every
                // data file
                time = new long[6];
                memory = new long[6];
                compareDataStructures();
            }
            addEnding();
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    
    /*
     * Same as the previous constructor, but reads and collects data from a given .txt 
     * file given in the argument. If run on eclipse, program argument needs to be
     * ../data/data_details.txt
     */
    public PerformanceAnalysisHash(String details_filename){
        //TODO: Save the details of the test data files
        try {
            addHeader();
            FileInputStream fileStream = new FileInputStream(details_filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
            String line;    // stores the whole line
            String[] parts; // splits the line by the comma to get the filename
            
            // in case file has no lines
            if ((line = reader.readLine()) == null) {
                System.out.println("ERROR: file is empty!");
                reader.close();
                return;
            }
            

            parts = line.split(",");
            // checks to see if the file indicates the right format
            if (!(parts[0].equals("filepath"))) {
                System.out.println("ERROR: file has the wrong format");
                reader.close();
                return;
            }
            
            String filePath = parts[1];
            
            // reads until all lines have been read
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                fileName = parts[0];
                loadData(filePath + "/" + fileName);
                
                // new time and memory arrays are reinitialized for every
                // data file
                time = new long[6];
                memory = new long[6];
                compareDataStructures();
            }
            addEnding();
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /*
     * this is used to call other comparison methods between treemap and hashtable.
     * Reinitializes the Hashtable and TreeMap for each .txt file
     */
    @Override
    public void compareDataStructures() {
        table = new HashTable(11, 0.7);
        map = new TreeMap();
        compareInsertion();
        compareSearch();
        compareDeletion();
        printToFile();
    }
    
    
    
    
    /*
     * Adds information from the data fields into PerformanceAnalysisReport.txt
     */
    private void printToFile() {
        try {
            // these Strings help keep track of the information for each output
            // line
            String operation;
            String dataStructure;
            System.setOut(file);    // this sets printout to the text file
            
            for (int i=0; i<time.length; i++) {
                operation = "REMOVE";
                if (i<4) {
                    operation = "GET";
                } if (i<2) {
                    operation = "PUT";
                }
                if (i==0 || i==2 || i==4) {
                    dataStructure = "HASHTABLE";
                } else {
                    dataStructure = "TREEMAP";
                }   // stores the information/format into the created text file
            System.out.println("|"+stringHelper(fileName, 20) +
                    stringHelper(operation, 15)+stringHelper(dataStructure,15)
                    + stringHelper(""+time[i], 25) +stringHelper(""+memory[i], 15));
            }
            
            System.setOut(console); // this sets printout to the console
        } catch (Exception e) {
            System.out.println("error at comparedatastructures()");
        }
        
        
    }

    
    
    /*
     * adds in whitespace to fit a specific length
     */
    private String stringHelper(String string, int size) {
        
        // will add white spaces until the string reaches the size
        while (string.length() < size) {
            string = " " + string;
        }
        string = string + "|";
        return string;
    }
    
    
    /*
     * Starts the memory and time collection
     */
    public void beforeMethod() {
        runtime.gc();
        startTime = System.nanoTime();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
    }
    
    
    
    /*
     * Records the amount of memory and time used and subtracts it with the start
     * time and start memory. Time is divided by 1000 to convert nanoseconds into
     * microseconds.
     */
    public void afterMethod(int index) {
        endTime = System.nanoTime();
        endMemory = runtime.totalMemory() - runtime.freeMemory();
        time[index] = (endTime - startTime)/1000;
        memory[index] = endMemory - startMemory;
    }
    
    
    /*
     * adds the information header to the created text file. This is where a new
     * PerformanceAnalysisReport.txt file is created, which will be used later to 
     * store and print information from.
     */
    private void addHeader() {
        try {
            
            file = new PrintStream(new File("PerformanceAnalysisReport.txt"));
            
            console = System.out;
            System.setOut(file);    // this sets printout to the text file
            
            System.out.println("The report name : Performance Analysis Report");
            for (int i=0; i<96; i++) {
                System.out.print("-");
            }
            System.out.println("\n" + "|            FileName|      Operation| Data" +
                "Structure|   Time Taken (micro sec)|     Bytes Used|");
            
            for (int i=0; i<96; i++) {
                System.out.print("-");
            }
            System.out.println("");
            System.setOut(console);
        } catch (Exception e) {
            System.out.println(e+" at addHeader()");
        }
    }
    
    
    
    /*
     * adds bottom line at the bottom end of the created text file
     */
    private void addEnding() {
        try {
            System.setOut(file);    // this sets printout to the text file
            
            for (int i=0; i<96; i++) {
                System.out.print("-");
            }
            System.setOut(console);
            
        } catch (Exception e) {
            System.out.println(e+ " at addEnding()");
        }
    }
    
    
    /*
     * prints out the final report as a table by reading from the created text file:
     * PerformanceAnalysisReport.txt. Has the time and memory usage for each 
     * comparison method
     */
    @Override
    public void printReport() {
        //TODO: Complete this method
        try {
            //FileInputStream fileStream = new FileInputStream("results.txt");
            FileInputStream fileStream = new FileInputStream("PerformanceAnalysisReport.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);   // reads the entire file
            }
            
        } catch (IOException e) {
            System.out.println("failed to print report");
        }
        
    }

    
    
    /*
     * calculates the time and memory of the insertion method for hashtable and 
     * treemap
     */
    @Override
    public void compareInsertion() {
        //TODO: Complete this method
        
        
        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in hashtable
            table.put(inputData.get(i), inputData.get(i));
        }
        afterMethod(0);
        
        
        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in treemap
            map.put(inputData.get(i), inputData.get(i));
        }
        afterMethod(1);
        
    }

    
    
    /*
     * calculates the time and memory of the deletion method for hashtable and 
     * treemap
     */
    @Override
    public void compareDeletion() {
        //TODO: Complete this method
        

        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in hashtable
            table.remove(inputData.get(i));
        }
        afterMethod(4);
        
        
        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in treemap
            map.remove(inputData.get(i));
        }
        afterMethod(5);
        
        
    }

    
    
    /*
     * calculates the time and memory of the get method for hashtable and treemap
     */
    @Override
    public void compareSearch() {
        //TODO: Complete this method
        
        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in hashtable
            table.get(inputData.get(i));
        }
        afterMethod(2);
        
        
        beforeMethod();
        for (int i=0; i<inputData.size(); i++) {
            // record the time when putting in treemap
            map.get(inputData.get(i));
        }
        afterMethod(3);
        
        
        
    }

    
    
    /*
    An implementation of loading files into local data structure is provided to you
    Please feel free to make any changes if required as per your implementation.
    However, this function can be used as is.
     */
    @Override
    public void loadData(String filename) throws IOException {

        // Opens the given test file and stores the objects each line as a string
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        inputData = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            inputData.add(line);
            line = br.readLine();
        }
        br.close();
    }
}
