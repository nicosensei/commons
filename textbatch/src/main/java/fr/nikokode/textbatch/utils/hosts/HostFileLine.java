package fr.nikokode.textbatch.utils.hosts;

import fr.nikokode.textbatch.job.BasicInputLine;

public class HostFileLine extends BasicInputLine {

    protected HostFileLine(String line) {
        super(line, DEFAULT_SEP);
    }

    public String getIPAddress() {
        return getFields()[0];
    }

    public String getHostName() {
        return getFields()[1];
    }

    public String getHostAlias1() {
        return getFields()[2];
    }

    public String getHostAlias2() {
        return getFields()[3];
    }

    public boolean isValidHostLine() {
        return (! getLine().startsWith("#")) && (getFields().length >= 4);
    }

}
