package uk.co.eduardo.aok.property;

import java.util.List;

/**
 * Represents a property value.
 *
 * @author Ed
 * @param <T> the type of the property.
 */
public interface PropertyValue< T >
{
   /**
    * Listener interface that receives notification that the value has changed.
    * 
    * @param <T> the type of the property.
    */
   public static interface PropertyListener< T >
   {
      /**
       * Notification that the value has changed.
       *
       * @param newValue the new value.
       */
      void valueChanged( T newValue );
   }

   /**
    * @return the value of the property.
    */
   T getValue();

   /**
    * @param value the value to set.
    */
   void setValue( final T value );

   /**
    * Adds a listener.
    *
    * @param listener the listener to add.
    */
   void addPropertyListener( PropertyListener< T > listener );

   /**
    * Removes a listener.
    *
    * @param listener the listener to remove.
    */
   void removePropertyListener( PropertyListener< T > listener );

   /**
    * Gets all the listeners for this property value.
    *
    * @return the list of listeners.
    */
   List< PropertyListener< T > > getPropertyListeners();

}
