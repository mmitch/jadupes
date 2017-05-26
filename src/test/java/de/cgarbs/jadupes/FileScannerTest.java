/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("javadoc")
public class FileScannerTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private FileScanner sut = new FileScanner();

	@Test
	public void scanFindsNoFilesInEmptyDirectory()
	{
		// given
		String emptyDirectory = tempFolder.getRoot().getAbsolutePath();

		// when
		List<ScannedFile> result = sut.scan(emptyDirectory);

		// then
		assertThat(result, empty());
	}
}
