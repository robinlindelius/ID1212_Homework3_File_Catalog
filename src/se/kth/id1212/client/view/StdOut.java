package se.kth.id1212.client.view;

/**
 * Thread safe output
 * Created by Robin on 2017-11-16.
 */
public class StdOut {

    /**
     * Prints the given output
     * @param output The output to be printed
     */
    synchronized void print(String output) {
        System.out.print(output);
    }

    /**
     * Printsthe given output with a extra line break
     * @param output The output to be printed
     */
    synchronized void println(String output) {
        System.out.println(output);
    }
}
