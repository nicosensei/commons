/**
 * 
 */
package fr.nikokode.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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
	public static void recursiveDelete(String dirPath) throws IOException {
		Path path = Paths.get(dirPath);
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {

				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir,
					IOException exc) throws IOException {

				if (exc == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					throw exc;
				}
			}

		});
	}
	
	/**
	 * Recursively deletes a directory by walking the file tree.
	 * @param dir the directory to delete.
	 * @throws IOException if deletion failed.
	 */
	public static void recursiveDelete(File dir) throws IOException {
		recursiveDelete(dir.getAbsolutePath());
	}

}
