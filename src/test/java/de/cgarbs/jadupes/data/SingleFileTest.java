/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("javadoc")
public class SingleFileTest
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
	public void givenPathIsReturned() throws IOException
	{
		// given
		Path somePath = createFileWithContent(tempDir, "filename", "FOO");

		// when
		SingleFile scannedFile = new SingleFile(somePath);

		// then
		assertThat(scannedFile.getNames().findFirst().get(), is(somePath));
	}

	@Test
	public void filesizeIsReturned() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "some content");

		// when
		SingleFile scannedFile = new SingleFile(file);

		// then
		assertThat(scannedFile.getSize(), is(Files.size(file)));
	}

	@Test
	public void scanningNonExistingFileThrowsRuntimeException()
	{
		// given
		Path madeUpFilename = tempDir.resolve("doesNotExist");

		// when
		exception.expect(RuntimeException.class);
		new SingleFile(madeUpFilename);

		// then
		fail("no exception thrown!");
	}

	@Test
	public void filesWithSameContentYieldDifferentFileKey() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "BAR");

		SingleFile scannedFile1 = new SingleFile(file1);
		SingleFile scannedFile2 = new SingleFile(file2);

		// when
		Object fileKey1 = scannedFile1.getFileKey();
		Object fileKey2 = scannedFile2.getFileKey();

		// then
		assertThat(fileKey1, not(fileKey2));
	}

	@Test
	public void hardlinkedFilesYieldDifferentFileKey() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = Files.createLink(tempDir.resolve("file2"), file1);

		SingleFile scannedFile1 = new SingleFile(file1);
		SingleFile scannedFile2 = new SingleFile(file2);

		// when
		Object fileKey1 = scannedFile1.getFileKey();
		Object fileKey2 = scannedFile2.getFileKey();

		// then
		assertThat(fileKey1, is(fileKey2));
	}

	@Test
	public void newFileLinkCountIsOne() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "FOO");

		// then
		SingleFile scannedFile = new SingleFile(file);

		// then
		assertThat(scannedFile.getHardlinkCount(), is(1));
	}

	@Test
	public void hardlinkedFileLinkCountIsTwo() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "FOO");
		Files.createLink(tempDir.resolve("hardlink"), file);

		// then
		SingleFile scannedFile = new SingleFile(file);

		// then
		assertThat(scannedFile.getHardlinkCount(), is(2));
	}

	@Test
	public void twoNewFilesInTheSameDirectoryAreOnTheSameDevice() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "FOO");

		// then
		SingleFile scannedFile1 = new SingleFile(file1);
		SingleFile scannedFile2 = new SingleFile(file2);

		// then
		assertThat(scannedFile1.getDevice(), is(scannedFile2.getDevice()));
	}

}
