package fr.nikokode.textbatch.utils.hosts;

import fr.nikokode.textbatch.job.AbstractInputFileReader;
import fr.nikokode.textbatch.job.InputFileException;

public class HostFileReader extends AbstractInputFileReader<HostFileLine> {

    public HostFileReader(String inputFile) throws InputFileException {
        super(inputFile, 1, true);
    }

    public HostFileReader(String inputFile, int sectionSize)
    throws InputFileException {
        super(inputFile, sectionSize, true);
    }

    @Override
    protected HostFileLine parseLine(String line) {
        return new HostFileLine(line);
    }

}