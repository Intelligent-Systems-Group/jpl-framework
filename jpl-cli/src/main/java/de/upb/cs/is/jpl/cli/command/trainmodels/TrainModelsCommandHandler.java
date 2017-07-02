package de.upb.cs.is.jpl.cli.command.trainmodels;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This handler is responsible for handling the {@link TrainModelsCommand}. It initializes the
 * command and can also interpret the according command result.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class TrainModelsCommandHandler extends ACommandHandler {
   private static final String ALGORITHM_X_IS_TRAINED_ON_DATASET_Y = "Algorithm %s is trained on dataset %s.";


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      return new TrainModelsCommand();
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      if (commandResult.isExecutedSuccessfully()) {
         @SuppressWarnings("unchecked")
         List<CommandResult> commandResultList = (List<CommandResult>) commandResult.getResult();

         StringBuilder stringBuilder = new StringBuilder();
         for (CommandResult singleCommandResult : commandResultList) {

            if (singleCommandResult.isExecutedSuccessfully()) {
               @SuppressWarnings("unchecked")
               Pair<ILearningAlgorithm, DatasetFile> algorithmAndDatasetPair = (Pair<ILearningAlgorithm, DatasetFile>) singleCommandResult
                     .getResult();
               stringBuilder.append(String.format(ALGORITHM_X_IS_TRAINED_ON_DATASET_Y, algorithmAndDatasetPair.getFirst(),
                     algorithmAndDatasetPair.getSecond().getFile().getName()));
            } else {
               stringBuilder.append(singleCommandResult.getException().getMessage());
            }
            stringBuilder.append(StringUtils.LINE_BREAK);
         }

         return stringBuilder.toString();
      }

      return commandResult.getException().getMessage();
   }


   @Override
   public void init() {
      // No need of function
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new TrainModelsCommandConfiguration();
   }

}
