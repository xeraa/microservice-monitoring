package net.xeraa;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Bad {

    /*
     * Allocate until you run into an OOM exception.
     */
    public static void main(String[] args) throws Exception {
        List<long[]> list = new LinkedList<long[]>();
        while (true) {
            list.add(new long[65536]); // An arbitrary number
            sleep(50);
        }
    }
}
