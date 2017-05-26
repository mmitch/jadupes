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
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
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
	private File tempDir;

	@Before
	public void setup()
	{
		tempDir = tempFolder.getRoot();
	}

	@Test
	public void scanFindsNoFilesInEmptyDirectory() throws IOException
	{
		// given
		String emptyDirectory = tempDir.getAbsolutePath();

		// when
		List<ScannedFile> result = sut.scan(emptyDirectory);

		// then
		assertThat(result, empty());
	}

	@Test
	public void scanFindsSingleRegularFile() throws IOException
	{
		// given
		createFileWithContent(tempDir, "file1", "FOO");
		String directory = tempFolder.getRoot().getAbsolutePath();

		// when
		List<ScannedFile> result = sut.scan(directory);

		// then
		assertThat(result, hasSize(1));
		ScannedFile scannedFile = result.get(0);
		assertThat(scannedFile.getName().toString(), is("file1"));
		assertThat(scannedFile.getDirectory().toString(), is(directory));
	}

	private void createFileWithContent(File dir, String name, String content) throws IOException
	{
		Path path = FileSystems.getDefault().getPath(dir.getAbsolutePath(), name);
		BufferedWriter writer = Files.newBufferedWriter(path);
		writer.write(content);
		writer.close();
	}

	// TODO: don't follow symlinks
}
