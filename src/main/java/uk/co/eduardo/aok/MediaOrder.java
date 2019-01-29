package uk.co.eduardo.aok;

import java.util.Comparator;

import org.jinstagram.entity.users.feed.MediaFeedData;

/**
 * Sorting for media.
 *
 * @author Ed
 */
public enum MediaOrder
{
   /**
    * Sorts posts chronologically (newest first).
    */
   CHRONOLOGICAL( "Date" )
   {
      @Override
      int compareMedia( final MediaFeedData o1, final MediaFeedData o2 )
      {
         final String s1 = o1.getCreatedTime();
         final String s2 = o2.getCreatedTime();
         final long l1 = Long.parseLong( s1 );
         final long l2 = Long.parseLong( s2 );
         return (int) ( l2 - l1 );
      }
   },

   /**
    * Sorts posts by popularity. Most popular first.
    */
   POPULAR( "Popularity" )
   {
      @Override
      int compareMedia( final MediaFeedData o1, final MediaFeedData o2 )
      {
         return o2.getLikes().getCount() - o1.getLikes().getCount();
      }
   };

   private final String name;

   MediaOrder( final String name )
   {
      this.name = name;
   }

   Comparator< MediaFeedData > getComparator()
   {
      return new Comparator< MediaFeedData >()
      {
         @Override
         public int compare( final MediaFeedData o1, final MediaFeedData o2 )
         {
            return compareMedia( o1, o2 );
         }
      };
   }

   abstract int compareMedia( final MediaFeedData o1, final MediaFeedData o2 );

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return this.name;
   }
}
