/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	private final Collection<List<T>> buckets;

	private <R> BucketList(List<T> elements, Function<T, R> classifier)
	{
		buckets = elements.stream() //
				.collect(Collectors.groupingBy(classifier)) //
				.values();
	}

	/**
	 * creates a bucket list
	 * 
	 * @param elements
	 *            the elements to put into the bucket list
	 * @param classifier
	 *            extracts the bucket identifier for every element
	 * @return a new bucket list
	 */
	public static <T, R> BucketList<T> create(List<T> elements, Function<T, R> classifier)
	{
		return new BucketList<T>(elements, classifier);
	}

	/**
	 * @return the buckets containing the elements (a list of lists)
	 */
	public Collection<List<T>> getBuckets()
	{
		return buckets;
	}

}
