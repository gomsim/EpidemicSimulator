import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class CsvWriter {

    private FileWriter fw;
    private String fileName;

    CsvWriter(FileWriter fw, String fileName){
        this.fw = fw;
        this.fileName = fileName;
    }

    public static CsvWriter open(String name){
        Calendar time = Calendar.getInstance();
        boolean hOneDigit = time.get(Calendar.HOUR_OF_DAY) < 10;
        boolean mOneDigit = time.get(Calendar.MINUTE) < 10;
        //name format: 12-06_Longterm.csv
        FileWriter fw = null;
        String fileName = 
            (hOneDigit ? "0": "") + //0
            time.get(Calendar.HOUR_OF_DAY) + //hour 
            "-" + 
            (mOneDigit? "0":"") + //0
            time.get(Calendar.MINUTE) + //minute
            "_" + 
            name.replaceAll(" ", ""); //algorithm name
        File file;
        int version = 0;
        do{
            file = new File("data/"+fileName + (++version == 1? "":"("+version+")") + ".csv");
        }while(file.exists());
        while(fw == null){
            try{
                fw = new FileWriter(file, true);
            }catch(Exception e){
                new File("data").mkdir();
            }
        }
        fileName = file.getName();
        System.out.println(fileName);
        return new CsvWriter(fw, fileName);
    }
    public void print(String str){
        try{
            fw.append(str);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String close(){
        try{
            fw.flush();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return fileName;
    }
}