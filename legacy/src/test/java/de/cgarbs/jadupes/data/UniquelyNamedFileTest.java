/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

import static de.cgarbs.jadupes.test.FileHelper.createFileWithContent;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("javadoc")
public class UniquelyNamedFileTest
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
		UniquelyNamedFile scannedFile = UniquelyNamedFile.of(somePath);

		// then
		assertThat(scannedFile.getName(), is(somePath));
	}

	@Test
	public void scanningNonExistingFileThrowsRuntimeException()
	{
		// given
		Path madeUpFilename = tempDir.resolve("doesNotExist");

		// when
		exception.expect(RuntimeException.class);
		UniquelyNamedFile.of(madeUpFilename);

		// then
		fail("no exception thrown!");
	}
}
