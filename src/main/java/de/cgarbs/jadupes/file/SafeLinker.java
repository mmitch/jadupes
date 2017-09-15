/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import de.cgarbs.jadupes.test.VisibleForTesting;

/**
 * Safely one file with a hardlink to another file.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class SafeLinker
{
	/**
	 * Safely hardlinks a file to another filename. If the target filename
	 * exists, it is replaced with a hardlink. If something goes wrong, a
	 * recovery of the replaced file is attempted before an exception is raised.
	 * 
	 * @param link
	 *            the hardlink target (replaced if existing, otherwise created)
	 * @param existing
	 *            the original file (left untouched)
	 * 
	 * @throws IOException
	 *             something went wrong
	 */
	public void safeLink(Path link, Path existing) throws IOException
	{
		Path backupFile = null;

		if (Files.exists(link))
		{
			backupFile = backupFile(link);
		}

		try
		{
			createLink(link, existing);
		} catch (UnsupportedOperationException | IOException e)
		{
			restoreFile(backupFile, link);
		}
	}

	private void restoreFile(Path source, Path target) throws IOException
	{
		if (source != null)
		{
			Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
		}
	}

	private Path backupFile(Path source) throws IOException
	{
		Path target = source.resolveSibling(source.getFileName().toString().concat("~~~jdupes"));
		Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
		return target;
	}

	@VisibleForTesting
	protected Path createLink(Path link, Path existing) throws IOException
	{
		return Files.createLink(link, existing);
	}

}
