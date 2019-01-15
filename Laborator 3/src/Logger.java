import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private String filename;

    public Logger(String filename) throws IOException {
        this.filename = filename;
    }

    public void writeToFile(String stringToWrite) throws IOException {
        FileWriter fileWriter=new FileWriter(filename,true);
        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
        bufferedWriter.write(stringToWrite);
        bufferedWriter.close();
    }
}
