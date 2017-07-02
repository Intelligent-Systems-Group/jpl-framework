package de.upb.cs.is.jpl.api.util;


/**
 * This util class offers convenience methods for {@link Object}.
 * 
 * @author Tanja Tornede
 *
 */
public class ObjectUtils {

   /**
    * Hides the public constructor.
    */
   private ObjectUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Checks if both given parameters are null.
    * 
    * @param object1 the first parameter to check
    * @param object2 the second parameter to check
    * @return {@code true} if both paramerts are null, {@code false} otherwise if at least one is
    *         not null
    */
   public static boolean areBothObjectsNull(Object object1, Object object2) {
      if (object1 == null && object2 == null) {
         return true;
      }
      return false;
   }


   /**
    * Checks if both given parameters are not null.
    * 
    * @param object1 the first parameter to check
    * @param object2 the second parameter to check
    * @return {@code true} if both paramerts are not null, {@code false} otherwise if at least one
    *         is null
    */
   public static boolean areBothObjectsNotNull(Object object1, Object object2) {
      if (object1 != null && object2 != null) {
         return true;
      }
      return false;
   }

}
