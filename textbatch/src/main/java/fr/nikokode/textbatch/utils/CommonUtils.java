/**
 *
 */
package fr.nikokode.textbatch.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ngiraud
 *
 */
public class CommonUtils {

    public static final void copyFile(String inputFilePath, String outputFilePath)
            throws IOException {
        BufferedInputStream in =
                new BufferedInputStream(new FileInputStream(inputFilePath));
        BufferedOutputStream out =
                new BufferedOutputStream(new FileOutputStream(outputFilePath));

        byte[] buffer = new byte[4096];
        int byteRead = 0;
        while ((byteRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, byteRead);
        }
        in.close();
        out.close();
    }

}
