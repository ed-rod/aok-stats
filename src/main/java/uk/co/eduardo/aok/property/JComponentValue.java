package uk.co.eduardo.aok.property;

import javax.swing.JComponent;

/**
 * A property value that can store a Swing component.
 *
 * @author Ed
 */
public class JComponentValue extends AbstractPropertyValue< JComponent >
{
   /**
    * Initializes a new EnumValue object.
    *
    * @param value the initial value.
    */
   public JComponentValue( final JComponent value )
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
