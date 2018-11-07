/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * a file with a possibly multiple known filename (hardlinks)
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class MultiFile extends FileBase
{
	private final List<Path> hardlinks = new ArrayList<>();

	private MultiFile(Path path, long size, Object fileKey, int nlink, long device)
	{
		super(size, fileKey, nlink, device);

		addHardlink(path);
	}

	/**
	 * Creates a new {@link MultiFile} with a single initial name.
	 * 
	 * @param singleFile
	 *            this existing {@link SingleFile} to be turned (copied) into a
	 *            {@link MultiFile}
	 * @return the new {@link SingleFile}
	 */
	public static MultiFile of(SingleFile singleFile)
	{
		return new MultiFile( //
				singleFile.getName(), //
				singleFile.getSize(), //
				singleFile.getFileKey(), //
				singleFile.getHardlinkCount(), //
				singleFile.getDevice());
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

	private void addHardlink(Path path)
	{
		hardlinks.add(path);
	}

}
