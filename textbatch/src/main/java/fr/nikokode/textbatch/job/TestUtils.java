/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author ngiraud
 *
 */
public class TestUtils {

    public static void textFileEquals(File tf1, File tf2) throws IOException {

        BufferedReader in1 = new BufferedReader(new FileReader(tf1));
        BufferedReader in2 = new BufferedReader(new FileReader(tf2));

        try {

            while (true) {
                String line1 = in1.readLine();
                String line2 = in2.readLine();

                if (line1 == null && line2 == null) {
                    return;
                }

                TestCase.assertEquals(line1, line2);
            }

        } finally {
            in1.close();
            in2.close();
        }
    }

}