package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.metric.EMetric;


/**
 * To test the JCommander parsing and evaluation configuration setup for evaluations command.
 * 
 * @author Pritha Gupta
 *
 */
public class JCommanderEvaluateAlgorithmsCommandTest {
   private JCommander jCommander;
   private EvaluateAlgorithmsCommandConfiguration evaluateAlgorithmsCommandConfiguration;
   private static final String EVALUATE_ALGORITHM_IDENTIFIER_COMMAND_LINE = "--evaluation_identifier=supplied_testset";
   private static final String METRICS_IDENTIFIER_COMMAND_LINE = "--metrics= %s,%s";
   private static final String EVALUATE_ALGORITHM_COMMAND_LINE = "eval_algorithms";


   /**
    * Initializes the JCommander and a  TestJCommandConfiguration object and adds it to the commands
    * JCommander listens to.
    */
   @Before
   public void setupJCommanderTest() {
      jCommander = new JCommander();
      evaluateAlgorithmsCommandConfiguration = new EvaluateAlgorithmsCommandConfiguration();
      jCommander.addCommand(EVALUATE_ALGORITHM_COMMAND_LINE, evaluateAlgorithmsCommandConfiguration);
   }


   /**
    * Tests if JCommander correctly parses a command.
    */
   @Test
   public void testCommandConfigurationSupport() {
      jCommander.parse(EVALUATE_ALGORITHM_COMMAND_LINE, EVALUATE_ALGORITHM_IDENTIFIER_COMMAND_LINE,
            String.format(METRICS_IDENTIFIER_COMMAND_LINE, EMetric.KENDALLS_TAU.getMetricIdentifier(),
                  EMetric.SPEARMANS_RANK_CORRELATION.getMetricIdentifier()));

      assertEquals(EVALUATE_ALGORITHM_COMMAND_LINE, jCommander.getParsedCommand());
      assertEquals(EEvaluation.SUPPLIED_TEST_SET_RANK_AGGREGATION.getEvaluationIdentifier(),
            evaluateAlgorithmsCommandConfiguration.getEvaluationIdentifier());
      assertEquals(
            Arrays.asList(EMetric.KENDALLS_TAU.getMetricIdentifier(),
                  EMetric.SPEARMANS_RANK_CORRELATION.getMetricIdentifier()),
            evaluateAlgorithmsCommandConfiguration.getMetricIdentifier());

   }
}
