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
 * a file with multiple filenames/hardlinks (but we don't always know
 * <i>all</i> filenames, only those we have scanned)
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class MultiNamedFile extends FileBase
{
	private final List<Path> hardlinks = new ArrayList<>();

	private MultiNamedFile(Path path, long size, Object fileKey, int nlink, long device)
	{
		super(size, fileKey, nlink, device);
		hardlinks.add(path);
	}

	private MultiNamedFile(List<Path> paths, long size, Object fileKey, int nlink, long device)
	{
		super(size, fileKey, nlink, device);
		hardlinks.addAll(paths);
	}

	/**
	 * Creates a new {@link MultiNamedFile} with a single initial name.
	 * 
	 * @param uniquelyNamedFile
	 *            the existing {@link UniquelyNamedFile} to be turned
	 *            (duplicated)
	 *            into a {@link MultiNamedFile}
	 * @return the new {@link UniquelyNamedFile}
	 */
	public static MultiNamedFile of(UniquelyNamedFile uniquelyNamedFile)
	{
		return new MultiNamedFile( //
				uniquelyNamedFile.getName(), //
				uniquelyNamedFile.getSize(), //
				uniquelyNamedFile.getFileKey(), //
				uniquelyNamedFile.getHardlinkCount(), //
				uniquelyNamedFile.getDevice());
	}

	/**
	 * Merge the names (hardlinks) of two {@link MultiNamedFile}s that refer to
	 * the same file into a new {@link MultiNamedFile}.
	 * 
	 * @param multiNamedFileA
	 *            the one {@link MultiNamedFile}
	 * @param multiNamedFileB
	 *            the other {@link MultiNamedFile}
	 * @return new {@link MultiNamedFile} with the merged names (hardlinks) from
	 *         both sources
	 */
	public static MultiNamedFile merge(MultiNamedFile multiNamedFileA, MultiNamedFile multiNamedFileB)
	{
		assert (multiNamedFileA.getSize() == multiNamedFileB.getSize());
		assert (multiNamedFileA.getFileKey().equals(multiNamedFileB.getFileKey()));
		assert (multiNamedFileA.getHardlinkCount() == multiNamedFileB.getHardlinkCount());
		assert (multiNamedFileA.getDevice() == multiNamedFileB.getDevice());

		List<Path> combinedPaths = Stream.concat(multiNamedFileA.getNames(), multiNamedFileB.getNames()).collect(toList());
		return new MultiNamedFile(combinedPaths, multiNamedFileA.getSize(), multiNamedFileA.getFileKey(), multiNamedFileA.getHardlinkCount(), multiNamedFileA.getDevice());
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
