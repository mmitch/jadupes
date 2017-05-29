/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Compare files for content
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class FileComparator
{

	/**
	 * Reads two files and verifies that they contain the same content.
	 * 
	 * Comparison is done block by block. There are <u>no</u> additional checks
	 * that may short-circuit like comparing the filesize before reading the
	 * files.
	 * 
	 * @param file1
	 *            the first file to compare
	 * @param file2
	 *            the second file to compare
	 * @return true == both files have equal content
	 * @throws IOException
	 *             some error accessing a file
	 */
	public boolean haveSameContent(Path file1, Path file2) throws IOException
	{
		byte[] buffer1 = new byte[Configuration.READ_BLOCK_SIZE];
		byte[] buffer2 = new byte[Configuration.READ_BLOCK_SIZE];

		int bytesRead1;
		int bytesRead2;

		InputStream input1 = Files.newInputStream(file1);
		InputStream input2 = Files.newInputStream(file2);

		boolean sameContent = true;

		do
		{
			bytesRead1 = input1.read(buffer1);
			bytesRead2 = input2.read(buffer2);

			if (bytesRead1 != bytesRead2)
			{
				sameContent = false;
				break;
			}

			if (!Arrays.equals(buffer1, buffer2))
			{
				sameContent = false;
				break;
			}
		} while (bytesRead1 >= 0 && bytesRead2 >= 0);

		input1.close();
		input2.close();

		return sameContent;
	}

}
