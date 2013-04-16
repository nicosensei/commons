/**
 *
 */
package fr.nikokode.textbatch.job;

/**
 * @author ngiraud
 *
 */
public interface InputFileReader<L extends InputLine> {

    /**
     * Closes the reader.
     * @throws InputFileException
     */
    void close() throws InputFileException;

    /**
     * Atomically obtain a section of the combined path file
     * @return
     * @throws InputFileException
     */
    InputFileSection<L> readSection() throws InputFileException;

    /**
     * Atomically obtain a line of the combined path file
     * @return
     * @throws InputFileException
     */
    L readLine() throws InputFileException;

    int getLineCount();

    int getEmptyLineCount();

    int getNonEmptyLineCount();

    /**
     * @returns the encoding to use to read files.
     */
    String getEncoding();

}
