package de.upb.cs.is.jpl.api.util.datastructure;


/**
 * Generic Pair class that can aggregate any two classes in one object.
 * 
 * @author Tanja Tornede
 *
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 */
public class Pair<A, B> {


   private A first;
   private B second;


   /**
    * Constructor for creating a pair with first and second as aggregated objects.
    * 
    * @param first the first element of the pair.
    * @param second the second element of the pair.
    */
   private Pair(A first, B second) {
      this.first = first;
      this.second = second;
   }


   /**
    * Method for creating an appropriately typed pair.
    * 
    * @param <A> the type of the first object.
    * @param <B> the type of the second object.
    * @param first the first element of the pair.
    * @param second the second element in the pair.
    * @return template formed for the pair.
    */
   public static <A, B> Pair<A, B> of(A first, B second) {
      if (first == null) {
         throw new NullPointerException("First element of pair is null.");
      }
      if (second == null) {
         throw new NullPointerException("Second element of pair is null.");
      }
      return new Pair<>(first, second);
   }


   /**
    * Returns the first element of the pair.
    * 
    * @return the first element of the pair.
    */
   public A getFirst() {
      return first;
   }


   /**
    * Returns the second element of the pair.
    * 
    * @return the second element of the pair.
    */
   public B getSecond() {
      return second;
   }


   /**
    * Set the first element of the pair.
    * 
    * @param first the first element to set.
    */
   public void setFirst(A first) {
      this.first = first;
   }


   /**
    * Set the second element of the pair.
    * 
    * @param second the second element to set.
    */
   public void setSecond(B second) {
      this.second = second;
   }


   @Override
   public String toString() {
      return "Pair [first=" + first + ", second=" + second + "]";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((first == null) ? 0 : first.hashCode());
      result = prime * result + ((second == null) ? 0 : second.hashCode());
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
      Pair<A, B> other = (Pair<A, B>) obj;
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
      return true;
   }

}
