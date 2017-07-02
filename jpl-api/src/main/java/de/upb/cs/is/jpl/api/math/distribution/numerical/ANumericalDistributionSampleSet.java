package de.upb.cs.is.jpl.api.math.distribution.numerical;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.upb.cs.is.jpl.api.math.distribution.ADistributionSampleSet;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IIntegerDistribution;


/**
 * This abstract class represents a {@link ANumericalDistributionSampleSet}, storing samples in the
 * form of a {@link Number}.
 * 
 * @author Alexander Hetzer
 *
 * @param <SPACE> the space (domain) of the distribution whose samples should be contained inside
 *           this sample set, e.g. Integer for {@link IIntegerDistribution}s. Has to be a subclass
 *           of {@link Number}.
 */
public abstract class ANumericalDistributionSampleSet<SPACE extends Number> extends ADistributionSampleSet<SPACE> {

   private static final String ERROR_INDEX_OUT_OF_BOUNDS = "The given index %d is out of bounds.";
   private static final String ERROR_SAMPLE_SET_NULL = "The given samples must not be null.";

   private List<SPACE> samples;


   /**
    * Creates a new empty {@link ANumericalDistributionSampleSet}.
    */
   public ANumericalDistributionSampleSet() {
      this.samples = new ArrayList<>();
   }


   /**
    * Creates a new {@link ANumericalDistributionSampleSet} initialized with the given {@link List}
    * of samples. Note that this constructor will create a copy of this list.
    * 
    * @param samples the {@link List} of samples, this sample set should be initialized with
    */
   public ANumericalDistributionSampleSet(List<SPACE> samples) {
      this.samples = new ArrayList<>(samples);
   }


   /**
    * Creates a new {@link ANumericalDistributionSampleSet} initialized with the given
    * {@link ArrayList} of samples. Note that this constructor does NOT copy the list.
    * 
    * @param samples the {@link ArrayList} of samples, this sample set should be initilized with
    */
   public ANumericalDistributionSampleSet(ArrayList<SPACE> samples) {
      this.samples = samples;
   }


   @Override
   public void addSample(SPACE sample) {
      Objects.requireNonNull(sample, ERROR_SAMPLE_SET_NULL);
      samples.add(sample);
   }


   @Override
   public void addSampleAtIndex(SPACE sample, int index) {
      Objects.requireNonNull(sample, ERROR_SAMPLE_SET_NULL);

   }


   @Override
   public SPACE getSample(int index) {
      if (index < 0 || index >= samples.size()) {
         throw new IndexOutOfBoundsException(String.format(ERROR_INDEX_OUT_OF_BOUNDS, index));
      }
      return samples.get(index);
   }


   @Override
   public int getSize() {
      return samples.size();
   }


}
