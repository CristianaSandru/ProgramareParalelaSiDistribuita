import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class Main {
    private static boolean ok;
    private static Vector<Float> elements = new Vector<>();

    public static float calculate_time(float startTime, float stopTime) {
        float elapsedTime = stopTime - startTime;
        return elapsedTime / 1000000000 ;
    }

    public static void main(String[] args) {
        float start_time = System.nanoTime();
        try {
            FineGrainSync sortedLinkedList = new FineGrainSync();
//            CourseGrainSync sortedLinkedList = new CourseGrainSync();
            Logger log = new Logger("src\\op.log");
            Logger logIt = new Logger("src\\logIt.log");

            Thread threadAdd1 = new Thread(() -> {
                Random random = new Random();
                for(int i = 0; i < 100; i++) {
                    float nr = random.nextFloat();
                    nr = nr % 100;
                    sortedLinkedList.insert(nr);
                    elements.add(nr);
                    try {
                        log.writeToFile("T1:: insert: " + nr + " , time: " + calculate_time(start_time, System.nanoTime()) + "\n");
                    } catch (IOException e) {
                        System.out.println("A aparut o eroare la scrierea in fisier thread1: " + e);
                    }
               }
            });

            Thread threadAdd2 = new Thread(() -> {
                Random random = new Random();
                for (int i = 0; i < 50; i++) {
                    float nr = random.nextFloat();
                    nr = nr % 100;
                    sortedLinkedList.insert(nr);
                    elements.add(nr);
                    try {
                        log.writeToFile("T2:: insert: " + nr + " , time: " + calculate_time(start_time, System.nanoTime()) + "\n");
                    } catch (IOException e) {
                        System.out.println("A aparut o eroare la scrierea in fisier thread2 : " + e);
                    }
                }
            });

            Thread threadRemove = new Thread(() -> {
                Random random = new Random();
                for (int i = 0; i < 50; i++) {
                    int index = -1;
                    if (elements.size() > 0) {
                         index = random.nextInt() % elements.size();
                    }
                    float nr;
                    try {
                        nr = elements.get(index);
                    } catch (Exception e) {
                        nr = random.nextFloat() % 100;
                    }
                    sortedLinkedList.remove(nr);
                    try {
                        log.writeToFile("T3:: remove: " + nr + " , time: " + calculate_time(start_time, System.nanoTime())+ "\n");
                    } catch (IOException e) {
                        System.out.println("A aparut o eroare la scrierea in fisier thread3 : " + e);
                    }
                }
            });

            Thread threadIterator = new Thread(() -> {
                int count = 0;
                while (!ok) {
                    count++;
                    SortedLinkedList.It it = sortedLinkedList.getIterator();
                    String str = "";
                    while (it.hasNext()) {
                        if (str == "") {
                            str = "" + it.next();
                        } else {
                            str = str + " , " + it.next();
                        }
                    }
//                    System.out.println(str);
                    try {
                        log.writeToFile("Iterator:: " + str + "\n");
                        logIt.writeToFile("It " + count + ": " + str + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1);

                    } catch (InterruptedException e) {
                        System.out.println("A aparut o eroare la Thread.sleep: " + e);
                    }
                }
            });

            threadAdd1.start();
            threadAdd2.start();
            threadRemove.start();
            threadIterator.start();
            threadAdd1.join();
            threadAdd2.join();
            threadRemove.join();
            ok = true;
            threadIterator.join();
            log.writeToFile("---------------------------------\n");
            logIt.writeToFile("---------------------------------\n");
            System.out.println("Took " + calculate_time(start_time, System.nanoTime()) + " seconds");
        } catch (IOException e) {
            System.out.println("A aparut o eroare: " + e);
        } catch (InterruptedException e) {
            System.out.println("A aparut o eroare la Thread.join: " + e);
        }
    }
}
