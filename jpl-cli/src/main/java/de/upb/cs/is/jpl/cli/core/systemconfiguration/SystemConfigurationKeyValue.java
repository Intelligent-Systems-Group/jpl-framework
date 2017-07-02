package de.upb.cs.is.jpl.cli.core.systemconfiguration;


/**
 * This class contains JSON key values for the SystemConfiguraiton.json file.
 * 
 * @author Andreas Kornelsen
 *
 */
public class SystemConfigurationKeyValue {


   /**
    * The constant INPUT_FILES identifies an array of input files objects.
    */
   public static final String INPUT_FILES = "input_files";

   /**
    * The Constant OUTPUT_FILE identifies an output file where the output of JPL is stored.
    */
   public static final String OUTPUT_FILE = "output_file";

   /**
    * The Constant LEARNING_PROBLEM identifies the preference learning type which should be solved
    * by the listed algorithms.
    */
   public static final String LEARNING_PROBLEM = "learning_problem";

   /** The Constant ALGORITHMS identifies an array of algorithm objects. */
   public static final String ALGORITHMS = "algorithms";

   /** The Constant EVALUATION identifies an object for the evaluation configuration. */
   public static final String EVALUATION = "evaluation";

   /** The Constant SEED identifies an object for the evaluation configuration. */
   public static final String SEED = "seed";


   /**
    * The Constant FILE_PATH is an key value for an input file and identifies an absolute path to a
    * dataset.
    */
   public static final String FILE_PATH = "file_path";

   /**
    * The Constant SELECTED_CONTEXT_FEATURES is an key value for an input file and identifies an
    * array of selected context features.
    */
   public static final String SELECTED_CONTEXT_FEATURES = "selected_context_features";

   /**
    * The Constant SELECTED_ITEM_FEATURES is an key value for an input file and identifies an array
    * of selected item features.
    */
   public static final String SELECTED_ITEM_FEATURES = "selected_item_features";

   /**
    * The Constant DESCRIPTION is an key value for an input file and identifies the description for
    * an input file.
    */
   public static final String DESCRIPTION = "description";


   /**
    * Instantiates a private SystemConfiguration to hide the public one.
    */
   private SystemConfigurationKeyValue() {

   }

}
