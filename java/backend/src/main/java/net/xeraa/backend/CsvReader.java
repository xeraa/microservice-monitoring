package net.xeraa.backend;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader {
    public static ArrayList readAsStrings(String url) throws FileNotFoundException, IOException {
        /**returns all of the data in a file as Strings given the File object*/
        ArrayList data = new ArrayList();
        InputStream ips= CsvReader.class.getResourceAsStream(url);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader reader = new BufferedReader(ipsr);
        String nextLine = reader.readLine();
        while (nextLine != null) {
            data.add(nextLine);
            nextLine = reader.readLine();
        }
        reader.close();//just a good idea aparently

        return data;
    }

    public static ArrayList extractFromCommas(String dataLine) {
        //Gives back the data that is found between commas in a String
        ArrayList data = new ArrayList();
        String theString = "";
        for (int i = 0; i < dataLine.length(); i++) { //go down the whole string
            if (dataLine.charAt(i) == ',') {
                if (i == 0) {
                    //do nothing
                } else {
                    data.add(theString); //this means that the next comma has been reached
                    theString = ""; //reset theString Variable
                }
            } else {
                theString = theString + dataLine.charAt(i); //otherwise, just keep piling the chars onto the cumulative string
            }
        }
        if (!theString.equalsIgnoreCase("")) //only if the last position is not occupied with nothing then add the end on
        {
            data.add(theString);
        }
        return data;
    }
}
