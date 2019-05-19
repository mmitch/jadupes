/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * StoredData represents a uniquely identified stream of data stored in a
 * filesystem.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class StoredData
{
	private final long size;
	private final Object fileKey;
	private final int nlink;
	private final long device;

	protected StoredData(long size, Object fileKey, int nlink, long device)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (device ^ (device >>> 32));
		result = prime * result + ((fileKey == null) ? 0 : fileKey.hashCode());
		result = prime * result + nlink;
		result = prime * result + (int) (size ^ (size >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoredData other = (StoredData) obj;
		if (device != other.device)
			return false;
		if (fileKey == null)
		{
			if (other.fileKey != null)
				return false;
		}
		else if (!fileKey.equals(other.fileKey))
			return false;
		if (nlink != other.nlink)
			return false;
		if (size != other.size)
			return false;
		return true;
	}
}
