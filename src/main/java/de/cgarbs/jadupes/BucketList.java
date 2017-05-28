/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A big list that is divided into sublists (buckets) via given
 * criteria (bucket identifier).
 * After putting the elements into the different buckets, the
 * bucket identifiers are forgotten, only the partition of the
 * elements into the buckets remains.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 * @param <T>
 *            the type of elements to store
 */
public class BucketList<T>
{
	private List<T> elements;

	private BucketList(List<T> elements)
	{
		this.elements = elements;
	}

	/**
	 * creates a bucket list
	 * 
	 * @param elements
	 *            the elements to put into the bucket list
	 * @param keyExtractor
	 *            extracts the bucket identifier for
	 * @return a new bucket list
	 */
	public static <T, R> BucketList<T> create(List<T> elements, Function<T, R> keyExtractor)
	{
		return new BucketList<T>(elements);
	}

	/**
	 * @return the buckets containing the elements (a list of lists)
	 */
	public List<List<T>> getBuckets()
	{
		ArrayList<List<T>> ret = new ArrayList<List<T>>();
		elements.forEach(element -> ret.add(Arrays.asList(element)));
		return ret;
	}

}
