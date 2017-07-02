package de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * This commands determines which algorithms are applicable for the dataset that was previously set.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
public class DetermineApplicableAlgorithmsCommand extends ACommand {

   private static final Logger logger = LoggerFactory.getLogger(DetermineApplicableAlgorithmsCommand.class);

   private static final String ERROR_DATASET_IN_CONFIGURATION_EMPTY = "The list of dataset files stored in the system is null.";
   private static final String ERROR_NO_DATASET_IN_CONFIGURATION = "The list of dataset files stored in the system configuration is empty.";

   private static final String ALGORITHM_RUNS_ON = "Algorithm %s runs on dataset %s.";
   private static final String ALGORITHM_RUNS_NOT_ON = "Algorithm  %s runs not on dataset %s.";

   private String failureReason = StringUtils.EMPTY_STRING;
   Map<DatasetFile, List<ILearningAlgorithm>> datasetFileToListOfLearningAlgorithmsMap;


   /**
    * Creates a new {@link DetermineApplicableAlgorithmsCommand}.
    */
   public DetermineApplicableAlgorithmsCommand() {
      super(ECommand.DETERMINE_APPLICABLE_ALGORITHMS.getCommandIdentifier());
      datasetFileToListOfLearningAlgorithmsMap = new HashMap<>();
   }


   @Override
   public boolean canBeExecuted() {
      if (isListOfDatasetFilesInSystemConfigurationNull() || isListOfDatasetFilesInSystemConfigurationEmpty()) {
         return false;
      }
      return true;
   }


   /**
    * Checks if the dataset files stored in the system configuration are null.
    * 
    * @return {@code true} if the list of dataset files stored in the system configuration is null,
    *         otherwise {@code false} if the list of dataset files stored in the system
    *         configuration is not null.
    */
   private boolean isListOfDatasetFilesInSystemConfigurationNull() {
      if (SystemConfiguration.getSystemConfiguration().getDatasetFiles() == null) {
         failureReason = ERROR_DATASET_IN_CONFIGURATION_EMPTY;
         return true;
      }
      return false;
   }


   /**
    * Checks if the list of dataset files stored in the system configuration is empty.
    * 
    * @return {@code true} if the list of dataset files stored in the system configuration is empty,
    *         otherwise {@code false} if the list of dataset files stored in the system
    *         configuration contains at least one element.
    */
   private boolean isListOfDatasetFilesInSystemConfigurationEmpty() {
      if (SystemConfiguration.getSystemConfiguration().getDatasetFiles().isEmpty()) {
         failureReason = ERROR_NO_DATASET_IN_CONFIGURATION;
         return true;
      }
      return false;
   }


   @Override
   public CommandResult executeCommand() {
      return getCommandResultWithMapOfDatasetFilesToListOfApplicableAlgorithms();
   }


   /**
    * Returns a {@link CommandResult} with a map of dataset files to a list of applicable learning
    * algorithms as result.
    *
    * @return the map of dataset files to a list of applicable algorithms
    */
   private CommandResult getCommandResultWithMapOfDatasetFilesToListOfApplicableAlgorithms() {
      for (DatasetFile datasetFile : SystemConfiguration.getSystemConfiguration().getDatasetFiles()) {
         List<ILearningAlgorithm> applicableAlgorithms = new ArrayList<>();
         datasetFileToListOfLearningAlgorithmsMap.put(datasetFile, applicableAlgorithms);

         for (ELearningProblem eLearningProblem : ELearningProblem.getELearningProblems()) {
            for (ELearningAlgorithm eLearningAlgorithm : ELearningAlgorithm.getELearningAlgorithmsByLearningProblem(eLearningProblem)) {
               addAlgorithmToMapIfApplicable(datasetFile, eLearningAlgorithm);
            }
         }
      }

      return CommandResult.createSuccessCommandResult(datasetFileToListOfLearningAlgorithmsMap);
   }


   /**
    * Adds the according algorithm of the given enum to the member variable that maps the dataset
    * files to a list of applicable algorithms, if the given algorithm is applicable for the given
    * dataset.
    * 
    * @param datasetFile the dataset file of the dataset to check
    * @param eLearningAlgorithm the enum of the learning algorithm to check
    */
   private void addAlgorithmToMapIfApplicable(DatasetFile datasetFile, ELearningAlgorithm eLearningAlgorithm) {
      try {
         ILearningAlgorithm iLearningAlgorithm = eLearningAlgorithm.createLearningAlgorithm();

         IDatasetParser iDatasetParser = iLearningAlgorithm.getDatasetParser();
         iDatasetParser.parsePartialOf(datasetFile, 1);

         datasetFileToListOfLearningAlgorithmsMap.get(datasetFile).add(iLearningAlgorithm);
         logger.debug(String.format(ALGORITHM_RUNS_ON, iLearningAlgorithm, datasetFile.getFile().getName()));

      } catch (ParsingFailedException exception) {
         // Only exception could be that parsing don't fit
         logger.warn(String.format(ALGORITHM_RUNS_NOT_ON, eLearningAlgorithm.getIdentifier(), datasetFile.getFile().getName()), exception);
      }
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      // No function needed
   }

}
