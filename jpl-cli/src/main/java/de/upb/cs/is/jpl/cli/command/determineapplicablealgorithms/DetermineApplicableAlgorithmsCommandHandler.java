package de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This command Handler is responsible for handling given
 * {@link DetermineApplicableAlgorithmsCommandConfiguration} and creating an
 * {@link DetermineApplicableAlgorithmsCommand}. It holds the according command configuration before
 * it is passed to the command. Additionally it is responsible for interpreting the
 * {@link CommandResult} given by the command at the end of execution.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
public class DetermineApplicableAlgorithmsCommandHandler extends ACommandHandler {

   private static final String ALGORITHM_RUNS_ON = "Algorithm %s runs on dataset %s.";


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      return new DetermineApplicableAlgorithmsCommand();
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      return getUserOutput(commandResult.getResult());
   }


   @Override
   public void init() {
      // No init needed
   }


   /**
    * Returns a string listing all datasets and its applicable algorithms.
    *
    * @param result the result of a {@link CommandResult} of a
    *           {@link DetermineApplicableAlgorithmsCommand}
    * @return a string listing datasets and applicable algorithms
    */
   private String getUserOutput(Object result) {
      StringBuilder stringBuilder = new StringBuilder();

      @SuppressWarnings("unchecked") // works as of construction
      Map<DatasetFile, List<ILearningAlgorithm>> datasetFileToListOfLearningAlgorithmsMap = (Map<DatasetFile, List<ILearningAlgorithm>>) result;

      for (Entry<DatasetFile, List<ILearningAlgorithm>> entry : datasetFileToListOfLearningAlgorithmsMap.entrySet()) {
         for (ILearningAlgorithm learningAlgorithm : entry.getValue()) {
            stringBuilder.append(String.format(ALGORITHM_RUNS_ON, learningAlgorithm, entry.getKey().getFile().getName()));
            stringBuilder.append(StringUtils.LINE_BREAK);
         }
      }

      return stringBuilder.toString();
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new DetermineApplicableAlgorithmsCommandConfiguration();
   }

}
