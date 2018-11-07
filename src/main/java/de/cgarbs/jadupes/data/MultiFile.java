/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import static java.util.stream.Collectors.toList;

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
		hardlinks.add(path);
	}

	private MultiFile(List<Path> paths, long size, Object fileKey, int nlink, long device)
	{
		super(size, fileKey, nlink, device);
		hardlinks.addAll(paths);
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
	 * Merge the names (hardlinks) {@link MultiFile}s that refer to the same
	 * file into a new {@link MultiFile}.
	 * 
	 * @param MultiFileA
	 *            the one {@link MultiFile}
	 * @param MultiFileB
	 *            the other {@link MultiFile}
	 * @return new {@link MultiFile} with the merged names (hardlinks) from both
	 *         sources
	 */
	public static MultiFile merge(MultiFile MultiFileA, MultiFile MultiFileB)
	{
		assert (MultiFileA.getSize() == MultiFileB.getSize());
		assert (MultiFileA.getFileKey().equals(MultiFileB.getFileKey()));
		assert (MultiFileA.getHardlinkCount() == MultiFileB.getHardlinkCount());
		assert (MultiFileA.getDevice() == MultiFileB.getDevice());

		List<Path> combinedPaths = Stream.concat(MultiFileA.getNames(), MultiFileB.getNames()).collect(toList());
		return new MultiFile(combinedPaths, MultiFileA.getSize(), MultiFileA.getFileKey(), MultiFileA.getHardlinkCount(), MultiFileA.getDevice());
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

}
