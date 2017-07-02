package de.upb.cs.is.jpl.api.math.util;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class linearizes the two-dimensional array into a one-dimensional array and does the
 * appropriate subscripting calculations using internally stored row and column sizes. This file can
 * be used to simulate a 2d matrix which are larger than minimum size of a 2d {@link Array}. It can
 * be used to dynamically increase the size of the matrix which is restricted in a {@link Array}.
 * 
 * @author Pritha Gupta
 */

public class TwoDMatrixUtils {
   private int rows;
   private int cols;
   private List<Integer> data;

   private static final String SQUARE_BRACKET_OPEN = "[";
   private static final String SQUARE_BRACKET_CLOSE = "]";
   private static final String SEPERATOR_FOR_MATRIX_ELEMENTS = " , ";


   /**
    * Allocate a matrix with the indicated initial dimensions.
    * 
    * @param cols the column (horizontal or x) dimension for the matrix
    * @param rows the row (vertical or y) dimension for the matrix
    */
   public TwoDMatrixUtils(int rows, int cols) {
      this.rows = rows;
      this.cols = cols;
      this.data = new ArrayList<>();
      int size = rows * cols;
      int temp = 0;
      for (int i = 0; i < size; i++) {
         data.add(temp);
      }
   }


   /**
    * Calculates the index of the indicated row and column for a matrix with the indicated width.
    * This uses row-major ordering of the matrix elements.
    * 
    * @param col the column index of the desired element
    * @param row the row index of the desired element
    * @param width the width of the matrix, in row major case its the columns.
    * @return the row major index in the linearized list.
    */
   public static int getIndex(int row, int col, int width) {
      return row * width + col;
   }


   /**
    * Returns the rows of the 2D Matrix.
    * 
    * @return the rows of the 2D Matrix.
    */
   public int getRows() {
      return rows;
   }


   /**
    * Returns the columns of the 2D Matrix.
    * 
    * @return the columns of the 2D Matrix.
    */

   public int getCols() {
      return cols;
   }


   /**
    * Returns the element of the Matrix at provided row and column index.
    * 
    * @param rowIndex the row Index of the element to be returned.
    * @param colIndex the column Index of the element to be returned.
    * @return the matrix element at the provided indices.
    */
   public int get(int rowIndex, int colIndex) {
      return data.get(getIndex(rowIndex, colIndex, cols));
   }


   /**
    * Set the element of the matrix at the row and column index.
    * 
    * @param rowIndex the row Index of the element to be set.
    * @param colIndex the column Index of the element to be set.
    * @param value the value of the element to be set.
    * 
    */
   public void set(int rowIndex, int colIndex, int value) {
      data.set(getIndex(rowIndex, colIndex, cols), value);
   }


   /**
    * Add the value at the existing value of the element of the matrix at row and column index.
    * 
    * @param rowIndex the Row Index of the element to be set.
    * @param colIndex the Column Index of the element to be set.
    * @param value the value of the element to be added in the existing value.
    * 
    */
   public void addInExistingValue(int rowIndex, int colIndex, int value) {
      data.set(getIndex(rowIndex, colIndex, cols), get(rowIndex, colIndex) + value);
   }


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(StringUtils.LINE_BREAK);
      for (int i = 0; i < getRows(); i++) {
         builder.append(SQUARE_BRACKET_OPEN);
         for (int j = 0; j < getCols(); j++) {
            if (get(i, j) / 10 == 0)
               builder.append(0);
            builder.append(get(i, j));
            if (j < (getCols() - 1))
               builder.append(SEPERATOR_FOR_MATRIX_ELEMENTS);
         }
         builder.append(SQUARE_BRACKET_CLOSE);
         builder.append(StringUtils.LINE_BREAK);
      }
      return builder.toString();
   }

}