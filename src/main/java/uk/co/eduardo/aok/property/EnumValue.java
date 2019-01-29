package uk.co.eduardo.aok.property;

/**
 * A property value that can store enumerated types.
 *
 * @author Ed
 * @param <E> the type of the enumeration.
 */
public class EnumValue< E extends Enum< E > > extends AbstractPropertyValue< E >
{
   /**
    * Initializes a new EnumValue object.
    *
    * @param value the initial value.
    */
   public EnumValue( final E value )
   {
      super( value );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( "nls" )
   public String toString()
   {
      return "\"" + getValue() + "\"";
   }
}
