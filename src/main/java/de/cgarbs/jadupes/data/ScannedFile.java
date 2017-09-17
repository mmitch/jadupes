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
	private final int nlink;
	// TODO: add "unix:device" attribute to use for BucketList grouping

	@VisibleForTesting
	protected ScannedFile(Path file, long size, Object fileKey)
	{
		this.file = file;
		this.size = size;
		this.fileKey = fileKey;
		this.nlink = 1;
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
			// TODO: figure out what happens when unix: is not available and
			// what to do then. Perhaps check once on startup if unix: is
			// available and then always do this or that (perhaps use
			// ScannedFile subclasses?)
			Map<String, Object> attributes = Files.readAttributes(file, "unix:size,fileKey,nlink");
			this.size = Long.parseLong(attributes.get("size").toString());
			this.fileKey = attributes.get("fileKey");
			this.nlink = Integer.parseInt(attributes.get("nlink").toString());
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
	 * @return the number of hardlinks this file shares
	 */
	public int getHardlinkCount()
	{
		return nlink;
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
