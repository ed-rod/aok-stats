package uk.co.eduardo.aok;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * UI for the gallery buttons.
 *
 * @author Ed
 */
public class GalleryButtonUI extends BasicButtonUI
{
   private static final Color SELECT_COLOR = new Color( 77, 182, 172 );

   /**
    * {@inheritDoc}
    */
   @Override
   protected void paintButtonPressed( final Graphics g, final AbstractButton b )
   {
      if( b.isContentAreaFilled() )
      {
         g.setColor( getSelectColor() );
         g.fillRect( 0, 0, b.getWidth(), b.getHeight() );
      }
   }

   private Color getSelectColor()
   {
      return SELECT_COLOR;
   }
}
