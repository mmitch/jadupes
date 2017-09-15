/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.fs;

import static de.cgarbs.jadupes.test.FileHelper.assertThatFileContains;
import static de.cgarbs.jadupes.test.FileHelper.assertThatSameFile;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.cgarbs.jadupes.file.SafeLinker;
import de.cgarbs.jadupes.test.FileHelper;

@SuppressWarnings("javadoc")
public class SafeLinkerTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private final SafeLinker sut = new SafeLinker();
	private Path tempDir;

	@Before
	public void setup()
	{
		tempDir = tempFolder.getRoot().toPath();
	}

	@Test
	public void simplyCreatesALinkIfLinkFileDoesNotExist() throws IOException
	{
		// given
		Path originalFile = FileHelper.createFileWithContent(tempDir, "a", "aaa");
		Path linkTarget = tempDir.resolve("b");

		// when
		sut.safeLink(linkTarget, originalFile);

		// then
		assertThatSameFile(originalFile, linkTarget);
	}

	@Test
	public void overwritesLinkTargetIfItExists() throws IOException
	{
		// given
		Path originalFile = FileHelper.createFileWithContent(tempDir, "a", "aaa");
		Path linkTarget = FileHelper.createFileWithContent(tempDir, "b", "bbb");

		// when
		sut.safeLink(linkTarget, originalFile);

		// then
		assertThatSameFile(originalFile, linkTarget);
	}

	@Test
	public void linkTargetIsRestoredIfNoHardlinkingIsSupported() throws IOException
	{
		// given
		Path originalFile = FileHelper.createFileWithContent(tempDir, "a", "aaa");
		Path linkTarget = FileHelper.createFileWithContent(tempDir, "b", "bbb");
		SafeLinker brokenLinker = new SafeLinkerWithRuntimeException(() -> new UnsupportedOperationException());

		// when
		brokenLinker.safeLink(linkTarget, originalFile);

		// then
		assertThatFileContains(originalFile, "aaa");
		assertThatFileContains(linkTarget, "bbb");
	}

	@Test
	public void linkTargetIsRestoredIfIOExceptionIsEncountered() throws IOException
	{
		// given
		Path originalFile = FileHelper.createFileWithContent(tempDir, "a", "aaa");
		Path linkTarget = FileHelper.createFileWithContent(tempDir, "b", "bbb");
		SafeLinker brokenLinker = new SafeLinkerWithIOException(() -> new IOException());

		// when
		brokenLinker.safeLink(linkTarget, originalFile);

		// then
		assertThatFileContains(originalFile, "aaa");
		assertThatFileContains(linkTarget, "bbb");
	}

	@Test
	public void testNonExistingLinkTargetWithException() throws IOException
	{
		// given
		Path originalFile = FileHelper.createFileWithContent(tempDir, "a", "aaa");
		Path linkTarget = tempDir.resolve("b");
		SafeLinker brokenLinker = new SafeLinkerWithIOException(() -> new IOException());

		// when
		brokenLinker.safeLink(linkTarget, originalFile);

		// then
		assertThatFileContains(originalFile, "aaa");
		assertThat(Files.exists(linkTarget), is(false));
	}

	private class SafeLinkerWithIOException extends SafeLinker
	{
		private final Supplier<IOException> ioException;

		private SafeLinkerWithIOException(Supplier<IOException> ioException)
		{
			this.ioException = ioException;
		}

		protected Path createLink(Path link, Path existing) throws IOException
		{
			throw ioException.get();
		}

	}

	private class SafeLinkerWithRuntimeException extends SafeLinker
	{
		private final Supplier<RuntimeException> runtimeException;

		private SafeLinkerWithRuntimeException(Supplier<RuntimeException> runtimeException)
		{
			this.runtimeException = runtimeException;
		}

		protected Path createLink(Path link, Path existing) throws IOException
		{
			throw runtimeException.get();
		}

	}
}
