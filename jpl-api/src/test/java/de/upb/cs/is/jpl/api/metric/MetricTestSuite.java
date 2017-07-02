package de.upb.cs.is.jpl.api.metric;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.metric.accuracy.AccuracyMetricTest;
import de.upb.cs.is.jpl.api.metric.crossentropy.CrossEntropyErrorTest;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.ExampleBasedFMeasureTestSuite;
import de.upb.cs.is.jpl.api.metric.hammingloss.HammingLossTest;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTauTest;
import de.upb.cs.is.jpl.api.metric.labelbasedfmeasure.MacroLabelBasedFMeasureTest;
import de.upb.cs.is.jpl.api.metric.meanabsoluteerror.MeanAbsoluteErrorTest;
import de.upb.cs.is.jpl.api.metric.meansquarederror.MeanSquaredErrorTest;
import de.upb.cs.is.jpl.api.metric.rootmeansquareerror.RootMeanSquareErrorTest;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelationTest;
import de.upb.cs.is.jpl.api.metric.subset01.Subset01LossTest;


/**
 * Test suite for all metric tests.
 * 
 * @author Pritha Gupta
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ KendallsTauTest.class, SpearmansCorrelationTest.class, AccuracyMetricTest.class, HammingLossTest.class,
      MeanAbsoluteErrorTest.class, MeanSquaredErrorTest.class, RootMeanSquareErrorTest.class, ExampleBasedFMeasureTestSuite.class,
      Subset01LossTest.class, CrossEntropyErrorTest.class, MacroLabelBasedFMeasureTest.class })
public class MetricTestSuite {

}
