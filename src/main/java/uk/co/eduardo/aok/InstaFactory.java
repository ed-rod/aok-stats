package uk.co.eduardo.aok;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;

/**
 * Creates an Instagram instance.
 *
 * @author Ed
 */
public class InstaFactory
{
   private final IGUser user;

   /**
    * Initializes a new InstaFactory object.
    *
    * @param user the user for which we want to retrieve the data.
    */
   public InstaFactory( final IGUser user )
   {
      this.user = user;
   }

   /**
    * Creates an Instagram instance.
    *
    * @return an new Instagram instance.
    */
   public Instagram create()
   {
      final String accessToken = this.user.getAccessToken();
      final Instagram instagram = new Instagram( new Token( accessToken, AppConfig.CLIENT_SECRET ) );
      return instagram;
   }
}
