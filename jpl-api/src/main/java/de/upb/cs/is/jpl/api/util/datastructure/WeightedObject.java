package de.upb.cs.is.jpl.api.util.datastructure;


/**
 * This class allows it to assign objects which don't implement the {@link Comparable} interface a
 * weight value, by which they become {@link Comparable}.
 * 
 * @author Sebastian Osterbrink
 *
 * @param <T> the type of the content object
 */
public class WeightedObject<T> implements Comparable<WeightedObject<T>> {

   protected double weight;
   protected T value;


   /**
    * Creates a new {@link WeightedObject}, which is a pair of an value and it's weight.
    * 
    * @param value the value described by this object
    * @param weight the weight which is compared
    */
   public WeightedObject(T value, Double weight) {
      this.value = value;
      this.weight = weight;
   }


   @Override
   public int compareTo(WeightedObject<T> secondObject) {
      if (Double.compare(weight, secondObject.getWeight()) == 0)
         return 0;
      if (weight < secondObject.getWeight())
         return -1;
      else
         return 1;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o instanceof WeightedObject<?>) {
         WeightedObject<?> obj = (WeightedObject<?>) o;
         return obj.getValue().equals(value) && Double.compare(obj.getWeight(), weight) == 0;
      }
      return false;
   }


   @Override
   public int hashCode() {
      int code = super.hashCode();
      code += 31 * Double.hashCode(weight);
      code += 31 * value.hashCode();
      return code;
   }


   /**
    * Returns the assigned weight value
    * 
    * @return the weight
    */
   public double getWeight() {
      return weight;
   }


   /**
    * Returns the assigned value object
    * 
    * @return the value
    */
   public T getValue() {
      return value;
   }


   @Override
   public String toString() {
      return value.toString() + "(w = " + weight + ")";
   }

}
