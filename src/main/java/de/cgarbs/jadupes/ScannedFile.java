/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.nio.file.Path;

/**
 * a relevant file
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class ScannedFile
{
	private Path file;

	/**
	 * creates a new scanned file
	 * 
	 * @param file
	 *            the Path of the file (directory + filename)
	 */
	public ScannedFile(Path file)
	{
		this.file = file;
	}

	/**
	 * @return the Path of this file (directory + filename)
	 */
	public Path getFile()
	{
		return file;
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
