import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public abstract class Algorithm{

    private String name;
    protected int timePassed;
    private int maxTime;
    private CsvWriter csvWriter;

    public Algorithm(String name, int maxTime){
        this.name = name;
        this.timePassed = 0;
        this.maxTime = maxTime;
    }

    public boolean isDone(){
        return maxTime == timePassed;
    }

    public void openOutputStream(String ... headers){
        csvWriter = CsvWriter.open(name);
        for(int i = 0; i < headers.length; i++){
            csvWriter.print(headers[i] + (i < headers.length-1 ? ",":"\n"));
        }
    }
    public abstract int[][] init();
    public void tick(){
        timePassed++;
    }
    public void logOutput(int ... data){
        for(int i = 0; i < data.length; i++){
            csvWriter.print(data[i] + (i < data.length-1 ? ",":"\n"));
        }
    }
    public void closeStream() {
        String line;
        String fileName = csvWriter.close();
        Process process = null;
        try {
            System.out.println("Running Rscript ./src/main/RScript.r data/" + fileName + " plots/ " + System.getProperty("user.dir"));
            process = Runtime.getRuntime().exec("Rscript ./src/main/RScript.r data/" + fileName + " plots/ " + System.getProperty("user.dir"));
            JOptionPane.showMessageDialog(null, "Analysing data collected from simulation.\n"+
            "This is a very resouce intensive process that might take a couple of minutes.\n"+
            "Do not close SOC simulator while during this process!", "Analysing data", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
            JOptionPane.showMessageDialog(null, "Data analysis completed successfully.\n"+
            "The resulting document can be found in the plots folder.\n"+
            "The SOC simulator can now safetly be terminated.", "Analysis complete!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return name;
    }
}