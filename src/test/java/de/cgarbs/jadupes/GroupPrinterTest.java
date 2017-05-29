/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
		List<ScannedFile> emptyList = Collections.emptyList();

		// when
		printer_underTest.printGroup(emptyList);

		// then
		assertThat(recorder.getLinesLeft(), is(0));
	}

	@Test
	public void filenamesArePrintedFromList()
	{
		// given
		ScannedFile file1 = new FakeScannedFile("file_1");
		ScannedFile file2 = new FakeScannedFile("file_2");
		List<ScannedFile> fileGroup = Arrays.asList(file1, file2);

		// when
		printer_underTest.printGroup(fileGroup);

		// then
		assertThat(recorder.getLinesLeft(), is(2));
		assertThat(recorder.getNextLine(), is("file_1"));
		assertThat(recorder.getNextLine(), is("file_2"));
	}

	@Test
	public void multipleGroupsAreSeparatedByAnEmptyLine()
	{
		// given
		List<ScannedFile> fileGroup1 = Collections.singletonList(new FakeScannedFile("file_1"));
		List<ScannedFile> fileGroup2 = Collections.singletonList(new FakeScannedFile("file_2"));

		// when
		printer_underTest.printGroup(fileGroup1);
		printer_underTest.printGroup(fileGroup2);

		// then
		assertThat(recorder.getLinesLeft(), is(3));
		assertThat(recorder.getNextLine(), is("file_1"));
		assertThat(recorder.getNextLine(), is(""));
		assertThat(recorder.getNextLine(), is("file_2"));
	}

	private class FakeScannedFile extends ScannedFile
	{

		public FakeScannedFile(String filename)
		{
			super(Paths.get(filename), 0);
		}

	}
}
