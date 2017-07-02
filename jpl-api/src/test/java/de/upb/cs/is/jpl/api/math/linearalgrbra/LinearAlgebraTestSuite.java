package de.upb.cs.is.jpl.api.math.linearalgrbra;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Test suite for all linear algebra related classes.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ DenseDoubleVectorTest.class, DenseDoubleMatrixTest.class, SparseDoubleVectorTest.class, SparseDoubleMatrixTest.class })
public class LinearAlgebraTestSuite {

}
