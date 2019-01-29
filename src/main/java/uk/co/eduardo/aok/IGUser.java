package uk.co.eduardo.aok;

/**
 * Enumeration of the users that are available in the system.
 *
 * @author Ed
 */
@SuppressWarnings( "nls" )
public enum IGUser
{
   /**
    * Eduardo Rodrigues (edmoregis).
    */
   EDMOREGIS( "1687045706.0739eb6.c65ce49c94f04317ac6ed50a2211fc2a" ),

   /**
    * Abagail Greening (abagail.greening)
    */
   ABAGAIL( "5583929526.0739eb6.329e983eadcd4608b45cda8912f21663" ),

   /**
    * Abagail's Oxford Kitchen (abagails_oxford_kitchen)
    */
   AOK( "9240466857.0739eb6.5d85ef50e40841f688427f5be2c4d052" );

   private final String accessToken;

   IGUser( final String accessToken )
   {
      this.accessToken = accessToken;
   }

   /**
    * Gets the access token.
    *
    * @return the access token.
    */
   public String getAccessToken()
   {
      return this.accessToken;
   }

   /**
    * Gets the user ID.
    *
    * @return the user ID.
    */
   public String getUserId()
   {
      final int dotIndex = this.accessToken.indexOf( '.' );
      if( dotIndex >= 0 )
      {
         return this.accessToken.substring( 0, dotIndex );
      }
      return "";
   }

   /**
    * Gets a user from their access token.
    *
    * @param accessToken the access token.
    * @return the associated user.
    */
   public static IGUser from( final String accessToken )
   {
      for( final IGUser user : IGUser.values() )
      {
         if( user.accessToken.equals( accessToken ) )
         {
            return user;
         }
      }
      return null;
   }
}
