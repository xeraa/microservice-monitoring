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
            list.add(new long[200_000]); // How many elements to allocate
            System.out.println(formatBytes(Runtime.getRuntime().totalMemory()) + " of " +
                    formatBytes(Runtime.getRuntime().maxMemory()) + " allocated; " +
                    formatBytes(Runtime.getRuntime().freeMemory()) + " free");
            sleep(75);
        }
    }

    /**
     * Format the bytes in a human readable format.
     * Size units have a distance of 10 bits (1024=2^10) meaning the position of the highest 1 bit - or in other words
     * the number of leading zeros - differ by 10 (Bytes=KB*1024, KB=MB*1024,...).
     */
    private static String formatBytes(long bytes) {
        if (bytes < 1024){
            return bytes + "B";
        }
        int unit = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        return String.format("%.1f%sB", (double)bytes / (1L << (unit * 10)), " KMGTPE".charAt(unit));
    }
}
