package de.upb.cs.is.jpl.api.util.datastructure;


import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Generic Triple class that can aggregate any three classes in one object.
 * 
 * @author Sebastian Gottschalk
 *
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 * @param <C> the type of the third object
 */
public class Triple<A, B, C> {
   private static final String ERROR_X_ELEMENT_IS_NULL = "element of triple is null.";
   private static final String ERROR_FIRST_ELEMENT_IS_NULL = "First" + StringUtils.SINGLE_WHITESPACE + ERROR_X_ELEMENT_IS_NULL;
   private static final String ERROR_SECOND_ELEMENT_IS_NULL = "Second" + StringUtils.SINGLE_WHITESPACE + ERROR_X_ELEMENT_IS_NULL;
   private static final String ERROR_THIRD_ELEMENT_IS_NULL = "Third" + StringUtils.SINGLE_WHITESPACE + ERROR_X_ELEMENT_IS_NULL;
   private static final String TO_STRING_INFORMATION = "Triple [first=%s, second=%s, third=%s]";

   private A first;
   private B second;
   private C third;


   /**
    * Constructor for creating a triple with first, second and third as aggregated objects.
    * 
    * @param first the first element of the triple
    * @param second the second element of the triple
    * @param third the third element of the triple
    */
   private Triple(A first, B second, C third) {
      this.first = first;
      this.second = second;
      this.third = third;
   }


   /**
    * Method for creating an appropriately typed triple.
    * 
    * @param <A> the type of the first object
    * @param <B> the type of the second object
    * @param <C> the type of the third object
    * @param first the first element of the triple
    * @param second the second element in the triple
    * @param third the third element in the triple
    * @return the templatized triple
    */
   public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
      if (first == null) {
         throw new NullPointerException(ERROR_FIRST_ELEMENT_IS_NULL);
      }
      if (second == null) {
         throw new NullPointerException(ERROR_SECOND_ELEMENT_IS_NULL);
      }
      if (third == null) {
         throw new NullPointerException(ERROR_THIRD_ELEMENT_IS_NULL);
      }
      return new Triple<>(first, second, third);
   }


   /**
    * Returns the first element of the triple.
    * 
    * @return the first element of the triple
    */
   public A getFirst() {
      return first;
   }


   /**
    * Returns the second element of the triple.
    * 
    * @return the second element of the triple
    */
   public B getSecond() {
      return second;
   }


   /**
    * Returns the third element of the triple.
    * 
    * @return the third element of the triple
    */
   public C getThird() {
      return third;
   }


   /**
    * Set the first element of the triple.
    * 
    * @param first the first element to set
    */
   public void setFirst(A first) {
      this.first = first;
   }


   /**
    * Set the second element of the triple.
    * 
    * @param second the second element to set
    */
   public void setSecond(B second) {
      this.second = second;
   }


   /**
    * Set the third element of the triple.
    * 
    * @param third the second element to set
    */
   public void setThird(C third) {
      this.third = third;
   }


   @Override
   public String toString() {
      return String.format(TO_STRING_INFORMATION, first, second, third);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((first == null) ? 0 : first.hashCode());
      result = prime * result + ((second == null) ? 0 : second.hashCode());
      result = prime * result + ((third == null) ? 0 : third.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      @SuppressWarnings("unchecked")
      Triple<A, B, C> other = (Triple<A, B, C>) obj;
      if (first == null) {
         if (other.first != null)
            return false;
      } else if (!first.equals(other.first))
         return false;
      if (second == null) {
         if (other.second != null)
            return false;
      } else if (!second.equals(other.second))
         return false;
      if (third == null) {
         if (other.third != null)
            return false;
      } else if (!third.equals(other.third))
         return false;

      return true;
   }

}
