package uk.co.eduardo.aok.property;

/**
 * Property to store integer.
 *
 * @author Ed
 */
public class IntegerValue extends AbstractPropertyValue< Integer >
{
   /**
    * Initializes a new IntegerValue object.
    *
    * @param value the initial value.
    */
   public IntegerValue( final Integer value )
   {
      super( value );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return Integer.toString( getValue() );
   }
}
