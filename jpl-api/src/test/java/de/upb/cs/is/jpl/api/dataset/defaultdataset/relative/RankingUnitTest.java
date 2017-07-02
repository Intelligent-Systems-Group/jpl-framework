package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Test the {@link Ranking} class and its methods.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class RankingUnitTest {

   private static final String EXPECTED = "%s i1%si2%si3%s";


   /**
    * Test the output of the toString method
    */
   @Test
   public void testToString() {
      int[] objectArray = { 1, 2, 3 };

      // 1 > 2 > 3
      int[] operatorArray = { Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING };
      String expectedString = String.format(EXPECTED, StringUtils.EMPTY_STRING, Ranking.COMPARABLE_OPERATOR, Ranking.COMPARABLE_OPERATOR,
            StringUtils.EMPTY_STRING);
      assertEquals(expectedString.trim(), new Ranking(objectArray, operatorArray).toString().trim());

      // { 1 , 2 } > 3
      expectedString = String.format(EXPECTED, Ranking.OPENING_BRACKETS_OPERATOR, Ranking.NOT_COMPARABLE_OPERATOR,
            Ranking.CLOSING_BRACKETS_OPERATOR + StringUtils.EMPTY_STRING + Ranking.COMPARABLE_OPERATOR, StringUtils.EMPTY_STRING);
      operatorArray = new int[] { Ranking.OPENING_BRACKETS_ENCODING, Ranking.NOT_COMPARABLE_ENCODING,
            Ranking.CLOSING_BRACKETS_ENCODING * 10 + Ranking.COMPARABLE_ENCODING };


      assertEquals(expectedString.trim(), new Ranking(objectArray, operatorArray).toString().trim());

      // 1 > { 2 , 3 }
      expectedString = String.format(EXPECTED, StringUtils.EMPTY_STRING,
            Ranking.COMPARABLE_OPERATOR + StringUtils.EMPTY_STRING + Ranking.OPENING_BRACKETS_OPERATOR, Ranking.NOT_COMPARABLE_OPERATOR,
            Ranking.CLOSING_BRACKETS_OPERATOR);
      operatorArray = new int[] { Ranking.COMPARABLE_ENCODING * 10 + Ranking.OPENING_BRACKETS_ENCODING, Ranking.NOT_COMPARABLE_ENCODING,
            Ranking.CLOSING_BRACKETS_ENCODING };


      assertEquals(expectedString.trim(), new Ranking(objectArray, operatorArray).toString().trim());
   }
}
