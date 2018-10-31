/*
 * Copyright 2018 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * A directory (relative or absolute) in the filesystem.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 */
public class Directory
{
	private final Path directory;

	/**
	 * creates a new directory
	 * 
	 * @param directory
	 *            String representation of the directory
	 */
	public Directory(String directory)
	{
		this.directory = FileSystems.getDefault().getPath(directory);
	}

	/**
	 * creates a new directory
	 * 
	 * @param directory
	 *            Path representation of the directory
	 */
	public Directory(Path directory)
	{
		this.directory = directory;
	}

	/**
	 * @return this directory as a {@link Path}
	 */
	public Path asPath()
	{
		return directory;
	}
}
