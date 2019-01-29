package uk.co.eduardo.aok;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Maintains a cache of images.
 *
 * @author Ed
 */
public class ImageUtilities
{
   private static final int DEFAULT_SIZE = -1;

   private static final Map< String, Map< Integer, BufferedImage > > IMAGE_CACHE = new HashMap<>();

   private ImageUtilities()
   {
      // prevent instantiation.
   }

   /**
    * Gets the image at the given URL.
    *
    * @param url the URL to fetch.
    * @return the image at the URL.
    */
   public static BufferedImage getImage( final String url )
   {
      return getImage( url, DEFAULT_SIZE );
   }

   /**
    * Gets the image at the given URL and resizes it. The image will be resized to fit a square of dimension
    * <code>size x size</code>
    *
    * @param url the URL to fetch.
    * @param size the size of the resized image to get.
    * @return the image at the URL.
    */
   public static BufferedImage getImage( final String url, final int size )
   {
      // Check to see if the image already exists
      Map< Integer, BufferedImage > urlMap = IMAGE_CACHE.get( url );
      if( urlMap == null )
      {
         urlMap = createUrlMap( url );
      }

      BufferedImage image = urlMap.get( size );
      if( image == null )
      {
         image = createImage( url, urlMap, size );
      }
      return image;
   }

   private static Map< Integer, BufferedImage > createUrlMap( final String url )
   {
      final Map< Integer, BufferedImage > urlMap = new HashMap<>();
      IMAGE_CACHE.put( url, urlMap );
      return urlMap;
   }

   private static BufferedImage createImage( final String url, final Map< Integer, BufferedImage > urlMap, final int size )
   {
      // If we already have the default size image, get that and resize it.
      BufferedImage original = urlMap.get( DEFAULT_SIZE );
      if( original == null )
      {
         original = fetchImage( url );
         urlMap.put( DEFAULT_SIZE, original );
      }

      if( original != null )
      {
         final BufferedImage resized = resize( original, size );
         urlMap.put( size, resized );
         return resized;
      }

      return null;
   }

   private static BufferedImage fetchImage( final String urlString )
   {
      try
      {
         final URL url = new URL( urlString );
         return ImageIO.read( url );
      }
      catch( final IOException exception )
      {
         exception.printStackTrace();
      }
      return null;
   }

   private static BufferedImage resize( final BufferedImage original, final int size )
   {
      if( ( original.getWidth() == size ) || ( size == -1 ) )
      {
         return original;
      }

      final BufferedImage resized = new BufferedImage( size, size, BufferedImage.TYPE_INT_ARGB );
      final Graphics2D g2d = resized.createGraphics();
      g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
      g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
      g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
      g2d.drawImage( original, 0, 0, size, size, 0, 0, original.getWidth(), original.getHeight(), null );

      return resized;
   }
}
