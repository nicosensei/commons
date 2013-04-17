/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.File;

import fr.nikokode.textbatch.Tool;

import junit.framework.TestCase;

/**
 * @author ngiraud
 *
 */
public abstract class ToolTestCase extends TestCase {

    private final long initTime = System.currentTimeMillis();

    @Override
    protected void setUp() throws Exception {
        String path = ToolTestCase.class.getPackage().getName()
        .replaceAll("\\.", File.separator);
        Tool.init(
                "./src/test/resources/" + path +"/settings.xml" ,
                getName());
    }

    @Override
    protected void tearDown() throws Exception {
        Tool.cleanup(initTime);
    }

}