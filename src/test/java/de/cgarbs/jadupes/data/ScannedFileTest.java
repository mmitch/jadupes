/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
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
public class ScannedFileTest
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
		ScannedFile scannedFile = new ScannedFile(somePath);

		// then
		assertThat(scannedFile.getFile(), is(somePath));
	}

	@Test
	public void filesizeIsReturned() throws IOException
	{
		// given
		Path file = createFileWithContent(tempDir, "file", "some content");

		// when
		ScannedFile scannedFile = new ScannedFile(file);

		// then
		assertThat(scannedFile.getSize(), is(Files.size(file)));
	}

	@Test
	public void scannedFileDoesNotMatchNull() throws IOException
	{
		ScannedFile scannedFile = createUnspecifiedScannedFile();
		assertThat(scannedFile.equals(null), is(false));
	}

	@Test
	public void scannedFileMatchesItself() throws IOException
	{
		ScannedFile scannedFile = createUnspecifiedScannedFile();
		assertThat(scannedFile.equals(scannedFile), is(true));
	}

	@Test
	public void scannedFileDoesNotMatchCompletelyDifferentObject() throws IOException
	{
		ScannedFile scannedFile = createUnspecifiedScannedFile();
		assertThat(scannedFile.equals("somePlainString"), is(false));
	}

	@Test
	public void scannedFileMatchesSameScannedFile() throws IOException
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
	public void scannedFileDoesNotMatchDifferentScannedFile() throws IOException
	{
		// given
		ScannedFile scannedFile1 = new ScannedFile(createFileWithContent(tempDir, "name1", "FOO"));
		ScannedFile scannedFile2 = new ScannedFile(createFileWithContent(tempDir, "name2", "BAR"));

		// when
		boolean result = scannedFile1.equals(scannedFile2);

		// then
		assertThat(result, is(false));
		assertThat(scannedFile1, not(sameInstance(scannedFile2)));
	}

	@Test
	public void scannedFileMatchesSamePath() throws IOException
	{
		// given
		Path someFile = createFileWithContent(tempDir, "filename", "FOO");

		ScannedFile sut = new ScannedFile(someFile);

		// when
		boolean result = sut.equals(someFile);

		// then
		assertThat(result, is(true));

	}

	@Test
	public void scannedFileDoesNotMatchDifferentPath() throws IOException
	{
		// given
		Path someFile = createFileWithContent(tempDir, "filename", "FOO");
		Path otherFile = createFileWithContent(tempDir, "othername", "BAR");

		ScannedFile sut = new ScannedFile(someFile);

		// when
		boolean result = sut.equals(otherFile);

		// then
		assertThat(result, is(false));

	}

	@Test
	public void scanningNonExistingFileThrowsRuntimeException()
	{
		// given
		Path madeUpFilename = tempDir.resolve("doesNotExist");

		// when
		exception.expect(RuntimeException.class);
		new ScannedFile(madeUpFilename);

		// then
		fail("no exception thrown!");
	}

	@Test
	public void filesWithSameContentYieldDifferentFileKey() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "BAR");

		ScannedFile scannedFile1 = new ScannedFile(file1);
		ScannedFile scannedFile2 = new ScannedFile(file2);

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

		ScannedFile scannedFile1 = new ScannedFile(file1);
		ScannedFile scannedFile2 = new ScannedFile(file2);

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
		ScannedFile scannedFile = new ScannedFile(file);

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
		ScannedFile scannedFile = new ScannedFile(file);

		// then
		assertThat(scannedFile.getHardlinkCount(), is(2));
	}

	private ScannedFile createUnspecifiedScannedFile() throws IOException
	{
		return new ScannedFile(createFileWithContent(tempDir, "file", "some content"));
	}

}
