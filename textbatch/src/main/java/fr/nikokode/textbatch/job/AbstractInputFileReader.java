/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import fr.nikokode.textbatch.Tool;


/**
 * Allows buffered reading of sections (fixed number of lines) of a text file.
 * Synchronized.
 *
 * @author ngiraud
 *
 */
public abstract class AbstractInputFileReader<L extends InputLine>
implements InputFileReader<L> {

    private static final String ENCODING = Tool.getInstance().getProperty(
            InputFileReader.class, "encoding");

    /**
     * Number of lines per section.
     */
    private int sectionSize = -1;

    private boolean ignoreEmptyLines = true;

    /**
     * Buffered reader for the input file.
     */
    private BufferedReader inputFile;

    private String inputFilePath;

    private int usableLineCount;
    private int emptyLineCount;

    public AbstractInputFileReader(
            String inputFile,
            int sectionSize,
            boolean ignoreEmptyLines) throws InputFileException {

        this.inputFilePath = inputFile;
        this.ignoreEmptyLines = ignoreEmptyLines;
        countLines(inputFile);

        Tool.getInstance().logInfo(
                usableLineCount + " lines to process in " + inputFile);

        try {

            this.inputFile = new BufferedReader(
                    new InputStreamReader(new FileInputStream(inputFile),
                            getEncoding()));
        } catch (FileNotFoundException e) {
            throw InputFileException.fileNotFound(inputFile);
        } catch (UnsupportedEncodingException e) {
            throw InputFileException.ioError(inputFilePath, e);
        }

        this.sectionSize = sectionSize;

        Tool.getInstance().logInfo("Processing input file by chunks of "
                + sectionSize + " lines.");
    }

    /**
     * Closes the reader.
     * @throws InputFileException
     */
    public synchronized void close() throws InputFileException {
        try {
            inputFile.close();
        } catch (IOException e) {
            throw InputFileException.closeFailed(inputFilePath, e);
        }
    }

    /**
     * Atomically obtain a section of the combined path file
     * @return
     * @throws InputFileException
     */
    public synchronized InputFileSection<L> readSection()
    throws InputFileException {

        List<L> lines = new LinkedList<L>();

        while (lines.size() < sectionSize) {
            String l;
            try {
                l = inputFile.readLine();
            } catch (IOException e) {
                throw InputFileException.readError(inputFilePath, e);
            }
            if (l == null) {
                break;
            }
            if (ignoreEmptyLines && lineIsEmpty(l)) {
                continue; // skip empty lines
            }
            lines.add(parseLine(l));
        }

        return new InputFileSection<L>(lines, lines.size() < sectionSize);

    }

    /**
     * Atomically obtain a line of the combined path file
     * @return
     * @throws InputFileException
     */
    public synchronized L readLine() throws InputFileException {

        L line = null;
        while (line == null) {

            String l;
            try {
                l = inputFile.readLine();
            } catch (IOException e) {
                throw InputFileException.readError(inputFilePath, e);
            }
            if (l == null) {
                break;
            }
            if (ignoreEmptyLines && lineIsEmpty(l)) {
                continue; // skip empty lines
            }
            line = parseLine(l);
        }

        return line;

    }

    public int getLineCount() {
        return usableLineCount + emptyLineCount;
    }

    public int getEmptyLineCount() {
        return emptyLineCount;
    }

    public int getNonEmptyLineCount() {
        return usableLineCount;
    }

    @Override
    public String getEncoding() {
        return ENCODING;
    }

    protected abstract L parseLine(String line) throws InputFileException;

    protected String getInputFilePath() {
        return inputFilePath;
    }

    private synchronized void countLines(String inputFile)
    throws InputFileException {
        this.usableLineCount = 0;

        try {
            BufferedReader tmp =
                new BufferedReader(new FileReader(inputFile));

            String l = null;
            while ((l = tmp.readLine()) != null) {

                if (lineIsEmpty(l)) {
                    this.emptyLineCount++;
                    if (! ignoreEmptyLines) {
                        this.usableLineCount++;
                    }
                } else {
                    this.usableLineCount++;
                }

            }

        } catch (FileNotFoundException e) {
            throw InputFileException.fileNotFound(inputFile);
        } catch (IOException e) {
            throw InputFileException.readError(inputFile, e);
        }
    }

    private boolean lineIsEmpty(String l) {
        return l.trim().isEmpty();
    }

}
