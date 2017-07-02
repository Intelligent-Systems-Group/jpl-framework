package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link EvaluateAlgorithmsCommand}. The Parameters are defined for easy handling by JCommander.
 * 
 * @author Pritha Gupta
 * @author Andreas Kornelsen
 * 
 */
@Parameters(separators = "=", commandDescription = "Runs the evaluation of the given algorithms on the current datasets based on the given evaluation configuration.")
public class EvaluateAlgorithmsCommandConfiguration extends ACommandConfiguration {

   @Parameter(names = { "--evaluation_identifier",
         "-ei" }, description = "Specifies which kind of evaluation should be executed.", required = false)
   private String evaluationIdentifier;

   @Parameter(names = { "--metrics",
         "-m" }, description = "Specifies the list of evaluation metrics on which the evaluation should be based on.", required = false)
   private List<String> metricIdentifiers;


   /**
    * Constructor which created the command configuration for {@link EvaluateAlgorithmsCommand} and
    * initializes the member variables.
    */
   public EvaluateAlgorithmsCommandConfiguration() {
      metricIdentifiers = new ArrayList<>();
      resetFields();
   }


   @Override
   public void resetFields() {
      evaluationIdentifier = StringUtils.EMPTY_STRING;
      metricIdentifiers.clear();
   }


   @Override
   public ICommandConfiguration getCopy() {
      EvaluateAlgorithmsCommandConfiguration evaluateAlgorithmsCommandConfiguration = new EvaluateAlgorithmsCommandConfiguration();
      evaluateAlgorithmsCommandConfiguration.setEvaluationIdentifier(getEvaluationIdentifier());
      evaluateAlgorithmsCommandConfiguration.setEvaluationMetricIdentifier(CollectionsUtils.getDeepCopyOf(getMetricIdentifier()));
      return evaluateAlgorithmsCommandConfiguration;
   }


   /**
    * Returns the evaluation identifier parsed form the command line.
    * 
    * @return evaluationIdentifier the evaluation identifier for current evaluation
    */
   public String getEvaluationIdentifier() {
      return evaluationIdentifier;
   }


   /**
    * Returns the list of evaluation metric identifiers parsed form the command line.
    * 
    * @return evaluationMetricIdentifiers the evaluation identifier for current evaluation
    */

   public List<String> getMetricIdentifier() {
      return metricIdentifiers;
   }


   /**
    * Set the evaluation identifier for the command configuration.
    * 
    * @param evaluationIdentifier the evaluation identifier for current evaluation to set
    */
   public void setEvaluationIdentifier(String evaluationIdentifier) {
      this.evaluationIdentifier = evaluationIdentifier;
   }


   /**
    * Set the list of evaluation metric identifiers for the command configuration.
    * 
    * @param evaluationMetricIdentifiers the list of evaluation metric identifiers required for
    *           current evaluation to set
    */

   public void setEvaluationMetricIdentifier(List<String> evaluationMetricIdentifiers) {
      this.metricIdentifiers = CollectionsUtils.getDeepCopyOf(evaluationMetricIdentifiers);
   }

}
