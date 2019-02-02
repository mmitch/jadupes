/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.cgarbs.jadupes.data.Directory;
import de.cgarbs.jadupes.data.UniquelyNamedFile;

/**
 * Scans directories recursively for regular files
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class RegularFileScanner
{
	private final List<UniquelyNamedFile> scannedFiles = new ArrayList<>();

	/**
	 * scan the given directory for regular files
	 * 
	 * @param dir
	 *            the directory to scan
	 */
	public void scan(Directory dir)
	{
		try
		{
			Path startPath = dir.asPath();
			Files.walk(startPath) //
					.filter(Files::isRegularFile) //
					.map(UniquelyNamedFile::of) //
					.forEach(scannedFiles::add);
		} catch (IOException e)
		{
			// convert to RuntimeException because of Stream :-/
			throw new RuntimeException(e);
		}
	}

	/**
	 * returns the previously scanned files
	 * 
	 * @return the scanned files
	 * @see #scan
	 */
	public Stream<UniquelyNamedFile> getScannedFiles()
	{
		return scannedFiles.stream();
	}
}
