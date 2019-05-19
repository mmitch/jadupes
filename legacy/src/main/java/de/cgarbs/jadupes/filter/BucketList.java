/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.filter;

import java.util.Collection;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	/**
	 * Identifies sub-buckets by a value for every element.
	 * Elements mapping to the same value are put in the same sub-bucket.
	 * 
	 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
	 *
	 * @param <T>
	 *            the type of elements in the bucket list
	 * @param <R>
	 *            the type of identifier created
	 */
	public interface Classifier<T, R> extends Function<T, R>
	{
	};

	/**
	 * Directly creates sub-buckets by distributing the elements of a bucket.
	 * 
	 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
	 *
	 * @param <T>
	 *            the type of elements in the bucket list
	 */
	public interface Partitioner<T> extends Function<List<T>, Stream<List<T>>>
	{
	};

	// FIXME: make final again (currently impossible bc toString() + Stream)
	private Stream<List<T>> buckets;

	private BucketList(Stream<List<T>> buckets)
	{
		this.buckets = buckets;
	}

	/**
	 * Creates a bucket list.
	 * 
	 * @param elements
	 *            the elements to put into the bucket list
	 * @param classifier
	 *            extracts the bucket identifier for every element
	 * @return a new bucket list
	 */
	public static <T, R> BucketList<T> create(List<T> elements, Classifier<T, R> classifier)
	{

		return new BucketList<T>(partition(elements, classifier));
	}

	/**
	 * @return the buckets containing the elements (a list of lists)
	 */
	public Collection<List<T>> getBuckets()
	{
		return buckets.collect(Collectors.toList());
	}

	/**
	 * Creates a new bucket list in which each existing bucket is split into
	 * further sub-buckets.
	 * Elements of different buckets are not mixed in the new sub-buckets.
	 * 
	 * @param classifier
	 *            extracts the new sub-bucket identifier for every element
	 * @return a new bucket list
	 */
	public <R> BucketList<T> refine(Classifier<T, R> classifier)
	{
		return new BucketList<T>(buckets.flatMap(bucket -> partition(bucket, classifier)));
	}

	/**
	 * Creates a new bucket list in which each existing bucket is split into
	 * further sub-buckets.
	 * Elements of different buckets are not mixed in the new sub-buckets.
	 * 
	 * @param partitioner
	 *            creates the sub-lists for a given list
	 * @return a new bucket list
	 */
	public BucketList<T> refine(Partitioner<T> partitioner)
	{
		return new BucketList<T>(buckets.flatMap(bucket -> partitioner.apply(bucket)));
	}

	/**
	 * Creates a new bucket list in which all buckets that contain only
	 * one element are removed.
	 * 
	 * @return a new bucket list without all unique elements
	 */
	public BucketList<T> removeUniqueElements()
	{
		return new BucketList<T>(buckets.filter(bucket -> bucket.size() > 1));
	}

	/**
	 * <b>look out:</b> this is an expensive operation because it has to convert
	 * the BucketList Stream to a List and back into a Stream (and another
	 * Stream)
	 * 
	 * TODO: move to dedicated statistics method?
	 */
	@Override
	public String toString()
	{
		List<List<T>> bucketsCopy = buckets.collect(Collectors.toList());
		buckets = bucketsCopy.parallelStream();

		LongSummaryStatistics statistics = bucketsCopy.parallelStream().collect(Collectors.summarizingLong(List::size));

		long bucketCount = statistics.getCount();
		long elementCount = statistics.getSum();
		long minElements = statistics.getMin();
		double avgElements = statistics.getAverage();
		long maxElements = statistics.getMax();

		if (bucketCount == 0)
		{
			minElements = 0;
			maxElements = 0;
		}

		return String.format("BucketList: total: %d buckets, %d elements, min/avg/max elements per bucket: %d/%.0f/%d", bucketCount, elementCount, minElements, avgElements, maxElements);
	}

	private static <T, R> Stream<List<T>> partition(List<T> elements, Function<T, R> classifier)
	{
		return elements.parallelStream() //
				.collect(Collectors.groupingBy(classifier)) //
				.values() //
				.parallelStream();
	}

}
