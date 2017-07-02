package de.upb.cs.is.jpl.api.util;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.upb.cs.is.jpl.api.util.datastructure.WeightedObject;


/**
 * 
 * 
 * Tests for {@link WeightedObject}
 * 
 * @author Sebastian Osterbrink
 *
 */
public class WeightedObjectTest {


   /**
    * Tests the basic compare operations
    */
   @Test
   public void testCompare() {
      WeightedObject<String> first = new WeightedObject<String>("abc", 1.0);
      WeightedObject<String> second = new WeightedObject<String>("def", 2.0);
      assertEquals("Test the compare operation", -1, first.compareTo(second));
      assertEquals("Test the compare operation", 1, second.compareTo(first));
      assertEquals("Test the compare operation", 0, first.compareTo(first));
      assertEquals("Test the compare operation", 0, second.compareTo(second));

   }
}
