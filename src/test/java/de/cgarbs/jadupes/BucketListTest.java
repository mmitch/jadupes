/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static java.util.function.Function.identity;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		BucketList<String> result = BucketList.create(emptyList, identity());

		// then
		assertThat(result.getBuckets(), empty());
	}

	@Test
	public void bucketListWithIdentityFunctionWrapsEveryElementIntoAList()
	{
		// given
		List<String> elements = Arrays.asList("A", "B", "C");

		// when
		BucketList<String> result = BucketList.create(elements, identity());

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
	public void refineBucketListBySecondCriteria()
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

	private int getDigits(Integer i)
	{
		return i.toString().length();
	}

	private boolean evenOdd(Integer i)
	{
		return i % 2 == 0;
	}
}
