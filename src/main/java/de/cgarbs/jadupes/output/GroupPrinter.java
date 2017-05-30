/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import de.cgarbs.jadupes.data.ScannedFile;

/**
 * Printer for groups of files
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class GroupPrinter implements OutputAction
{
	PrintStream output = System.out;

	private boolean firstGroup = true;

	@Override
	public void processGroup(List<ScannedFile> files)
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

	@Override
	public void finalizeOutput()
	{
		// not needed
	}

}
