package uk.co.eduardo.aok.property;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract implementation of a property value.
 *
 * @author Ed
 * @param <T> the type of the property.
 */
public class AbstractPropertyValue< T > implements PropertyValue< T >
{
   private final List< PropertyListener< T > > listeners = new CopyOnWriteArrayList<>();

   private T value;

   /**
    * Initializes a new AbstractPropertyValue object.
    *
    * @param value the value to set.
    */
   public AbstractPropertyValue( final T value )
   {
      this.value = value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public T getValue()
   {
      return this.value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setValue( final T value )
   {
      this.value = value;

      for( final PropertyListener< T > listener : this.listeners )
      {
         listener.valueChanged( value );
      }
   }

   /**
    * Adds a listener.
    *
    * @param listener the listener to add.
    */
   @Override
   public void addPropertyListener( final PropertyListener< T > listener )
   {
      if( ( listener != null ) && !this.listeners.contains( listener ) )
      {
         this.listeners.add( listener );
      }
   }

   /**
    * Removes a listener.
    *
    * @param listener the listener to remove.
    */
   @Override
   public void removePropertyListener( final PropertyListener< T > listener )
   {
      this.listeners.remove( listener );
   }

   /**
    * Gets the listeners.
    *
    * @return the listeners.
    */
   @Override
   public List< PropertyListener< T > > getPropertyListeners()
   {
      return this.listeners;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return Objects.hash( this.value );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals( final Object obj )
   {
      if( this.getClass().isAssignableFrom( obj.getClass() ) )
      {
         return Objects.equals( this.value, ( (AbstractPropertyValue< ? >) obj ).value );
      }
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return this.value != null ? this.value.toString() : ""; //$NON-NLS-1$
   }
}
