package de.upb.cs.is.jpl.cli.command.help;


import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This command Handler is responsible for handling given {@link HelpCommandConfiguration} and
 * creating an {@link HelpCommand}. It holds the according command configuration before it is passed
 * to the command. Additionally it is responsible for interpreting the {@link CommandResult} given
 * by the command at the end of execution.
 * 
 * @author Tanja Tornede
 *
 */
public class HelpCommandHandler extends ACommandHandler {

   @Override
   public void init() {
      // no init needed
   }


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      return new HelpCommand();
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new HelpCommandConfiguration();
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      return StringUtils.EMPTY_STRING;
   }

}
