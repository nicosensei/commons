/**
 * 
 */
package com.github.nicosensei.commons.utils;

import java.io.File;
import java.io.IOException;

/**
 * Common file utilities.
 * 
 * @author ngiraud
 *
 */
public class FileUtils {

	/**
	 * Recursively deletes a directory by walking the file tree.
	 * @param dirPath the directory to delete.
	 * @throws IOException if deletion failed.
	 */
	// Java 7 implementation
//	public static void recursiveDelete(String dirPath) throws IOException {
//		Path path = Paths.get(dirPath);
//		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//
//			@Override
//			public FileVisitResult visitFile(Path file,
//					BasicFileAttributes attrs) throws IOException {
//
//				Files.delete(file);
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult postVisitDirectory(Path dir,
//					IOException exc) throws IOException {
//
//				if (exc == null) {
//					Files.delete(dir);
//					return FileVisitResult.CONTINUE;
//				} else {
//					throw exc;
//				}
//			}
//
//		});
//	}
	
	public static void recursiveDelete(String dirPath) throws IOException {
		File f = new File(dirPath);
		if (!f.exists()) {
            return;
        }

        // If the file is a directory, delete all files in this directory,
        // and its subdirectories
        if (f.isDirectory()) {
            File[] subfiles = f.listFiles();

            if (subfiles != null) { // Can be null in case of error
                for (File subfile : subfiles) {
                	recursiveDelete(subfile);
                }
            }
        }
        if (!f.delete()) {
            boolean isDir = f.isDirectory();
            if (!isDir) {
                remove(f);
            }
        }
	}
	
	/**
	 * Recursively deletes a directory by walking the file tree.
	 * @param dir the directory to delete.
	 * @throws IOException if deletion failed.
	 */
	public static void recursiveDelete(File dir) throws IOException {
		recursiveDelete(dir.getAbsolutePath());
	}
	
	/**
     * Remove a file.
     * @param f
     *            A file to completely and utterly remove.
     * @return true if the file did exist, false otherwise.
     * @throws ArgumentNotValid if f is null.
     * @throws SecurityException
     *             If a security manager exists and its <code>{@link
     *                           java.lang.SecurityManager#checkDelete}</code>
     *             method denies delete access to the file
     */
    public static boolean remove(File f) {
        if (!f.exists()) {
            return false;
        }
        if (f.isDirectory()) {
            return false; //Do not attempt to delete a directory
        }
        if (!f.delete()) {
            // Hack to remove file on windows! Works only sometimes!
            File delFile = new File(f.getAbsolutePath());
            delFile.delete();
            if (delFile.exists()) {
                return false;
            }
        }

        return true;
    }


}
