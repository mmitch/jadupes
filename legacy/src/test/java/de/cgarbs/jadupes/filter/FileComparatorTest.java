/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.cgarbs.jadupes.filter.FileComparator;
import de.cgarbs.jadupes.test.FileHelper;

@SuppressWarnings("javadoc")
public class FileComparatorTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Path tempDir;

	@Before
	public void setup()
	{
		tempDir = tempFolder.getRoot().toPath();
	}

	@Test
	public void filesWithDifferentLengthAreDifferent() throws IOException
	{
		// given
		Path file1 = FileHelper.createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createFileWithContent(tempDir, "file2", "FOOBAR");

		// when
		boolean result = new FileComparator().haveSameContent(file1, file2);

		// then
		assertThat(result, is(false));
	}

	@Test
	public void filesWithDifferentContentAreDifferent() throws IOException
	{
		// given
		Path file1 = FileHelper.createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createFileWithContent(tempDir, "file2", "BAR");

		// when
		boolean result = new FileComparator().haveSameContent(file1, file2);

		// then
		assertThat(result, is(false));
	}

	@Test
	public void filesWithSameContentAreEqual() throws IOException
	{
		// given
		Path file1 = FileHelper.createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createFileWithContent(tempDir, "file2", "FOO");

		// when
		boolean result = new FileComparator().haveSameContent(file1, file2);

		// then
		assertThat(result, is(true));
	}

	@Test
	public void bigFilesWithDifferentContentAreDifferent() throws IOException
	{
		// this forces multiple blocks to be read, testing the read-loop

		// given
		Path file1 = FileHelper.createBigFileWithEndingContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createBigFileWithEndingContent(tempDir, "file2", "BAR");

		// when
		boolean result = new FileComparator().haveSameContent(file1, file2);

		// then
		assertThat(result, is(false));
	}
}
