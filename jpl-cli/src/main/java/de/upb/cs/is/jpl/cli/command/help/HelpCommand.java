package de.upb.cs.is.jpl.cli.command.help;


import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.InputControl;


/**
 * This command is responsible to output help texts for the user if he/she asks for help.
 * 
 * @author Tanja Tornede
 *
 */
public class HelpCommand extends ACommand {

   private static final String PRINTED_JCOMMANDER_USAGE = "Printed JCommander usage.";


   /**
    * Constructor for a {@code HelpCommand}.
    * 
    */
   public HelpCommand() {
      super(ECommand.HELP.getCommandIdentifier());
   }


   @Override
   public boolean canBeExecuted() {
      // can always be executed as it only prints the JCommander usage method
      return true;
   }


   @Override
   public CommandResult executeCommand() {
      InputControl.getInputControl().printUsage();
      return CommandResult.createSuccessCommandResult(PRINTED_JCOMMANDER_USAGE);
   }


   @Override
   public String getFailureReason() {
      // can be empty, as the command cannot fail
      return StringUtils.EMPTY_STRING;
   }


   @Override
   public void undo() {
      // nothing to do here
   }

}
