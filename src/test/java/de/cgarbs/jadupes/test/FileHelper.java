/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import de.cgarbs.jadupes.data.Configuration;

/**
 * Helper methods regarding Files and Pathes for unit testing.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class FileHelper
{
	/**
	 * create a subdirectory
	 * 
	 * @param directory
	 *            where to create the subdirectory
	 * @param subdir
	 *            subdirectory name
	 * @return the subdirectory
	 * @throws IOException
	 *             something went wrong
	 */
	public static Path createSubdirectory(Path directory, String subdir) throws IOException
	{
		return Files.createDirectories(directory.resolve(subdir));
	}

	/**
	 * create a new file with given content
	 * 
	 * @param directory
	 *            where to create the file
	 * @param filename
	 *            the name of the new file
	 * @param content
	 *            the content of the new file
	 * @return the file
	 * @throws IOException
	 *             something went wrong
	 */
	public static Path createFileWithContent(Path directory, String filename, String content) throws IOException
	{
		Path file = directory.resolve(filename);
		BufferedWriter writer = Files.newBufferedWriter(file);
		writer.write(content);
		writer.close();
		return file;
	}

	/**
	 * create a sparse file
	 * 
	 * @param directory
	 *            where to create the file
	 * @param filename
	 *            the name of the new file
	 * @return the sparse file
	 * @throws IOException
	 *             something went wrong
	 */
	public static Path createSparseFile(Path directory, String filename) throws IOException
	{
		Path file = directory.resolve(filename);
		RandomAccessFile randomAccessFile = new RandomAccessFile(file.toFile(), "w");
		randomAccessFile.seek(1 << 20);
		randomAccessFile.writeChar('x');
		randomAccessFile.close();
		return file;
	}

	/**
	 * create a new file with a huge block of fixed date and
	 * the given content at the end
	 * 
	 * @param directory
	 *            where to create the file
	 * @param filename
	 *            the name of the new file
	 * @param content
	 *            the content at the end of the new file
	 * @return the file
	 * @throws IOException
	 *             something went wrong
	 */
	public static Path createBigFileWithEndingContent(Path directory, String filename, String content) throws IOException
	{
		Path file = directory.resolve(filename);
		BufferedWriter writer = Files.newBufferedWriter(file);
		for (int i = 0; i < Configuration.READ_BLOCK_SIZE; i++)
		{
			writer.write("XXX");
		}
		writer.write(content);
		writer.close();
		return file;
	}
}
