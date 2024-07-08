package es.udc.redes.tutorial.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Copy {

    public static void main(String[] args) {
        // Check arg
        if (args.length != 2) {
            System.err.println("Error: Invalid number of arguments\nUsage: java es.udc.redes.tutorial.copy.Copy <source file> <destination file>");
            System.exit(-1);
        }

        // Get inputs
        String origin = args[0];
        String end = args[1];
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(origin);
            out = new FileOutputStream(end);
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.err.println("I/O error: " + e.getMessage());
            }
        }
    }
}