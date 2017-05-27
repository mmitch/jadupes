/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scans directories for relevant files
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class FileScanner
{
	/**
	 * scan the given directory for relevant files
	 * 
	 * @param dir
	 *            the directory to scan
	 * @return the found files
	 * @throws IOException
	 *             any IO error
	 */
	public List<ScannedFile> scan(String dir) throws IOException
	{
		Path startPath = FileSystems.getDefault().getPath(dir);
		return Files.walk(startPath) //
				.filter(Files::isRegularFile) //
				.map(ScannedFile::new) //
				.collect(Collectors.toList());
	}

}
