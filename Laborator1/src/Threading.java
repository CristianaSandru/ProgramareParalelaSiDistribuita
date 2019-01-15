import java.util.ArrayList;

public class Threading {
    private volatile int[] A;
    private volatile int[] B;
    private int[] C;
    private int Threads;
    private int Length;
    private int Size;

    public Threading (int[] a, int[] b, int l, int t, int s) {
        this.A = a;
        this.B = b;
        this.Length = l;
        this.Threads = t;
        this.Size = s;
    }

    class Add implements Runnable {
        private int Offset;
        private int End;

        Add(int offset, int end) {
            Offset = offset;
            End = end;
        }

        @Override
        public void run() {
//            System.out.println("Offset:" + Offset + " End:" + End + " Length:" + A.length);
            for (int i = Offset; i <= End; i++) {
                C[i] = A[i] + B[i];
            }
        }
    }

    public void do_add() {
        this.C = new int[A.length];
        int offset = 0;
        int length = this.Length;
        int threads = this.Threads;
        int batch_size = (int) Math.ceil(length / threads);
        int end = offset + batch_size -1;

        while (threads > 0) {
            //System.out.println("Offset:" + offset + " End: " + end + " Length:" + length + " Threads:" + threads + " Batch:" + batch_size);

            Add t = new Add(offset, end);
            new Thread(t).start();

            offset = end + 1;
            length = length - batch_size;
            threads = threads - 1;
            if (threads > 0) {
                batch_size = (int) Math.ceil(length / threads);
            }
            end = end + batch_size;
        }
    }

    class Multiply implements Runnable {
        private int Offset;
        private int End;

        Multiply(int offset, int end) {
            Offset = offset;
            End = end;
        }

        @Override
        public void run() {
            for (int i = Offset; i <= End; i++) {
                int prod = 0;
                for (int j = i*Size; j<(i+1)*Size; j++) {
                    prod = prod + A[j] * B[j];
                }
                C[i] = prod;
            }
        }
    }

    public void do_multipy() {
        this.C = new int[this.Length];
        int offset = 0;
        int length = this.Length;
        int threads = this.Threads;
        int batch_size = (int) Math.ceil(length / threads);
        int end = offset + batch_size -1;

//        long startTime = System.currentTimeMillis();
        while (threads > 0) {
//            System.out.println("Offset:" + offset + " End: " + end + " Length:" + length + " Threads:" + threads + " Batch:" + batch_size);

            Multiply t = new Multiply(offset, end);
            new Thread(t).start();
            offset = end + 1;
            length = length - batch_size;
            threads = threads - 1;
            if (threads > 0) {
                batch_size = (int) Math.ceil(length / threads);
            }
            end = end + batch_size;
        }
//        long stopTime = System.currentTimeMillis();
//        long elapsedTime = stopTime - startTime;
//        float total = elapsedTime/ (float)1000000000.000000;
//
//        System.out.println(String.format("Took %.10f seconds", total));
    }


    public int[] getC() {
        return this.C;
    }

    public void printC (int m) {
        for (int i = 0; i<C.length; i++) {
            if (i%m == 0 && i>0) {
                System.out.println();
            }
            System.out.print(this.C[i] + " ");
        }

    }
}
