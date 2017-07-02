package de.upb.cs.is.jpl.cli.command;


import java.io.File;

import de.upb.cs.is.jpl.cli.common.AUnitTest;


/**
 * This abstract class is used for all tests of the baselearner algorithms.
 * 
 * @author Tanja Tornede
 */
public abstract class ACommandUnitTest extends AUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "command" + File.separator;


   /**
    * Creates a new unit test for commands.
    * 
    */
   public ACommandUnitTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Creates a new unit test for commands with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ACommandUnitTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }

}
