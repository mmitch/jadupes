/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Printer for groups of files
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class GroupPrinter
{
	PrintStream output = System.out;

	private boolean firstGroup = true;

	/**
	 * prints the names of a group of files
	 * 
	 * @param files
	 *            the files to print
	 */
	public void printGroup(List<ScannedFile> files)
	{
		if (firstGroup)
		{
			firstGroup = false;
		}
		else
		{
			output.println("");
		}

		files.stream() //
				.map(ScannedFile::getFile) //
				.map(Path::toString) //
				.forEach(output::println);
	}

}
