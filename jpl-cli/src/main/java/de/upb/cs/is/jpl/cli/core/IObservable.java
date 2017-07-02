package de.upb.cs.is.jpl.cli.core;


import java.util.Observer;


/**
 * IObservable is an interface implementing all public functions implemented by
 * {@link java.util.Observable} in order to make these accessible from an interface, which extends
 * IOberservable.
 * 
 * The comments are copied from the Java documentation and slightly adapted to our style.
 * 
 *
 */
public interface IObservable {

   /**
    * Adds an observer to the set of observers for this object, provided that it is not the same as
    * some observer already in the set. The order in which notifications will be delivered to
    * multiple observers is not specified. See the class comment.
    *
    * @param o an observer to be added
    * @throws NullPointerException if the parameter o is null
    */
   public void addObserver(Observer o);


   /**
    * Deletes an observer from the set of observers of this object. Passing {@code null} to this
    * method will have no effect.
    * 
    * @param o the observer to be deleted
    */
   public void deleteObserver(Observer o);


   /**
    * If this object has changed, as indicated by the {@code hasChanged} method, then notify all of
    * its observers and then call the {@code clearChanged} method to indicate that this object has
    * no longer changed.
    * <p>
    * Each observer has its {@code update} method called with two arguments: this observable object
    * and {@code null}. In other words, this method is equivalent to: <blockquote><tt>
    * notifyObservers(null)</tt></blockquote>
    *
    */
   public void notifyObservers();


   /**
    * If this object has changed, as indicated by the {@code hasChanged} method, then notify all of
    * its observers and then call the {@code clearChanged} method to indicate that this object has
    * no longer changed.
    * <p>
    * Each observer has its {@code update} method called with two arguments: this observable object
    * and the {@code arg} argument.
    *
    * @param arg any object
    */
   public void notifyObservers(Object arg);


   /**
    * Clears the observer list so that this object no longer has any observers.
    */
   public void deleteObservers();


   /**
    * Tests if this object has changed.
    *
    * @return {@code true} if and only if the {@code setChanged} method has been called more
    *         recently than the {@code clearChanged} method on this object; {@code false} otherwise.
    */
   public boolean hasChanged();


   /**
    * Returns the number of observers of this <tt>Observable</tt> object.
    *
    * @return the number of observers of this object
    */
   public int countObservers();
}
