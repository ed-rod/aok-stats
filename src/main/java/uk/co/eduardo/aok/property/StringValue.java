package uk.co.eduardo.aok.property;

/**
 * A simple string value for a property.
 *
 * @author Ed
 */
public class StringValue extends AbstractPropertyValue< String >
{
   /**
    * Initializes a new StringValue object.
    *
    * @param value the value to set.
    */
   public StringValue( final String value )
   {
      super( value );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "\"" + getValue().replace( "\"", "\\\"" ) + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
}
