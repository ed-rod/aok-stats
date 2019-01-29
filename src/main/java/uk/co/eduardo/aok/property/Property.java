package uk.co.eduardo.aok.property;

import java.util.Objects;

/**
 * Represents the property key.
 *
 * @author Ed
 * @param <T> the type of the property.
 */
public class Property< T >
{
   private final Class< T > type;

   private final String name;

   /**
    * Initializes a new Property object.
    *
    * @param type the type of the property.
    * @param name the name of the property.
    */
   public Property( final Class< T > type, final String name )
   {
      this.type = type;
      this.name = name;
   }

   /**
    * Gets the name of the property.
    *
    * @return the name.
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * Gets the type of the property.
    *
    * @return the type.
    */
   public Class< T > getType()
   {
      return this.type;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return getName();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return Objects.hash( this.type, this.name );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals( final Object obj )
   {
      if( obj instanceof Property )
      {
         final Property< ? > other = (Property< ? >) obj;
         return Objects.equals( this.type, other.type ) && Objects.equals( this.name, other.name );
      }
      return false;
   }
}
