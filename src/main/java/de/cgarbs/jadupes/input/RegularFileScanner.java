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

import de.cgarbs.jadupes.data.Directory;
import de.cgarbs.jadupes.data.SingleFile;

/**
 * Scans directories recursively for regular files
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class RegularFileScanner
{
	private final List<SingleFile> scannedFiles = new ArrayList<>();

	/**
	 * scan the given directory for regular files
	 * 
	 * @param dir
	 *            the directory to scan
	 * @throws IOException
	 *             any IO error
	 */
	public void scan(Directory dir) throws IOException
	{
		Path startPath = dir.asPath();
		Files.walk(startPath) //
				.filter(Files::isRegularFile) //
				.map(SingleFile::new) //
				.forEach(scannedFiles::add);
	}

	/**
	 * returns the previously scanned files
	 * 
	 * @return the scanned files
	 * @see #scan
	 */
	public List<SingleFile> getScannedFiles()
	{
		return scannedFiles;
	}
}
