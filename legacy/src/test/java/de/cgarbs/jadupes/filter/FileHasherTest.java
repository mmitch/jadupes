/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.cgarbs.jadupes.filter.FileHasher;
import de.cgarbs.jadupes.test.FileHelper;

@SuppressWarnings("javadoc")
public class FileHasherTest
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
	public void twoIdenticalFilesGiveTheSameHash() throws IOException
	{
		// given
		Path file1 = FileHelper.createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createFileWithContent(tempDir, "file2", "FOO");

		// when
		long hash1 = new FileHasher().getHash(file1);
		long hash2 = new FileHasher().getHash(file2);

		// then
		assertThat(hash1, is(hash2));
	}

	@Test
	public void twoDifferentFilesGiveADifferentHash() throws IOException
	{
		// ...unless we hit a collision by any chance ;-)

		// given
		Path file1 = FileHelper.createFileWithContent(tempDir, "file1", "FOO");
		Path file2 = FileHelper.createFileWithContent(tempDir, "file2", "BAR");

		// when
		long hash1 = new FileHasher().getHash(file1);
		long hash2 = new FileHasher().getHash(file2);

		// then
		assertThat(hash1, not(hash2));
	}
}
