package uk.co.eduardo.aok.property;

/**
 * Represents a value that itself can hold other properties.
 *
 * @author Ed
 */
public class NestedValue extends AbstractPropertyValue< PropertyMap >
{
   /**
    * Initializes a new NestedValue object.
    *
    * @param value the value to set.
    */
   public NestedValue( final PropertyMap value )
   {
      super( value );
   }
}
