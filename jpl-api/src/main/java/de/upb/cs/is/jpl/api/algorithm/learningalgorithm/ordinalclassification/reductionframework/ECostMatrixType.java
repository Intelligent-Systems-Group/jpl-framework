package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework;


/**
 * Enumeration of all supported cost matrices which can be used for the
 * {@link OrdinalClassificationReductionFramework}.
 * 
 * <p>
 * For the cost matrix <code>C</code> it is important that they are V-shaped, that means that
 * <ul>
 * <li><code>C(i,j-1) &gt;= C(i,j)</code>, if <code>i &gt;= j</code></li>
 * <li><code>C(i,j) &lt;= C(i,j+1)</code>, if <code>i &lt;= j</code></li>
 * </ul>
 * 
 * 
 * @author Tanja Tornede
 *
 */
public enum ECostMatrixType {

   /**
    * A simple V-shaped classification matrix with entries
    * <p>
    * <code>C(i,j) = (i != j) ? 1 : 0</code>.
    */
   CLASSIFICATION_MATRIX("classification_matrix") {
      @Override
      public double computeCostMatrixEntry(int i, int j) {
         return i != j ? 1 : 0;
      }
   },

   /**
    * A V-shaped and also convex cost matrix, which reflects the ordering preferences better then
    * the classification matrix.
    * 
    * <p>
    * For a convex matrix <code>C</code> holds:
    * <p>
    * <code>C(i,j) - C(i,j-1) &lt;= C(i,j+1) - C(i,j)</code>
    */
   ABSOLUTE_COST_MATRIX("absolute_cost_matrix") {
      @Override
      public double computeCostMatrixEntry(int i, int j) {
         return Math.abs(i - j);
      }
   };

   private String identifier;


   private ECostMatrixType(String identifier) {
      this.identifier = identifier;
   }


   private String getCostMatrixTypeIdentifier() {
      return identifier;
   }


   /**
    * Returns the {@code CostMatrixType} {@link Enum} associated with given identifier.
    * 
    * @param costMatrixTypeIdentifier the identifier of the cost matrix type to search for
    * @return the type of the cost matrix of the given identifier if found, otherwise {@code null}
    *         if not found
    */
   public static ECostMatrixType getECostMatrixTypeByIdentifier(String costMatrixTypeIdentifier) {
      for (ECostMatrixType type : ECostMatrixType.values()) {
         if (type.getCostMatrixTypeIdentifier().equals(costMatrixTypeIdentifier)) {
            return type;
         }
      }
      return null;
   }


   /**
    * Returns entry (i,j) of the cost matrix. This matrix is a V-shaped cost matrix.
    * 
    * @param i the x-coordinate of the entry
    * @param j the y-coordinate of the entry
    * 
    * @return the entry of the cost matrix at the given position
    */
   public abstract double computeCostMatrixEntry(int i, int j);

}
