package uk.co.eduardo.aok;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Override some defaults of a standard combo box.
 *
 * @author Ed
 */
public class ComboBoxUI extends BasicComboBoxUI
{
   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( "nls" )
   protected JButton createArrowButton()
   {
      final JButton button = new BasicArrowButton( SwingConstants.SOUTH,
                                                   UIManager.getColor( "ComboBox.buttonBackground" ),
                                                   UIManager.getColor( "ComboBox.buttonShadow" ),
                                                   UIManager.getColor( "ComboBox.buttonDarkShadow" ),
                                                   UIManager.getColor( "ComboBox.buttonHighlight" ) );
      button.setBorder( UIManager.getBorder( "ComboBox.border" ) );
      button.setName( "ComboBox.arrowButton" );
      return button;
   }
}
