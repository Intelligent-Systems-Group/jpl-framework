package de.upb.cs.is.jpl.cli.command.adddatasets;


import java.io.File;

import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommand;


/**
 * Abstract class for tests for the {@link AddDatasetCommand}.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class AAddDatasetCommandTest extends ACommandUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "adddataset" + File.separator;


   /**
    * Creates a new integration test for the {@link AddDatasetCommand}.
    */
   public AAddDatasetCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }

}
