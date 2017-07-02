package de.upb.cs.is.jpl.api.metric.examplebasedfmeasure;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * This test suite runs the tests for the {@link ExampleBasedFMeasure} and all sub measures.
 * 
 * @author Alexander Hetzer
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ExampleBasedFMeasureTest.class, ExampleBasedPrecisionTest.class, ExampleBasedRecallTest.class })
public class ExampleBasedFMeasureTestSuite {
}
