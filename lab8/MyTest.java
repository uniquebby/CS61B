import java.util.HashMap;
import java.io.IOException;
import java.util.Scanner;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

/** Performs a timing test on three different set implementations.
 *  @author Josh Hug
 *  @author Brendan Hu
 */
public class MyTest {
    /**
     * Requests user input and performs tests of three different set
     * implementations. ARGS is unused.
     */
    public static void main(String[] args) throws IOException {
        int N = 39999;
        int L = 20;

        System.out.println("\n This program inserts random "
                           + N
                           + " Strings of length \n"
                           + L
                           + " Into different types of maps "
                           + "as <String, Integer> pairs.\n");

            String repeat = "y";
            int fastsize = 0, fastsubsize = 0;
            double fastld = 0, fastsubld = 0, fasttiem = 9999999.9, builtinfasttiem = 99999.9, temptiem = 0, builtintemptime = 0,
                    sumtiem = 0, builtinsumtiem = 0;
            int TIMES = 1;
            int resizeoff = 2, subresizeoff = 2;
            for (int initSize = 1; initSize <= 4; initSize += 1) {
                for (int subinitSize = 1; subinitSize <= 16; subinitSize += 1,
                        System.out.print(" subinitSize:" + subinitSize + "----------initSize:" + initSize + '\n')) {
                    for (double loadFactor = 4.5; loadFactor <= 7; loadFactor += 0.1) {
                        for (double subLoadFactor = 1; subLoadFactor <= 9; subLoadFactor *= 1.5) {
                            for (int sizeToBST = 8; sizeToBST <= 32; sizeToBST++) {

                                sumtiem = 0;
                                builtinsumtiem = 0;
                                for (int i = 0; i < TIMES; ++i) {
                                    temptiem = timeRandomMap61B(new MyHashBSTMap<String, Integer>(initSize, loadFactor,
                                            subLoadFactor, subinitSize, getOff(loadFactor), getOff(subLoadFactor), sizeToBST), N, L);
                                    builtintemptime = timeRandomHashMap(new HashMap<String, Integer>(),
                                            N, L);
                                    sumtiem += temptiem;
                                    builtinsumtiem += builtintemptime;
                                }
//                                temptiem = sumtiem / TIMES;
                                temptiem = sumtiem;
//                                builtintemptime = builtinsumtiem / TIMES;
                                builtintemptime = builtinsumtiem;
                                //   System.out.print(" avergetime: " + temptiem + '\n');
                                if (temptiem <= fasttiem) {
                                    builtinfasttiem = builtintemptime;
                                    fasttiem = temptiem;
                                    fastsize = initSize;
                                    fastsubsize = subinitSize;
                                    fastld = loadFactor;
                                    fastsubld = subLoadFactor;
                                }
                                    System.out.print(" initSize:" + initSize);
                                    System.out.print(" subinitSize:" + subinitSize);
                                    System.out.print(" loadFactor:" + loadFactor);
                                    System.out.print(" subLoadFactor: " + subLoadFactor);
                                    System.out.print(" fasttime: " + fasttiem);
                                    System.out.print(" temptiem: " + temptiem);
                                    System.out.print(" builtintime: " + builtinfasttiem + '\n');
                                    System.out.print(" sizetobst: " + sizeToBST + '\n');
                            }
                        }
                    }
                }
            }
            System.out.print(" fastinitSize:" + fastsize);
            System.out.print(" fastsubinitSize:" + fastsubsize);
            System.out.print(" fastloadFactor:" + fastld);
            System.out.print(" fastresizeoff:" + resizeoff);
            System.out.print(" fastresubsizeoff:" + subresizeoff);
            System.out.print(" fastsubLoadFactor: " + fastsubld + '\n');
            System.out.print(" fasttime: " + fasttiem + '\n');
    }

    /**
     * Returns time needed to put N random strings of length L into the
     * Map61B 61bMap.
     */
    public static double insertRandom(Map61B<String, Integer> map61B, int N, int L) {
        Stopwatch sw = new Stopwatch();
        String s = "cat";
        for (int i = 0; i < N; i++) {
            s = StringUtils.randomString(L);
            map61B.put(s, new Integer(i));
        }
        return sw.elapsedTime();
    }


    /**
     * Attempts to insert N random strings of length L into map,
     * Prints time of the N insert calls, otherwise
     * Prints a nice message about the error
     */
    public static double timeRandomMap61B(Map61B<String, Integer> map, int N, int L) {
        try {
            double mapTime = insertRandom(map, N, L);
  //          System.out.printf(map.getClass() + "        : %.3f sec\n", mapTime);
            return mapTime;
        } catch (StackOverflowError e) { 
            printInfoOnStackOverflow(N, L); 
        } catch (RuntimeException e) { 
            e.printStackTrace(); 
        }
        return 9999999.9;
    }

    /**
     * Attempts to insert N random strings of length L into a HashMap
     * Prints time of the N insert calls, otherwise
     * Prints a nice message about the error
     */
    public static double timeRandomHashMap(HashMap<String, Integer> hashMap, int N, int L) {
        try {
            double javaTime = insertRandom(hashMap, N, L);
//            System.out.printf("Java's Built-in HashMap: %.3f sec\n", javaTime);
            return javaTime;
        } catch (StackOverflowError e) {
            printInfoOnStackOverflow(N, L);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return 9999999.9;
    }

    /**
     * Returns time needed to put N random strings of length L into the
     * HashMap hashMap.
     */
    public static double insertRandom(HashMap<String, Integer> hashMap, int N, int L) {
        Stopwatch sw = new Stopwatch();
        String s = "cat";
        for (int i = 0; i < N; i++) {
            s = StringUtils.randomString(L);
            hashMap.put(s, new Integer(i));
        }
        return sw.elapsedTime();
    }

    /**
     * Waits for the user on other side of Scanner
     * to enter a positive int,
     * and outputs that int
     */
    public static int waitForPositiveInt(Scanner input) {
        int ret = 0;
        do {
            while (!input.hasNextInt()) {
                errorBadIntegerInput();
                input.next();
            }
            ret = input.nextInt();
            input.nextLine(); //consume \n not taken by nextInt()
        } while (ret <= 0);
        return ret;
    }
    /* ------------------------------- Private methods ------------------------------- */
    /**
     * To be called after catching a StackOverflowError
     * Prints the error with corresponding N and L
     */
    private static void printInfoOnStackOverflow(int N, int L) {
        System.out.println("--Stack Overflow -- couldn't add " + N 
                            + " strings of length " + L + ".");
    }

    /**
     * Prints a nice message for the user on bad input
     */
    private static void errorBadIntegerInput() {
        System.out.print("Please enter a positive integer: ");
    }

    public static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }

    public static int getOff(double factor) {
        if (factor <= 1) {
            return 1;
        } else {
            return (int) (log2(factor) + 1.58);
        }
    }

    @Test
    public void testgetoff() {
        System.out.println(getOff(4));
    }
}

