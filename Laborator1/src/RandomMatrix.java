import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomMatrix {
    private int Lines;
    private int Columns;
    private String F;

    RandomMatrix(int lines, int columns, String file) {
        this.setLines(lines);
        this.setColumns(columns);
        this.setF(file);
    }

    public void GenerateMatrix() throws IOException {
        Random rand = new Random();
        FileWriter fw = new FileWriter(this.F);

        for(int i=1; i<=this.Lines; i++) {
            for(int j=1; j<=this.Columns; j++) {
                fw.write(String.valueOf(rand.nextInt(10))+" ");
            }
            fw.write("\n");
        }
        fw.close();
    }

    public int[][] ReadMatrixFromFile() throws IOException {
        int[][] matrix = new int[this.Lines][this.Columns];

        FileReader fileReader;
        fileReader = new FileReader(this.F);

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        int i = 0;
        while((line = bufferedReader.readLine()) != null) {
            String[] elementsStr;
            elementsStr = line.split(" ");
            int j = 0;
            for (String element: elementsStr) {
                matrix[i][j] = Integer.parseInt(element);
                j++;
            }
            i++;
        }
        bufferedReader.close();

        return matrix;
    }

    public int[] MatrixToVectorAdd(int[][] matrix) {
        int[] vector = new int[this.Lines * this.Columns];
        int el = 0;
        for (int i=0; i<=this.Lines-1; i++) {
            for (int j=0; j<=this.Columns -1; j++) {
                vector[el] = matrix[i][j];
                el++;
            }
        }
        return vector;
    }

    public int[] MatrixToVectorFM(int[][] matrix, int m) {
        int[] vector = new int[this.Lines * this.Columns * m];
        int el = 0;
        for (int i=0; i<=this.Lines-1; i++) {
            for (int k=1; k<=m; k++) {
                for (int j = 0; j <= this.Columns - 1; j++) {
                    vector[el] = matrix[i][j];
                    //System.out.print(vector[el] + " | ");
                    el++;
                }
            }
        }
        // System.out.println();

        return vector;
    }

    public int[] MatrixToVectorSM(int[][] matrix, int n) {
        int[] vector = new int[this.Lines * this.Columns * n];
        int el = 0;
        for (int k=1; k<=n; k++) {
            for (int i=0; i<=this.Columns-1; i++) {
                for (int j = 0; j <= this.Lines - 1; j++) {
                    vector[el] = matrix[j][i];
                    //System.out.print(vector[el] + " | ");
                    el++;
                }
            }
        }
        //System.out.println();
        return vector;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }

    public int getLines() {
        return Lines;
    }

    public void setLines(int lines) {
        Lines = lines;
    }

    public int getColumns() {
        return Columns;
    }

    public void setColumns(int columns) {
        Columns = columns;
    }
}