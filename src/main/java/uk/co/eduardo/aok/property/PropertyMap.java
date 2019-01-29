package uk.co.eduardo.aok.property;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uk.co.eduardo.aok.property.PropertyValue.PropertyListener;

/**
 * A map of properties.
 * <p>
 * Values are stored against a property. These values are saved in a set so duplicates cannot be added.
 * </p>
 *
 * @author Ed
 */
public class PropertyMap
{
   private final Map< Property< ? >, PropertyValue< ? > > map = new LinkedHashMap<>();

   /**
    * Gets all of the properties that have been added to the map.
    *
    * @return all the properties in the map.
    */
   public final Set< Property< ? > > getProperties()
   {
      return this.map.keySet();
   }

   /**
    * Adds a property to the map. If the property already exists then this throwsn an exception.
    *
    * @param property the property to set.
    * @param value the value to set.
    */
   public < T > void addPropertyValue( final Property< T > property, final PropertyValue< T > value )
   {
      if( hasProperty( property ) )
      {
         throw new IllegalStateException();
      }

      if( ( value != null ) && ( value.getValue() != null ) )
      {
         this.map.put( property, value );
      }
   }

   /**
    * Sets the property value. If the property has not been previously added with {@link #addPropertyValue(Property, PropertyValue)}
    * then this method throws an exception.
    *
    * @param property the property to set.
    * @param value the value to set.
    */
   public < T > void setPropertyValue( final Property< T > property, final T value )
   {
      if( !hasProperty( property ) )
      {
         throw new IllegalStateException();
      }

      if( ( value != null ) )
      {
         final PropertyValue< T > propertyValue = internalGetPropertyValue( property );
         propertyValue.setValue( value );
      }
   }

   /**
    * Gets the value associated with the property.
    *
    * @param property the property.
    * @return the first value associated with the property or <code>null</code> if the property does has not been added..
    */
   public < T > T getPropertyValue( final Property< T > property )
   {
      if( hasProperty( property ) )
      {
         final PropertyValue< T > propertyValue = internalGetPropertyValue( property );
         return propertyValue.getValue();
      }
      return null;
   }

   /**
    * Removes a property from the map. This removes all its values and the property itself.
    *
    * @param property the property to remove.
    */
   public < T > void removeProperty( final Property< T > property )
   {
      this.map.remove( property );
   }

   /**
    * Removes all properties and values from the map.
    */
   public void clear()
   {
      this.map.clear();
   }

   /**
    * Test to see whether or not this property map is empty.
    * <p>
    * The map is empty if it does not contain any properties. That is, if {@link #getProperties()} is an empty set.
    * </p>
    *
    * @return whether or not the map is empty.
    */
   public boolean isEmpty()
   {
      return this.map.isEmpty();
   }

   /**
    * Tests to see if the property map contains a particular property.
    *
    * @param property the property to check.
    * @return whether or not the property is in the map.
    */
   public < T > boolean hasProperty( final Property< T > property )
   {
      return this.map.containsKey( property );
   }

   /**
    * Adds a listener.
    *
    * @param property the property.
    * @param listener the listener to add.
    */
   public < T > void addPropertyListener( final Property< T > property, final PropertyListener< T > listener )
   {
      if( hasProperty( property ) )
      {
         final PropertyValue< T > propertyValue = internalGetPropertyValue( property );
         propertyValue.addPropertyListener( listener );
      }
   }

   /**
    * Removes a listener.
    *
    * @param property the property.
    * @param listener the listener to remove.
    */
   public < T > void removePropertyListener( final Property< T > property, final PropertyListener< T > listener )
   {
      if( hasProperty( property ) )
      {
         final PropertyValue< T > propertyValue = internalGetPropertyValue( property );
         propertyValue.removePropertyListener( listener );
      }
   }

   /**
    * Gets the listeners.
    *
    * @param property the property.
    * @return the listeners.
    */
   public < T > List< PropertyListener< T > > getPropertyListeners( final Property< T > property )
   {
      if( hasProperty( property ) )
      {
         final PropertyValue< T > propertyValue = internalGetPropertyValue( property );
         return propertyValue.getPropertyListeners();
      }
      return Collections.emptyList();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return Objects.hashCode( getProperties() );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals( final Object obj )
   {
      if( obj instanceof PropertyMap )
      {
         final PropertyMap other = (PropertyMap) obj;

         // It looks like MultiMap inherits Object.equals implementation so we check the maps for equality here.

         // Do they have the same properties?
         final Set< Property< ? > > thisProperties = this.getProperties();
         if( !thisProperties.equals( other.getProperties() ) )
         {
            return false;
         }

         // Check each property
         for( final Property< ? > property : thisProperties )
         {
            if( !Objects.equals( getPropertyValue( property ), other.getPropertyValue( property ) ) )
            {
               return false;
            }
         }
         return true;
      }
      return false;
   }

   private < T > PropertyValue< T > internalGetPropertyValue( final Property< T > property )
   {
      if( hasProperty( property ) )
      {
         // Update the value
         @SuppressWarnings( "unchecked" )
         final PropertyValue< T > propertyValue = (PropertyValue< T >) this.map.get( property );
         return propertyValue;
      }
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return toString( this, new StringBuilder(), 1 );
   }

   @SuppressWarnings( "nls" )
   private static final String COMMA = ", ";

   @SuppressWarnings( "nls" )
   private static final String NEWLINE = System.getProperty( "line.separator" );

   @SuppressWarnings( "nls" )
   private static final String INDENT = "   ";

   private static String toString( final PropertyMap props, final StringBuilder builder, final int level )
   {
      builder.append( '{' );
      builder.append( NEWLINE );

      for( final Property< ? > key : props.map.keySet() )
      {
         addIndent( builder, level );
         builder.append( '"' );
         builder.append( key.toString() );
         builder.append( '"' );
         builder.append( ':' );

         final PropertyValue< ? > propertyValue = props.map.get( key );
         if( propertyValue.getValue() instanceof PropertyMap )
         {
            toString( (PropertyMap) propertyValue.getValue(), builder, level + 1 );
         }
         else
         {
            builder.append( propertyValue.toString() );
         }
         builder.append( COMMA );
         builder.append( NEWLINE );
      }
      if( !props.getProperties().isEmpty() )
      {
         builder.setLength( builder.length() - COMMA.length() - NEWLINE.length() );
      }

      builder.append( NEWLINE );
      addIndent( builder, level - 1 );
      builder.append( '}' );
      return builder.toString();
   }

   private static void addIndent( final StringBuilder builder, final int depth )
   {
      for( int i = 0; i < depth; i++ )
      {
         builder.append( INDENT );
      }
   }
}
