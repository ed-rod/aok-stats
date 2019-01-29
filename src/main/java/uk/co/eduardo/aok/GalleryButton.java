package uk.co.eduardo.aok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import javax.swing.JButton;

import org.jinstagram.entity.users.feed.MediaFeedData;

/**
 * Gallery image item.
 *
 * @author Ed
 */
public class GalleryButton extends JButton
{
   private final MediaFeedData media;

   private final int pad = 10;

   private Font largeFont;

   /**
    * Initializes a new GalleryButton object.
    *
    * @param media the media element from which to create the gallery button.
    */
   @SuppressWarnings( "nls" )
   public GalleryButton( final MediaFeedData media )
   {
      this.media = media;
      setUI( new GalleryButtonUI() );
      setBorderPainted( false );

      // Strip hashtags from the caption. If there are no hashtags, take the full text.
      int end = media.getCaption().getText().indexOf( '#' );
      if( end < 0 )
      {
         end = media.getCaption().getText().length();
      }
      final String caption = media.getCaption().getText().substring( 0, end );
      setToolTipText( MessageFormat.format( "<html><b>({0})</b> <p>{1}</p><html>", media.getLikes().getCount(), caption ) );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void paintComponent( final Graphics g )
   {
      super.paintComponent( g );
      setupFont( g );

      final Font oldFont = g.getFont();
      final Color oldColor = g.getColor();
      try
      {
         final Graphics2D g2d = (Graphics2D) g;

         final int width = getWidth() - this.pad - this.pad;
         if( width > 0 )
         {
            final String thumbnailurl = this.media.getImages().getStandardResolution().getImageUrl();
            final BufferedImage image = ImageUtilities.getImage( thumbnailurl, width );
            g.drawImage( image, this.pad, this.pad, null );

            // Create the text overlay]
            g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            final FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
            final String string = Integer.toString( this.media.getLikes().getCount() );
            final TextLayout layout = new TextLayout( string, this.largeFont, frc );

            final int xStart = 20; // (int) bounds.getX();
            final int yStart = ( 20 + (int) layout.getAscent() ) - (int) layout.getDescent(); // (int) bounds.getY();

            g.setColor( new Color( 255, 255, 255, 40 ) );
            final int blurSize = 5;
            for( int x = 0; x < blurSize; x++ )
            {
               for( int y = 0; y < blurSize; y++ )
               {
                  layout.draw( g2d, xStart + x, yStart + y );
               }
            }
            g.setColor( Color.BLACK );
            layout.draw( g2d, xStart + ( blurSize / 2 ), yStart + ( blurSize / 2 ) );
         }
      }
      finally
      {
         g.setFont( oldFont );
         g.setColor( oldColor );
      }
   }

   private void setupFont( final Graphics g )
   {
      if( this.largeFont == null )
      {
         this.largeFont = g.getFont().deriveFont( g.getFont().getSize() * 1.5f );
      }
   }
}
