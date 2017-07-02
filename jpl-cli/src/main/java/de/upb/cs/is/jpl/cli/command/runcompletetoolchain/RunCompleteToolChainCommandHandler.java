package de.upb.cs.is.jpl.cli.command.runcompletetoolchain;


import java.util.List;

import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This command handler is responsible for handling a given
 * {@link RunCompleteToolChainCommandConfiguration} and creating a
 * {@link RunCompleteToolChainCommand}. It holds the according command configuration before it is
 * passed to the command. Additionally it is responsible for interpreting the {@link CommandResult}
 * given by the command at the end of execution.
 * 
 * @author Alexander Hetzer
 *
 */
public class RunCompleteToolChainCommandHandler extends ACommandHandler {

   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "The given command configuration is of the wrong type.";

   private RunCompleteToolChainCommandOutputGenerator outputGenerator;


   @Override
   public ICommand handleUserCommand(ICommandConfiguration commandConfiguration) {
      if (!(commandConfiguration instanceof RunCompleteToolChainCommandConfiguration)) {
         throw new IllegalArgumentException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      RunCompleteToolChainCommandConfiguration castedCommandConfiguration = (RunCompleteToolChainCommandConfiguration) commandConfiguration;
      return new RunCompleteToolChainCommand(castedCommandConfiguration);
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      @SuppressWarnings("unchecked")
      List<Pair<ECommand, Boolean>> commandExecutionSuccessPairs = (List<Pair<ECommand, Boolean>>) commandResult.getResult();
      return outputGenerator.generateResultMessage(commandExecutionSuccessPairs);
   }


   @Override
   public void init() {
      this.outputGenerator = new RunCompleteToolChainCommandOutputGenerator();
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new RunCompleteToolChainCommandConfiguration();
   }


}
