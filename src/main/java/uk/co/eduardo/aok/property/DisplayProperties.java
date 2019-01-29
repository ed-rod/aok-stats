package uk.co.eduardo.aok.property;

import javax.swing.JComponent;

import uk.co.eduardo.aok.IGUser;
import uk.co.eduardo.aok.MediaOrder;

/**
 * Holds the standard display properties.
 *
 * @author Ed
 */
@SuppressWarnings( "nls" )
public class DisplayProperties
{
   private DisplayProperties()
   {
      // Prevent instantiation.
      throw new IllegalStateException();
   }

   /**
    * The selected user.
    */
   public static final Property< IGUser > SELECTED_USER = new Property<>( IGUser.class, "selected_user" );

   /**
    * Property for the main view list content.
    */
   public static final Property< JComponent > LIST_CONTENT = new Property<>( JComponent.class, "list_view_content" );

   /**
    * Sort order for posts.
    */
   public static final Property< MediaOrder > MEDIA_ORDER = new Property<>( MediaOrder.class, "media_order_sort" );

   /**
    * Thumbnail size of posts in pixels.
    */
   public static final Property< Integer > THUMBNAIL_SIZE = new Property<>( Integer.class, "thumbnail_size" );
}
