package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.crossvalidation.CollaborativeFilteringCrossValidationTest;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.insample.CollaborativeFilteringInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit.CollaborativeFilteringPercentageSplitTest;


/**
 * Tests for Collaborative Filtering evaluations.
 * 
 * @author Sebastian Osterbrink
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CollaborativeFilteringPercentageSplitTest.class, CollaborativeFilteringCrossValidationTest.class,
      CollaborativeFilteringInSampleEvaluationTest.class })
public class CollaborativeFilteringEvaluationTestSuite {

}
