/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.test;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A PrintStream that records and replays what is written into it.
 * 
 * <b>beware:</b> If any output shows out on System.err, then not
 * all relevant methods are overwritten!
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class PrintStreamRecorder extends PrintStream
{
	private final Deque<String> recordedLines = new ArrayDeque<>();

	/**
	 * Creates a PrintStreamRecorder.
	 */
	public PrintStreamRecorder()
	{
		super(System.err);
	}

	@Override
	public void println(String x)
	{
		recordedLines.push(x);
	}

	/**
	 * @return the number of lines currently recorded
	 */
	public int getLinesLeft()
	{
		return recordedLines.size();
	}

	/**
	 * returns and removes the oldest recorded line
	 * 
	 * @return the oldest recorded line
	 */
	public String getNextLine()
	{
		return recordedLines.pollLast();
	}

	/**
	 * removes all recorded lines
	 */
	public void reset()
	{
		recordedLines.clear();
	}
}
