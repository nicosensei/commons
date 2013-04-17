/**
 *
 */
package fr.nikokode.textbatch.utils.hosts;

import java.io.IOException;
import java.util.HashMap;

import fr.nikokode.textbatch.job.InputFileException;


/**
 * @author ngiraud
 *
 */
public class HostFile {

    private static HostFile instance;

    private HashMap<String, String> hostAliases = new HashMap<String, String>();

    public static void init(String file)
    throws InputFileException, IOException {
        instance = new HostFile(file);
    }

    public static HostFile getInstance() {
        return instance;
    }

    private HostFile(String file) throws InputFileException, IOException {
        HostFileReader in = new HostFileReader(file);

        HostFileLine l;
        while ((l = in.readLine()) != null) {
            if (l.isValidHostLine()) {
                hostAliases.put(l.getHostAlias1(), l.getHostAlias2());
            }
        }
    }

    public String getHostAlias(String host) {
        String alias = hostAliases.get(host);
        return (alias != null ? alias : host);
    }

}
