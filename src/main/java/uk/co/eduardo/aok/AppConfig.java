package uk.co.eduardo.aok;

/**
 * Configuration settings for the application.
 *
 * @author Ed
 */
@SuppressWarnings( "nls" )
class AppConfig
{
   static final String CLIENT_ID = "0739eb69b49148098a77c2841fdc7eb5";

   static final String CLIENT_SECRET = "b9f24f17d1ad42ad9e4aa46063df5132";

   private AppConfig()
   {
      throw new IllegalStateException( "Cannot instantiate" );
   }
}
