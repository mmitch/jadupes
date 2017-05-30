/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.cgarbs.jadupes.data.ScannedFile;
import de.cgarbs.jadupes.test.PrintStreamRecorder;

@SuppressWarnings("javadoc")
public class StatisticsPrinterTest
{
	private final StatisticsPrinter printer_underTest = new StatisticsPrinter();
	private final PrintStreamRecorder recorder = new PrintStreamRecorder();

	@Before
	public void setup()
	{
		printer_underTest.output = recorder;
	}

	@Test
	public void resultWithoutGroupsPrintsEmptyStatistics()
	{
		// given

		// when
		printer_underTest.printResult();

		// then
		assertThat(recorder.getLinesLeft(), is(3));
		assertThat(recorder.getNextLine(), is("0 duplicate file groups"));
		assertThat(recorder.getNextLine(), is("0 duplicate files using 0 bytes"));
		assertThat(recorder.getNextLine(), is("0 files using 0 bytes to be deduplicated"));
	}

	@Test
	public void statisticsForOneGroupWithTwoFiles()
	{
		// given
		List<ScannedFile> group = Arrays.asList(new FakeScannedFile(100), new FakeScannedFile(100));
		printer_underTest.registerGroup(group);

		// when
		printer_underTest.printResult();

		// then
		assertThat(recorder.getLinesLeft(), is(3));
		assertThat(recorder.getNextLine(), is("1 duplicate file groups"));
		assertThat(recorder.getNextLine(), is("2 duplicate files using 200 bytes"));
		assertThat(recorder.getNextLine(), is("1 files using 100 bytes to be deduplicated"));
	}

	@Test
	public void statisticsForTwoGroupsWithFiveFiles()
	{
		// given
		List<ScannedFile> group1 = Arrays.asList(new FakeScannedFile(100), new FakeScannedFile(100), new FakeScannedFile(100));
		List<ScannedFile> group2 = Arrays.asList(new FakeScannedFile(2000), new FakeScannedFile(2000));
		printer_underTest.registerGroup(group1);
		printer_underTest.registerGroup(group2);

		// when
		printer_underTest.printResult();

		// then
		assertThat(recorder.getLinesLeft(), is(3));
		assertThat(recorder.getNextLine(), is("2 duplicate file groups"));
		assertThat(recorder.getNextLine(), is("5 duplicate files using 4300 bytes"));
		assertThat(recorder.getNextLine(), is("3 files using 2200 bytes to be deduplicated"));
	}

	private class FakeScannedFile extends ScannedFile
	{
		public FakeScannedFile(long size)
		{
			super(Paths.get(""), size);
		}
	}
}
