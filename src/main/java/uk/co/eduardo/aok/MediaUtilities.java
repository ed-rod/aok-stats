package uk.co.eduardo.aok;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.exceptions.InstagramException;

/**
 * Maintains a cache of media requests.
 *
 * @author Ed
 */
public class MediaUtilities
{
   private static final Logger LOGGER = Logger.getLogger( MediaUtilities.class.getName() );

   private static final Map< String, MediaFeed > MEDIAFEED_CACHE = new HashMap<>();

   private MediaUtilities()
   {
      // Prevent instantiation.
   }

   /**
    * Gets the media feed for the selected user.
    *
    * @param instagram the instagram instance.
    * @return the media feed.
    */
   public static MediaFeed getMediaFeed( final Instagram instagram )
   {
      final String accessToken = instagram.getAccessToken().getToken();
      final IGUser user = IGUser.from( accessToken );

      // Check to see if we have a cached value
      MediaFeed feed = MEDIAFEED_CACHE.get( accessToken );
      if( feed == null )
      {
         feed = createMediaFeed( user, instagram );
      }
      return feed;
   }

   private static MediaFeed createMediaFeed( final IGUser user, final Instagram instagram )
   {
      try
      {
         final String accessToken = user.getAccessToken();
         final String userId = user.getUserId();
         final MediaFeed feed = instagram.getRecentMediaFeed( userId, 1_000, null, null, null, null );
         MEDIAFEED_CACHE.put( accessToken, feed );
         return feed;
      }
      catch( final InstagramException exception )
      {
         LOGGER.log( Level.SEVERE, exception.getLocalizedMessage(), exception );
      }
      return null;
   }
}
