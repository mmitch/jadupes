/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import java.util.List;

import de.cgarbs.jadupes.data.ScannedFile;

/**
 * An Action that acts on the final groups of duplicate files.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public interface OutputAction
{
	/**
	 * processes a single group of duplicate files
	 * 
	 * @param files
	 *            the files in the group
	 */
	public void processGroup(List<ScannedFile> files);

	/**
	 * finalizes the output after all groups have been processed
	 */
	public void finalizeOutput();
}
