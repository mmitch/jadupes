/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("javadoc")
public class FileScannerTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private final FileScanner sut = new FileScanner();
	private Path tempDir;

	@Before
	public void setup()
	{
		tempDir = tempFolder.getRoot().toPath();
	}

	@Test
	public void scanFindsNoFilesInEmptyDirectory() throws IOException
	{
		// given

		// when
		List<ScannedFile> result = sut.scan(tempDir.toString());

		// then
		assertThat(result, empty());
	}

	@Test
	public void scanFindsSingleRegularFile() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");

		// when
		List<ScannedFile> result = sut.scan(tempDir.toString());

		// then
		assertThat(result, hasSize(1));
		ScannedFile scannedFile = result.get(0);
		assertThat(scannedFile.getName(), is(file1.getFileName()));
		assertThat(scannedFile.getDirectory(), is(file1.getParent()));
	}

	@Test
	public void scanFindsFileInSubdirectory() throws IOException
	{
		// given
		Path subDir = createSubdirectory("subdir");
		Path file1 = createFileWithContent(subDir, "file1", "FOO");

		// when
		List<ScannedFile> result = sut.scan(tempDir.toString());

		// then
		assertThat(result, hasSize(1));
		ScannedFile scannedFile = result.get(0);
		assertThat(scannedFile.getName(), is(file1.getFileName()));
		assertThat(scannedFile.getDirectory(), is(subDir));
	}

	private Path createSubdirectory(String subdir) throws IOException
	{
		return Files.createDirectories(tempDir.resolve(subdir));
	}

	private Path createFileWithContent(Path directory, String filename, String content) throws IOException
	{
		Path file = directory.resolve(filename);
		BufferedWriter writer = Files.newBufferedWriter(file);
		writer.write(content);
		writer.close();
		return file;
	}

	// TODO: don't follow symlinks
}
