/*
 * Copyright 2017 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.jadupes.test;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Something is visible (in most cases with visibility <i>package</i>)
 * only to be accessible from unit tests. It should not be accessed in
 * production code nor should the visibility be reduced.
 * 
 * @author Christian Garbs &lt;mitch@cgarbs.de&gt;
 *
 */
@Documented
@Retention(SOURCE)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface VisibleForTesting
{

}
