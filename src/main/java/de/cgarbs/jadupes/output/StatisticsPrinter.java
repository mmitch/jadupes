/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.output;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import de.cgarbs.jadupes.data.ScannedFile;
import de.cgarbs.jadupes.filter.BucketList;
import de.cgarbs.jadupes.test.VisibleForTesting;

/**
 * Prints statistics about groups of files.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class StatisticsPrinter implements OutputAction
{
	private BigInteger totalGroupCount = BigInteger.ZERO;
	private BigInteger totalFileCount = BigInteger.ZERO;
	private BigInteger totalFileSize = BigInteger.ZERO;
	private BigInteger totalDeduplicationCount = BigInteger.ZERO;
	private BigInteger totalDeduplicationSize = BigInteger.ZERO;

	@VisibleForTesting
	PrintStream output = System.out;

	@Override
	public void processGroup(List<ScannedFile> files)
	{
		// all files of a group have the same size
		BigInteger fileSize = BigInteger.valueOf(files.get(0).getSize());
		BigInteger fileCount = BigInteger.valueOf(files.size());

		Collection<List<ScannedFile>> filesByFileKey = BucketList.create(files, ScannedFile::getFileKey).getBuckets();
		BigInteger duplicateCount = BigInteger.valueOf(filesByFileKey.size() - 1);

		totalGroupCount = totalGroupCount.add(BigInteger.ONE);
		totalFileCount = totalFileCount.add(fileCount);
		totalFileSize = totalFileSize.add(fileCount.multiply(fileSize));
		totalDeduplicationCount = totalDeduplicationCount.add(duplicateCount);
		totalDeduplicationSize = totalDeduplicationSize.add(duplicateCount.multiply(fileSize));
	}

	@Override
	public void finalizeOutput()
	{
		printfln("%s duplicate file groups", totalGroupCount);
		printfln("%s duplicate files using %s bytes", totalFileCount, totalFileSize);
		printfln("%s files using %s bytes to be deduplicated", totalDeduplicationCount, totalDeduplicationSize);
	}

	private void printfln(String format, Object... args)
	{
		output.println(String.format(format, args));
	}
}
