import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    static class Operations {
        public int n;
        public int m;
        public int t;
        public int k;

        public void get_params_add() {
            System.out.print("n = ") ;
            Scanner sc = new Scanner(System.in);
            this.n = sc.nextInt();
            System.out.print("m = ");
            this.m = sc.nextInt();
            System.out.print("t = ");
            this.t = sc.nextInt();
        }

        public void get_params_multiply() {
            System.out.print("n = ") ;
            Scanner sc = new Scanner(System.in);
            this.n = sc.nextInt();
            System.out.print("m = ");
            this.m = sc.nextInt();
            System.out.print("k = ");
            this.k = sc.nextInt();
            System.out.print("t = ");
            this.t = sc.nextInt();
        }

        public void set_params_manually() {
            this.n =3;
            this.k = 3;
            this.m = 3;
            this.t = 2;
        }

        public void Adding() throws IOException, InterruptedException {

            RandomMatrix A = new RandomMatrix(n, m, "matrix1.txt");
            RandomMatrix B = new RandomMatrix(n, m, "matrix2.txt");

            A.GenerateMatrix();
            B.GenerateMatrix();

            int[][] a = A.ReadMatrixFromFile();
            int[] v1 = A.MatrixToVectorAdd(a);

            int[][] b = B.ReadMatrixFromFile();
            int[] v2 = B.MatrixToVectorAdd(b);

            //System.out.println(Runtime.getRuntime().availableProcessors());
            long startTime = System.currentTimeMillis();
            Threading tt = new Threading(v1, v2, n * m, t, 0);
            tt.do_add();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            float total = elapsedTime / (float) 1000.000000;

            System.out.println(String.format("Took %.20f", total));
//          TimeUnit.SECONDS.sleep(1);
//            tt.printC(k);
        }

        public void Multiply() throws IOException {
            RandomMatrix A = new RandomMatrix(n,m,"matrix1.txt");
            RandomMatrix B = new RandomMatrix(m,k, "matrix2.txt");

            A.GenerateMatrix();
            B.GenerateMatrix();

            int[][] a = A.ReadMatrixFromFile();
            int[] v1 = A.MatrixToVectorFM(a, k);

            int[][] b = B.ReadMatrixFromFile();
            int[] v2 =B.MatrixToVectorSM(b, n);

            long startTime = System.currentTimeMillis();

            Threading t2 = new Threading(v1,v2, n*k ,t, m);
            t2.do_multipy();

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            float total = elapsedTime/ (float)1000.000000;

            System.out.println(String.format("Took %.10f seconds", total));

            //TimeUnit.SECONDS.sleep(5);
            t2.printC(k);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Operations op = new Operations();
        op.set_params_manually();
//        op.Adding();
        op.Multiply();

    }
}
