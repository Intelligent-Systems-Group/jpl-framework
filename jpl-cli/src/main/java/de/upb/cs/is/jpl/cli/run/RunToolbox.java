package de.upb.cs.is.jpl.cli.run;


import java.util.Locale;
import java.util.Random;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.cli.core.CommandLineParserView;


/**
 * Main class for running the toolbox.
 * 
 * @author Alexander Hetzer
 *
 */
public class RunToolbox {


   /**
    * Private default constructor, as this class will only contain the main method of the tool and
    * therefore shall never be initialized.
    */
   private RunToolbox() {
   }


   /**
    * Entry point for the program.
    * 
    * @param args the arguments given by the user
    */
   public static void main(final String[] args) {
      Locale.setDefault(Locale.US);
      LoggingConfiguration.setupLoggingConfiguration();
      RandomGenerator.initializeRNG(new Random().nextLong());
      CommandLineParserView.getCommandLineParserView().handleUserInput(args);
   }

}
