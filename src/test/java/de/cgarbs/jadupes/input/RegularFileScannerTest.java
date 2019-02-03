/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.input;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static de.cgarbs.jadupes.test.FileHelper.createSubdirectory;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.cgarbs.jadupes.data.Directory;
import de.cgarbs.jadupes.data.StoredData;
import de.cgarbs.jadupes.data.UniquelyNamedFile;

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
	public void scanFindsNoFilesInEmptyDirectory()
	{
		// given

		// when
		sut.scan(tempDir);

		// then
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
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
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
		assertThat(result, hasSize(1));
		assertSameFile(result.get(0), file1);
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
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
		assertThat(result, hasSize(1));
		assertSameFile(result.get(0), file1);
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
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
		assertThat(result, hasSize(1));
		assertSameFile(result.get(0), file1);
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
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
		assertThat(result, hasSize(2));
		assertSameFile(result.get(0), file1);
		assertSameFile(result.get(1), file2);
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
		List<UniquelyNamedFile> result = sut.getScannedFiles().collect(toList());
		assertSameFiles(result, file1, file2, file3);
	}

	// FIXME: test for "don't follow symlinks"

	/*
	 * helper methods
	 */

	private static void assertSameFiles(List<UniquelyNamedFile> fileList1, Path... fileList2)
	{
		Object[] fileKeys1 = fileList1.stream() //
				.map(UniquelyNamedFile::getData) //
				.map(StoredData::getFileKey) //
				.toArray();

		Object[] fileKeys2 = Arrays.stream(fileList2) //
				.map(RegularFileScannerTest::getFileKey) //
				.toArray();

		assertThat(fileKeys1, arrayContainingInAnyOrder(fileKeys2));
	}

	private static void assertSameFile(UniquelyNamedFile file1, Path file2) throws IOException
	{
		assertThat(file1.getData().getFileKey(), is(getFileKey(file2)));
	}

	private static Object getFileKey(Path file)
	{
		try
		{
			return Files.readAttributes(file, "unix:fileKey").get("fileKey");
		} catch (IOException e)
		{
			// wrap to RuntimeException because of Stream :-/
			throw new RuntimeException(e);
		}
	}
}
