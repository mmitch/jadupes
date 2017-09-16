/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class BucketListTest
{
	@Test
	public void bucketListOfAnEmptyListIsEmpty()
	{
		// given
		List<String> emptyList = Collections.emptyList();

		// when
		BucketList<String> result = BucketList.create(emptyList, this::identity);

		// then
		assertThat(result.getBuckets(), empty());
	}

	@Test
	public void bucketListWithIdentityFunctionWrapsEveryElementIntoAList()
	{
		// given
		List<String> elements = Arrays.asList("A", "B", "C");

		// when
		BucketList<String> result = BucketList.create(elements, this::identity);

		// then
		assertThat(result.getBuckets().toArray(), //
				arrayContainingInAnyOrder( //
						Arrays.asList("A"), //
						Arrays.asList("B"), //
						Arrays.asList("C") //
				));
	}

	@Test
	public void bucketListOfStringsByTheirSize()
	{
		// given
		List<String> elements = Arrays.asList("dog", "cat", "mouse", "house", "tree");

		// when
		BucketList<String> result = BucketList.create(elements, String::length);

		// then
		assertThat(result.getBuckets().toArray(), //
				arrayContainingInAnyOrder( //
						Arrays.asList("dog", "cat"), //
						Arrays.asList("mouse", "house"), //
						Arrays.asList("tree") //
				));
	}

	@Test
	public void refineBucketListByClassifier()
	{
		// given
		List<Integer> elements = Arrays.asList(1, 2, 13, 17, 20, 25);
		BucketList<Integer> groupedByDigitCount = BucketList.create(elements, this::getDigits);

		// when
		BucketList<Integer> result = groupedByDigitCount.refine(this::evenOdd);

		// then
		assertThat(result.getBuckets().toArray(), //
				arrayContainingInAnyOrder( //
						Arrays.asList(1), //
						Arrays.asList(2), //
						Arrays.asList(13, 17, 25), //
						Arrays.asList(20) //
				));
	}

	@Test
	public void refineBucketListByPartitioner()
	{
		// given
		List<Integer> elements = Arrays.asList(2, 7, 9, 23, 24, 25, 99);
		BucketList<Integer> groupedByDigitCount = BucketList.create(elements, this::getDigits);

		// when
		BucketList<Integer> result = groupedByDigitCount.refine(this::createPairs);

		// then
		assertThat(result.getBuckets().toArray(), //
				arrayContainingInAnyOrder( //
						Arrays.asList(2, 7), //
						Arrays.asList(9), //
						Arrays.asList(23, 24), //
						Arrays.asList(25, 99) //
				));
	}

	@Test
	public void removeUniqueElementsRemovesBucketsWithOnlyOneElement()
	{
		// given
		List<String> elements = Arrays.asList("dog", "cat", "mouse", "house", "tree");
		BucketList<String> bucketList = BucketList.create(elements, String::length);

		// when
		BucketList<String> result = bucketList.removeUniqueElements();

		// then
		assertThat(result.getBuckets().toArray(), //
				arrayContainingInAnyOrder( //
						Arrays.asList("dog", "cat"), //
						Arrays.asList("mouse", "house") //
				// "tree" is removed
				));
	}

	@Test
	public void checkStatisticsForEmptyBucketList()
	{
		// given
		BucketList<String> empty = BucketList.create(Collections.emptyList(), this::identity);

		// when
		String result = empty.toString();

		// then
		assertThat(result, is("BucketList: total: 0 buckets, 0 elements, min/avg/max elements per bucket: 0/0/0"));
	}

	@Test
	public void checkStatisticsForBucketListWithOneElement()
	{
		// given
		BucketList<String> empty = BucketList.create(Arrays.asList("dog"), this::identity);

		// when
		String result = empty.toString();

		// then
		assertThat(result, is("BucketList: total: 1 buckets, 1 elements, min/avg/max elements per bucket: 1/1/1"));
	}

	@Test
	public void checkStatisticsForBucketListWithTwoElementsInOneBucket()
	{
		// given
		BucketList<String> empty = BucketList.create(Arrays.asList("dog", "dog"), this::identity);

		// when
		String result = empty.toString();

		// then
		assertThat(result, is("BucketList: total: 1 buckets, 2 elements, min/avg/max elements per bucket: 2/2/2"));
	}

	@Test
	public void checkStatisticsForBucketListWithTwoElementsInTwoBuckets()
	{
		// given
		BucketList<String> empty = BucketList.create(Arrays.asList("dog", "cat"), this::identity);

		// when
		String result = empty.toString();

		// then
		assertThat(result, is("BucketList: total: 2 buckets, 2 elements, min/avg/max elements per bucket: 1/1/1"));
	}

	@Test
	public void checkStatisticsForBucketListWithSixElementsInThreeBuckets()
	{
		// given
		BucketList<String> empty = BucketList.create(Arrays.asList("ant", "dog", "cat", "horse", "goat", "tiger"), String::length);

		// when
		String result = empty.toString();

		// then
		assertThat(result, is("BucketList: total: 3 buckets, 6 elements, min/avg/max elements per bucket: 1/2/3"));
	}

	private int getDigits(Integer i)
	{
		return i.toString().length();
	}

	private boolean evenOdd(Integer i)
	{
		return i % 2 == 0;
	}

	private Stream<List<Integer>> createPairs(List<Integer> integers)
	{
		List<List<Integer>> listOfLists = new ArrayList<>();
		List<Integer> currentList = null;

		Iterator<Integer> iter = integers.iterator();
		while (iter.hasNext())
		{
			if (currentList != null && currentList.size() > 1)
			{
				currentList = null;
			}

			if (currentList == null)
			{
				currentList = new ArrayList<>();
				listOfLists.add(currentList);
			}

			currentList.add(iter.next());
		}

		return listOfLists.stream();
	}

	private <T> T identity(T obj)
	{
		return obj;
	}
}
