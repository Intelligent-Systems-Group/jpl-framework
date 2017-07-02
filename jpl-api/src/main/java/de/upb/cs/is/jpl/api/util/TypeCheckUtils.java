package de.upb.cs.is.jpl.api.util;


import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.JplRuntimeException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedDatasetTypeException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;


/**
 * This class provides utility methods for checking the type of certain objects. It can be used to
 * check the the type of both an instance and a dataset, as this is a frequent task used in the
 * learning algorithms and models. If the check fails, an specialized {@link JplRuntimeException}
 * should be thrown.
 * 
 * @author Alexander Hetzer
 *
 */
public class TypeCheckUtils {

   private static final String ERROR_UNSUPPORTED_INSTANCE = "The instance type %s is unsupported. Use the following instance type: %s.";
   private static final String ERROR_UNSUPPORTED_DATASET = "The dataset type %s is unsupported. Use the following dataset type: %s.";


   /**
    * Hides the public default constructor, as this is a utility class.
    */
   private TypeCheckUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Checks if the given instance is of the given type.
    * 
    * @param <CONTEXT> the type of the context feature vector of the given instance
    * @param <ITEM> the type of the collection of the items associated with the instance
    * @param <RATING> the type of the prediction associated with the instance
    * @param instance the instance to check the type of
    * @param expectedType the expected type of the instance
    * 
    * @throws UnsupportedInstanceTypeException if the given instance is not of the given expected
    *            type
    */
   public static <CONTEXT, ITEM, RATING> void assertInstanceHasCorrectType(IInstance<CONTEXT, ITEM, RATING> instance,
         Class<?> expectedType) {
      if (!expectedType.isInstance(instance)) {
         Class<?> instanceClass = instance.getClass();
         throw new UnsupportedInstanceTypeException(
               String.format(ERROR_UNSUPPORTED_INSTANCE, instanceClass.getSimpleName(), expectedType.getSimpleName()));
      }
   }


   /**
    * Checks if the given dataset is of the given type.
    * 
    * @param <CONTEXT> the type of the context feature vector of the instances in the given dataset
    * @param <ITEM> the type of the collection of the items associated with the instances in the
    *           given dataset
    * @param <RATING> the type of the prediction associated with the instances in the given dataset
    * @param dataset the dataset to check the type of
    * @param expectedType the expected type of the dataset
    * 
    * @throws UnsupportedDatasetTypeException if the given dataset is not of the given expected type
    */
   public static <CONTEXT, ITEM, RATING> void assertDatasetHasCorrectType(IDataset<CONTEXT, ITEM, RATING> dataset, Class<?> expectedType) {
      if (!expectedType.isInstance(dataset)) {
         Class<?> datasetClass = dataset.getClass();
         throw new UnsupportedDatasetTypeException(
               String.format(ERROR_UNSUPPORTED_DATASET, datasetClass.getSimpleName(), expectedType.getSimpleName()));
      }
   }


}
