/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.cgarbs.jadupes.data.UniquelyNamedFile;
import de.cgarbs.jadupes.test.PrintStreamRecorder;

@SuppressWarnings("javadoc")
public class GroupPrinterTest
{
	private final GroupPrinter printer_underTest = new GroupPrinter();
	private final PrintStreamRecorder recorder = new PrintStreamRecorder();

	@Before
	public void setup()
	{
		printer_underTest.output = recorder;
	}

	@Test
	public void emptyListPrintsNothing()
	{
		// given
		List<UniquelyNamedFile> emptyList = Collections.emptyList();

		// when
		printer_underTest.processGroup(emptyList);

		// then
		assertThat(recorder.getLinesLeft(), is(0));
	}

	@Test
	public void filenamesArePrintedFromList()
	{
		// given
		UniquelyNamedFile file1 = new FakeScannedFile("file_1");
		UniquelyNamedFile file2 = new FakeScannedFile("file_2");
		List<UniquelyNamedFile> fileGroup = Arrays.asList(file1, file2);

		// when
		printer_underTest.processGroup(fileGroup);

		// then
		assertThat(recorder.getLinesLeft(), is(2));
		assertThat(recorder.getNextLine(), is("file_1"));
		assertThat(recorder.getNextLine(), is("file_2"));
	}

	@Test
	public void multipleGroupsAreSeparatedByAnEmptyLine()
	{
		// given
		List<UniquelyNamedFile> fileGroup1 = Collections.singletonList(new FakeScannedFile("file_1"));
		List<UniquelyNamedFile> fileGroup2 = Collections.singletonList(new FakeScannedFile("file_2"));

		// when
		printer_underTest.processGroup(fileGroup1);
		printer_underTest.processGroup(fileGroup2);

		// then
		assertThat(recorder.getLinesLeft(), is(3));
		assertThat(recorder.getNextLine(), is("file_1"));
		assertThat(recorder.getNextLine(), is(""));
		assertThat(recorder.getNextLine(), is("file_2"));
	}

	@Test
	public void withoutFileGroupsFinalizeDoesNothing()
	{
		// given

		// when
		printer_underTest.finalizeOutput();

		// when
		assertThat(recorder.getLinesLeft(), is(0));
	}

	@Test
	public void withFileGroupsFinalizeDoesNothing()
	{
		// given
		UniquelyNamedFile file1 = new FakeScannedFile("file_1");
		UniquelyNamedFile file2 = new FakeScannedFile("file_2");
		List<UniquelyNamedFile> fileGroup = Arrays.asList(file1, file2);
		printer_underTest.processGroup(fileGroup);
		recorder.reset();

		// when
		printer_underTest.finalizeOutput();

		// when
		// one line per file from processGroup(), but nothing else
		assertThat(recorder.getLinesLeft(), is(0));
	}

	private class FakeScannedFile extends UniquelyNamedFile
	{

		public FakeScannedFile(String filename)
		{
			super(Paths.get(filename), 0, filename);
		}

	}
}
