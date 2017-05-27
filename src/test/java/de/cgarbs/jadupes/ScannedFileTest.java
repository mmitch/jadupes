/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ScannedFileTest
{
	@Test
	public void scannedFileDoesNotMatchNull()
	{
		ScannedFile scannedFile = createUnspecifiedScannedFile();
		assertThat(scannedFile.equals(null), is(false));
	}

	@Test
	public void scannedFileMatchesItself()
	{
		ScannedFile scannedFile = createUnspecifiedScannedFile();
		assertThat(scannedFile.equals(scannedFile), is(true));
	}

	@Test
	public void scannedFileMatchesSameScannedFile()
	{
		// given
		ScannedFile scannedFile1 = createUnspecifiedScannedFile();
		ScannedFile scannedFile2 = createUnspecifiedScannedFile();

		// when
		boolean result = scannedFile1.equals(scannedFile2);

		// then
		assertThat(result, is(true));
		assertThat(scannedFile1, not(sameInstance(scannedFile2)));
	}

	@Test
	public void scannedFileDoesNotMatchDifferentScannedFile()
	{
		// given
		ScannedFile scannedFile1 = new ScannedFile(Paths.get("dir1", "name1"));
		ScannedFile scannedFile2 = new ScannedFile(Paths.get("dir2", "name2"));

		// when
		boolean result = scannedFile1.equals(scannedFile2);

		// then
		assertThat(result, is(false));
		assertThat(scannedFile1, not(sameInstance(scannedFile2)));
	}

	@Test
	public void scannedFileMatchesSamePath()
	{
		// given
		Path someFile = Paths.get("dir", "subdir", "filename");

		ScannedFile sut = new ScannedFile(someFile);

		// when
		boolean result = sut.equals(someFile);

		// then
		assertThat(result, is(true));

	}

	@Test
	public void scannedFileDoesNotMatchDifferentPath()
	{
		// given
		Path someFile = Paths.get("dir", "subdir", "filename");
		Path otherFile = Paths.get("dir", "subdir", "different");

		ScannedFile sut = new ScannedFile(someFile);

		// when
		boolean result = sut.equals(otherFile);

		// then
		assertThat(result, is(false));

	}

	private ScannedFile createUnspecifiedScannedFile()
	{
		return new ScannedFile(Paths.get("anyDirectory", "anyFilename"));
	}

}
