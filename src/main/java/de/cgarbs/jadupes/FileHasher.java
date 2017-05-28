/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import net.openhft.hashing.LongHashFunction;

/**
 * Creates hashes/digests of files.
 * 
 * The <a href="http://cyan4973.github.io/xxHash/">xxHash</a> algorithm is very
 * fast, but can produce collisions.
 * 
 * This is OK because we only infer <i>if two hashes are different,
 * the files are different</i>. The other way round would not be correct (<i>if
 * two hashes are the same, the files are the same</i>).
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class FileHasher
{
	private static final int READ_BLOCK_SIZE = 4096;

	/**
	 * returns the xxHash of a file
	 * 
	 * @param file
	 *            the file to check
	 * @return the xxHash value of the file
	 * @throws IOException
	 *             some error accessing the file
	 */
	public long getHash(Path file) throws IOException
	{
		byte[] buffer = new byte[READ_BLOCK_SIZE];
		long hashValue = 0;
		int bytesRead;

		InputStream input = Files.newInputStream(file);
		while ((bytesRead = input.read(buffer)) >= 0)
		{
			hashValue = LongHashFunction.xx(hashValue).hashBytes(buffer, 0, bytesRead);
		}

		return hashValue;
	}
}
