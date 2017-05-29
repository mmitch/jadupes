/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.data;

/**
 * various configuration constants
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
public class Configuration
{
	/**
	 * bytes to read() at once
	 */
	public static final int READ_BLOCK_SIZE = 4096;

	private Configuration()
	{
		// Singleton, only constants
	}
}
