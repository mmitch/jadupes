/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import de.cgarbs.jadupes.test.VisibleForTesting;

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
	private final Object fileKey;
	// TODO: add "unix:device" attribute to use for BucketList grouping

	@VisibleForTesting
	protected ScannedFile(Path file, long size, Object fileKey)
	{
		this.file = file;
		this.size = size;
		this.fileKey = fileKey;
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
			Map<String, Object> attributes = Files.readAttributes(file, "size,fileKey");
			this.size = Long.parseLong(attributes.get("size").toString());
			this.fileKey = attributes.get("fileKey");
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

	/**
	 * A file key is something unique to every file except for hardlinked files
	 * or the like.
	 * Under POSIX, it's something like a <code>device:inode</code> pair.
	 * 
	 * @return the file key of this file
	 * 
	 * @see BasicFileAttributes#fileKey()
	 */
	public Object getFileKey()
	{
		return fileKey;
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
