/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * a relevant file
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class ScannedFile
{
	private final Path file;
	private final long size;

	// only for testing
	protected ScannedFile(Path file, long size)
	{
		this.file = file;
		this.size = size;
	}

	/**
	 * creates a new scanned file
	 * 
	 * @param file
	 *            the Path of the file (directory + filename)
	 */
	public ScannedFile(Path file)
	{
		try
		{
			this.file = file;
			this.size = Files.size(file);
		} catch (IOException e)
		{
			// Rethrow as unchecked exception because of Stream
			// handling in Java 8. We want to exit anyway.
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the Path of this file (directory + filename)
	 */
	public Path getFile()
	{
		return file;
	}

	/**
	 * @return the size of this file
	 */
	public long getSize()
	{
		return size;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		if (obj instanceof Path)
		{
			return file.equals(obj);
		}
		if (obj instanceof ScannedFile)
		{
			ScannedFile other = (ScannedFile) obj;
			return file.equals(other.file);
		}
		return false;
	}
}
