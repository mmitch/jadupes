/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.cgarbs.jadupes.test.VisibleForTesting;

/**
 * a single file
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class ScannedFile
{
	private final List<Path> hardlinks = new ArrayList<>();
	private final long size;
	private final Object fileKey;
	private final int nlink;
	private final long device;

	@VisibleForTesting
	protected ScannedFile(Path path, long size, Object fileKey)
	{
		this.size = size;
		this.fileKey = fileKey;
		this.nlink = 1;
		this.device = 0;

		addHardlink(path);
	}

	/**
	 * creates a new scanned file
	 * 
	 * @param path
	 *            the Path of the file (directory + filename)
	 */
	public ScannedFile(Path path)
	{
		try
		{
			// TODO: figure out what happens when unix: is not available and
			// what to do then. Perhaps check once on startup if unix: is
			// available and then always do this or that (perhaps use
			// SingleFile subclasses?)
			Map<String, Object> attributes = Files.readAttributes(path, "unix:size,fileKey,nlink,dev");
			this.size = Long.parseLong(attributes.get("size").toString());
			this.fileKey = attributes.get("fileKey");
			this.nlink = Integer.parseInt(attributes.get("nlink").toString());
			this.device = Long.parseLong(attributes.get("dev").toString());

			addHardlink(path);
		} catch (IOException e)
		{
			// Rethrow as unchecked exception because of Stream
			// handling in Java 8. We want to exit anyway.
			throw new RuntimeException(e);
		}
	}

	/**
	 * A file with multiple hardlinks has multiple names.
	 * Only the names from scanned directories are known, so {@link #getNames()}
	 * might contain fewer results than {@link #getHardlinkCount()}.
	 * 
	 * @return the known names of this file (directory + filename)
	 */
	public Stream<Path> getNames()
	{
		return hardlinks.stream();
	}

	/**
	 * @return the size of this file
	 */
	public long getSize()
	{
		return size;
	}

	/**
	 * Only the names from scanned directories are known, so
	 * {@link #getHardlinkCount()}
	 * might give a bigger result than {@link #getNames()}.
	 * 
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

	private void addHardlink(Path path)
	{
		hardlinks.add(path);
	}

}
