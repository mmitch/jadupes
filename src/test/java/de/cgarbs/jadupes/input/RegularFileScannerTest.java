/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.input;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static de.cgarbs.jadupes.test.FileHelper.createSubdirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.cgarbs.jadupes.data.Directory;
import de.cgarbs.jadupes.data.ScannedFile;

@SuppressWarnings("javadoc")
public class RegularFileScannerTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private final RegularFileScanner sut = new RegularFileScanner();
	private Directory tempDir;

	@Before
	public void setup()
	{
		tempDir = new Directory(tempFolder.getRoot().toPath());
	}

	@Test
	public void scanFindsNoFilesInEmptyDirectory() throws IOException
	{
		// given

		// when
		sut.scan(tempDir);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result, empty());
	}

	@Test
	public void scanFindsSingleRegularFile() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");

		// when
		sut.scan(tempDir);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result, hasSize(1));
		assertThat(result.get(0).equals(file1), is(true));
	}

	@Test
	public void scanFindsFileInSubdirectory() throws IOException
	{
		// given
		Directory subDir = createSubdirectory(tempDir, "subdir");
		Path file1 = createFileWithContent(subDir, "file1", "FOO");

		// when
		sut.scan(tempDir);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result, hasSize(1));
		assertThat(result.get(0).equals(file1), is(true));
	}

	@Test
	public void scanFindsFileInDeeperSubdirectory() throws IOException
	{
		// given
		Directory subDir = createSubdirectory(tempDir, "subdirA");
		Directory subSubDir = createSubdirectory(subDir, "subdirB");
		Path file1 = createFileWithContent(subSubDir, "file1", "FOO");

		// when
		sut.scan(tempDir);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result, hasSize(1));
		assertThat(result.get(0).equals(file1), is(true));
	}

	@Test
	public void multipleScanResultsAccumulate() throws IOException
	{
		// given
		Directory subDirA = createSubdirectory(tempDir, "subdirA");
		Directory subDirB = createSubdirectory(tempDir, "subdirB");
		Path file1 = createFileWithContent(subDirA, "file1", "FOO");
		Path file2 = createFileWithContent(subDirB, "file2", "FOO");

		// when
		sut.scan(subDirA);
		sut.scan(subDirB);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result, hasSize(2));
		assertThat(result.get(0).equals(file1), is(true));
		assertThat(result.get(1).equals(file2), is(true));
	}

	@Test
	public void scanFindsMultipleFiles() throws IOException
	{
		// given
		Path file1 = createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = createFileWithContent(tempDir, "file2", "FOO");
		Path file3 = createFileWithContent(tempDir, "file3", "FOO");

		// when
		sut.scan(tempDir);

		// then
		List<ScannedFile> result = sut.getScannedFiles();
		assertThat(result.toArray(), arrayContainingInAnyOrder(file1, file2, file3));
	}

	// TODO: don't follow symlinks
}
