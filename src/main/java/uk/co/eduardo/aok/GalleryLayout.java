package uk.co.eduardo.aok;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A layout manager that arranges gallery items in a grid.
 *
 * @author Ed
 */
public class GalleryLayout implements LayoutManager
{
   private final int size;

   private final int padding;

   private final int rightPadding = 24;

   /**
    * Initializes a new GalleryLayout object.
    *
    * @param size the size of each tile in the gallery.
    */
   public GalleryLayout( final int size )
   {
      this.size = size;
      this.padding = 10;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addLayoutComponent( final String name, final Component comp )
   {
      // Do nothing.
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void removeLayoutComponent( final Component comp )
   {
      // Do nothing.
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Dimension preferredLayoutSize( final Container parent )
   {
      synchronized( parent.getTreeLock() )
      {
         // We have padding on the right (leave space for scrollbar) and then padding between each gallery item.
         final int parentWidth = parent.getWidth();
         final int paddedItemSize = this.size + this.padding;
         final int availableWidth = Math.max( 0, ( parentWidth - this.rightPadding ) + this.padding );
         final int columns = Math.max( 1, availableWidth / paddedItemSize );
         final int width = ( columns * paddedItemSize ) + this.padding;

         final int count = parent.getComponentCount();
         final int rows = (int) Math.ceil( (float) count / columns );
         final int height = ( rows * paddedItemSize ) - this.padding;
         return new Dimension( width, height );
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Dimension minimumLayoutSize( final Container parent )
   {
      return preferredLayoutSize( parent );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void layoutContainer( final Container parent )
   {
      synchronized( parent.getTreeLock() )
      {
         // We have 2* padding on the right (leave space for scrollbar) and then padding between each gallery item.
         final int parentWidth = parent.getWidth();
         final int paddedItemSize = this.size + this.padding;
         final int availableWidth = Math.max( 0, ( parentWidth - this.rightPadding ) + this.padding );
         final int columns = Math.max( 1, availableWidth / paddedItemSize );

         for( int i = 0; i < parent.getComponentCount(); i++ )
         {
            final Component galleryItem = parent.getComponent( i );
            final int row = i / columns;
            final int col = i - ( row * columns );
            final int x = col * paddedItemSize;
            final int y = row * paddedItemSize;
            galleryItem.setBounds( x, y, paddedItemSize, paddedItemSize );
         }
      }
   }
}
