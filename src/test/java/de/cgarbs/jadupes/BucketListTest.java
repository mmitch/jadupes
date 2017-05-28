/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes;

import static java.util.function.Function.identity;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

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

}
