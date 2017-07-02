package de.upb.cs.is.jpl.cli.command.trainmodels;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * This command obtains all datasets and algorithms which are contained in the
 * {@link SystemConfiguration} and trains the algorithms on the {@link IDataset}. The resulting
 * {@link ILearningModel}s are then stored in the {@link SystemConfiguration}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class TrainModelsCommand extends ACommand {

   private static final String ERROR_NO_LEARNING_ALGORITHM = "No learning algorithms have been selected for training.";
   private static final String ERROR_NO_DATASET_FILE = "No dataset files have been selected for training.";
   private static final String ERROR_NO_TRAINING_OF_ANY_ALGORITHM = "No algorithm could be trained on any dataset.";
   private static final String ERROR_TRAINING_OF_SINGLE_ALGORITHM_FAILED = "Training of %s on %s failed.";
   private static final String ERROR_REMARK = "Error: %s";

   private static final Logger logger = LoggerFactory.getLogger(TrainModelsCommand.class);
   private SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();
   private String failureReason = StringUtils.EMPTY_STRING;
   private boolean isAnyAlgorithmTrainable = false;


   /**
    * Constructs a {@link TrainModelsCommand}.
    */
   public TrainModelsCommand() {
      super(ECommand.TRAIN_MODELS.getCommandIdentifier());
   }


   @Override
   public boolean canBeExecuted() {
      // Define new because canBeExecuted can be called more than once
      failureReason = StringUtils.EMPTY_STRING;
      // Executing is ok if algorithm list and dataset list are not empty
      if (!doesSystemConfigurationHasDatasetFiles() || !doesSystemConfigurationHasLearningAlgorithms()) {
         return false;
      }

      return true;
   }


   /**
    * Checks if at least one {@link ILearningAlgorithm} is in the {@link SystemConfiguration}. If
    * not it also added the failure reason to {@link TrainModelsCommand#failureReason}.
    * 
    * @return {@code true} if the {@link SystemConfiguration} holds at least one learning algorithm,
    *         otherwise {@code false} if there is no algorithm in the {@link SystemConfiguration}
    */
   private boolean doesSystemConfigurationHasLearningAlgorithms() {
      if (systemConfiguration.getLearningAlgorithms().isEmpty()) {
         failureReason += ERROR_NO_LEARNING_ALGORITHM;
         return false;
      }
      return true;
   }


   /**
    * Checks if at least one {@link IDataset} is in the {@link SystemConfiguration}. If not it also
    * added the failure reason to {@link TrainModelsCommand#failureReason}.
    * 
    * @return {@code true} if the system configuration holds at least one dataset file, otherwise
    *         {@code false} if there is no dataset in the {@link SystemConfiguration}
    */
   private boolean doesSystemConfigurationHasDatasetFiles() {
      if (systemConfiguration.getDatasetFiles().isEmpty()) {
         failureReason += ERROR_NO_DATASET_FILE;
         return false;
      }
      return true;
   }


   @Override
   public CommandResult executeCommand() {
      List<CommandResult> commandResultList = trainModelsAndReturnCommandResults();

      if (!isAnyAlgorithmTrainable) {
         return CommandResult.createFailureCommandResult(new TrainModelsFailedException(ERROR_NO_TRAINING_OF_ANY_ALGORITHM));
      }

      return CommandResult.createSuccessCommandResult(commandResultList);
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      systemConfiguration.clearLearningAlgorithmDatasetPairToLearningModelMap();
   }


   /**
    * Creates a {@link CommandResult} list. If the training of a algorithm on a dataset was
    * successful there will be added a {@link CommandResult} of a pair of {@link ILearningAlgorithm}
    * and {@link IDataset}. Otherwise the current {@link CommandResult} will be filled with the
    * occurred exception which can be of type {@link TrainModelsFailedException} or
    * {@link ParsingFailedException}.
    * 
    * @return a list of {@link CommandResult} for each combination of algorithm and dataset in the
    *         {@link SystemConfiguration}
    */
   private List<CommandResult> trainModelsAndReturnCommandResults() {

      List<ILearningAlgorithm> learningAlgorithms = systemConfiguration.getLearningAlgorithms();
      List<DatasetFile> datasetFiles = systemConfiguration.getDatasetFiles();

      int maximumCommandResultListSize = learningAlgorithms.size() * datasetFiles.size();
      List<CommandResult> commandResults = new ArrayList<>(maximumCommandResultListSize);

      // Check each combination of algorithms and datasets
      for (ILearningAlgorithm learningAlgorithm : learningAlgorithms) {
         for (DatasetFile datasetFile : datasetFiles) {
            try {
               IDatasetParser datasetParser = learningAlgorithm.getDatasetParser();
               IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);
               ILearningModel<?> learningModel = learningAlgorithm.train(dataset);

               systemConfiguration.addModelForAlgorithmDatasetPair(learningModel, learningAlgorithm, dataset);
               commandResults.add(CommandResult.createSuccessCommandResult(Pair.of(learningAlgorithm, datasetFile)));
               isAnyAlgorithmTrainable = true;
            } catch (TrainModelsFailedException | ParsingFailedException e) {
               String errorMessage = String.format(ERROR_TRAINING_OF_SINGLE_ALGORITHM_FAILED, datasetFile.getFile().getName(),
                     learningAlgorithm) + String.format(ERROR_REMARK, e.getMessage());
               commandResults.add(CommandResult.createFailureCommandResult(new TrainModelsFailedException(errorMessage)));
               logger.error(errorMessage, e);
            }
         }
      }

      return commandResults;
   }
}
