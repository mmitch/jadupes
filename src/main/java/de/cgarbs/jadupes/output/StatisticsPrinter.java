/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import de.cgarbs.jadupes.data.ScannedFile;

/**
 * Prints statistics about groups of files.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class StatisticsPrinter
{
	// TODO: common superclass for all Printers

	BigInteger totalGroupCount = BigInteger.ZERO;
	BigInteger totalFileCount = BigInteger.ZERO;
	BigInteger totalFileSize = BigInteger.ZERO;
	BigInteger totalDeduplicationCount = BigInteger.ZERO;
	BigInteger totalDeduplicationSize = BigInteger.ZERO;

	PrintStream output = System.out;

	/**
	 * print the statistics summary
	 */
	public void printResult()
	{
		printfln("%s duplicate file groups", totalGroupCount);
		printfln("%s duplicate files using %s bytes", totalFileCount, totalFileSize);
		printfln("%s files using %s bytes to be deduplicated", totalDeduplicationCount, totalDeduplicationSize);
	}

	/**
	 * count a group of files
	 * 
	 * @param files
	 *            the files to count
	 */
	public void registerGroup(List<ScannedFile> files)
	{
		// all files of a group have the same size
		BigInteger fileSize = BigInteger.valueOf(files.get(0).getSize());
		BigInteger fileCount = BigInteger.valueOf(files.size());
		BigInteger duplicateCount = BigInteger.valueOf(files.size() - 1);

		totalGroupCount = totalGroupCount.add(BigInteger.ONE);
		totalFileCount = totalFileCount.add(fileCount);
		totalFileSize = totalFileSize.add(fileCount.multiply(fileSize));
		totalDeduplicationCount = totalDeduplicationCount.add(duplicateCount);
		totalDeduplicationSize = totalDeduplicationSize.add(duplicateCount.multiply(fileSize));
	}

	private void printfln(String format, Object... args)
	{
		output.println(String.format(format, args));
	}

}
