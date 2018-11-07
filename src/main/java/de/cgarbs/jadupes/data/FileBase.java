/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * a file
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class FileBase
{
	private final long size;
	private final Object fileKey;
	private final int nlink;
	private final long device;

	protected FileBase(long size, Object fileKey, int nlink, long device)
	{
		this.size = size;
		this.fileKey = fileKey;
		this.nlink = nlink;
		this.device = device;
	}

	/**
	 * @return the size of this file
	 */
	public long getSize()
	{
		return size;
	}

	/**
	 * @return the total number of hardlinks (names) this file shares
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

	/**
	 * @return the device/filesystem this file is on
	 */
	public long getDevice()
	{
		return device;
	}
}
