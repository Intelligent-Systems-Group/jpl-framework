package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.math.MatrixDecompositionException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * An implementation of the ordinal least squares as base learner for regression. It saves all
 * features in a {@link DenseDoubleMatrix} and than solves the equation with help of a
 * QR-Decomposition ({@link IMatrix#getQRDecomposition}).
 *
 * @author Sebastian Gottschalk
 */
public class LinearRegression extends ABaselearnerAlgorithm<LinearRegressionConfiguration> {


   /**
    * Creates a new linear regression base learner with the enum identifier.
    */
   public LinearRegression() {
      super(EBaseLearner.LINEAR_REGRESSION.getBaseLearnerIdentifier());
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      BaselearnerDataset simpleDataset = (BaselearnerDataset) dataset;

      IMatrix x = new DenseDoubleMatrix(formatFeaturesForQRDecomposition(simpleDataset));

      // create matrix from vector
      IMatrix ys = new DenseDoubleMatrix(simpleDataset.getCorrectResults(), 1);

      Pair<IMatrix, IMatrix> qrMatrix;
      try {
         qrMatrix = x.getQRDecomposition();

         // find least squares solution
         IMatrix qMatrix = qrMatrix.getFirst().createCopy();
         IMatrix rMatrix = qrMatrix.getSecond().createCopy();

         // Calculate Q^T * b
         qMatrix.transponse();
         qMatrix.multiply(ys);

         // Solve
         IVector solution = rMatrix.solve(qMatrix.getColumnVector(0));

         return new LinearRegressionLearningModel(solution);

      } catch (MatrixDecompositionException e) {
         throw new TrainModelsFailedException(e.getMessage(), e);
      }
   }


   @Override
   public LinearRegressionLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (LinearRegressionLearningModel) super.train(dataset);
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new LinearRegressionConfiguration();
   }


   /**
    * Format the features of the given {@link IDataset} so that they can be used by QRDecomposition
    * ({@link IMatrix#getQRDecomposition}).
    *
    * @param dataset the dataset which holds the features
    * @return the formatted features as matrix
    */
   private double[][] formatFeaturesForQRDecomposition(BaselearnerDataset dataset) {
      double[][] returnMatrix = new double[dataset.getNumberOfInstances()][dataset.getNumberOfFeatures() + 1];
      double[][] currentFeatures = dataset.getFeatureVectors();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         for (int j = 0; j < dataset.getNumberOfFeatures() + 1; j++) {
            if (j == 0) {
               returnMatrix[i][j] = 1;
            } else {
               returnMatrix[i][j] = currentFeatures[i][j - 1];
            }
         }
      }
      return returnMatrix;
   }
}
