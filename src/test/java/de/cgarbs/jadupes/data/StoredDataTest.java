/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("javadoc")
public class StoredDataTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private Path tempDir;

	@Before
	public void setup()
	{
		tempDir = tempFolder.getRoot().toPath();
	}

	@Test
	public void filesizeIsReturned() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "some content");

		// when
		StoredData storedData = UniquelyNamedFile.of(file).getData();

		// then
		assertThat(storedData.getSize(), is(Files.size(file)));
	}

	@Test
	public void filesWithSameContentYieldDifferentFileKey() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "FOO");

		StoredData storedData1 = UniquelyNamedFile.of(file1).getData();
		StoredData storedData2 = UniquelyNamedFile.of(file2).getData();

		// when
		Object fileKey1 = storedData1.getFileKey();
		Object fileKey2 = storedData2.getFileKey();

		// then
		assertThat(fileKey1, not(fileKey2));
	}

	@Test
	public void hardlinkedFilesYieldSameFileKey() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = Files.createLink(tempDir.resolve("file2"), file1);

		StoredData storedData1 = UniquelyNamedFile.of(file1).getData();
		StoredData storedData2 = UniquelyNamedFile.of(file2).getData();

		// when
		Object fileKey1 = storedData1.getFileKey();
		Object fileKey2 = storedData2.getFileKey();

		// then
		assertThat(fileKey1, is(fileKey2));
	}

	@Test
	public void newFileLinkCountIsOne() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "FOO");

		// then
		StoredData storedData = UniquelyNamedFile.of(file).getData();

		// then
		assertThat(storedData.getHardlinkCount(), is(1));
	}

	@Test
	public void hardlinkedFileLinkCountIsTwo() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "FOO");
		Files.createLink(tempDir.resolve("hardlink"), file);

		// then
		StoredData storedData = UniquelyNamedFile.of(file).getData();

		// then
		assertThat(storedData.getHardlinkCount(), is(2));
	}

	@Test
	public void twoNewFilesInTheSameDirectoryAreOnTheSameDevice() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "BAR");

		// then
		StoredData storedData1 = UniquelyNamedFile.of(file1).getData();
		StoredData storedData2 = UniquelyNamedFile.of(file2).getData();

		// then
		assertThat(storedData1.getDevice(), is(storedData2.getDevice()));
	}

}
