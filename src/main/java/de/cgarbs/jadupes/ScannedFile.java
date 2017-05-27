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
	private final Path name;
	private final Path directory;

	/**
	 * creates a new file
	 * 
	 * @param name
	 *            the name of the file
	 * @param directory
	 *            the directory the file is in
	 */
	public ScannedFile(Path name, Path directory)
	{
		this.name = name;
		this.directory = directory;
	}

	/**
	 * @return the name of the file
	 */
	public Path getName()
	{
		return name;
	}

	/**
	 * @return the directory the file is in
	 */
	public Path getDirectory()
	{
		return directory;
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
		Path myPath = directory.resolve(name.toString());
		if (obj instanceof Path)
		{
			return myPath.equals(obj);
		}
		if (obj instanceof ScannedFile)
		{
			ScannedFile other = (ScannedFile) obj;
			return myPath.equals(other.getDirectory().resolve(other.getName().toString()));
		}
		return false;
	}
}
