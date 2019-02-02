/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import de.cgarbs.jadupes.test.VisibleForTesting;

/**
 * a file with a single filename/hardlink
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class UniquelyNamedFile extends FileBase
{
	private final Path path;

	@VisibleForTesting
	protected UniquelyNamedFile(Path path, long size, Object fileKey)
	{
		this(path, size, fileKey, 1, 0);
	}

	private UniquelyNamedFile(Path path, long size, Object fileKey, int nlink, long device)
	{
		super(size, fileKey, nlink, device);
		this.path = path;
	}

	/**
	 * Creates a new {@link UniquelyNamedFile}. Reads and stores the relevant
	 * file
	 * attributes from the filesystem.
	 * 
	 * @param path
	 *            the Path of the file (directory + filename)
	 * @return the new {@link UniquelyNamedFile}
	 */
	public static UniquelyNamedFile of(Path path)
	{
		try
		{
			// TODO: figure out what happens when unix: is not available and
			// what to do then. Perhaps check once on startup if unix: is
			// available and then always do this or that (perhaps use
			// SingleFile subclasses?)
			Map<String, Object> attributes = Files.readAttributes(path, "unix:size,fileKey,nlink,dev");
			long size = Long.parseLong(attributes.get("size").toString());
			Object fileKey = attributes.get("fileKey");
			int nlink = Integer.parseInt(attributes.get("nlink").toString());
			long device = Long.parseLong(attributes.get("dev").toString());

			return new UniquelyNamedFile(path, size, fileKey, nlink, device);
		} catch (IOException e)
		{
			// Rethrow as unchecked exception because of Stream
			// handling in Java 8. We want to exit anyway.
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the name of this file (directory + filename)
	 */
	public Path getName()
	{
		return path;
	}
}
