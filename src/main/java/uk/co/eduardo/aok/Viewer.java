package uk.co.eduardo.aok;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import uk.co.eduardo.aok.property.DisplayProperties;
import uk.co.eduardo.aok.property.EnumValue;
import uk.co.eduardo.aok.property.IntegerValue;
import uk.co.eduardo.aok.property.JComponentValue;
import uk.co.eduardo.aok.property.PropertyMap;

/**
 * Main entry point.
 *
 * @author Ed
 */
public class Viewer
{
   private static final Logger LOGGER;

   private static final int POST_SIZE = 256;

   static
   {
      Logger logger = null;
      try
      {
         final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         LogManager.getLogManager().readConfiguration( classLoader.getResourceAsStream( "logging.properties" ) ); //$NON-NLS-1$

         logger = Logger.getLogger( Viewer.class.getName() );
      }
      catch( final IOException exception )
      {
         // Ignore and use default logger
      }
      LOGGER = logger;
   }

   /**
    * Main entry point.
    *
    * @param args ignored.
    */
   public static void main( final String[] args )
   {
      start();
   }

   private static void start()
   {
      SwingUtilities.invokeLater( () -> initUI() );
   }

   private static JFrame createFrame()
   {
      final JFrame frame = new JFrame( "AOK Stats" );
      frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
      frame.pack();
      frame.setSize( 900, 600 );
      frame.setLocationRelativeTo( null );
      frame.setVisible( true );
      return frame;
   }

   private static void initUI()
   {
      setLaf();

      final JFrame frame = createFrame();
      final Map< IGUser, UserInfo > userMap = getUserMap();

      // The UI is in 3 sections top-to-bottom: user select, controls, content
      final JPanel userPanel = new JPanel( new GridLayout( 1, 2 ) );
      final JPanel controlsPanel = new JPanel( new FlowLayout( FlowLayout.LEADING ) );
      final JPanel contentPanel = new JPanel( new BorderLayout() );

      // We create a property map that will hold all the display parameters
      final PropertyMap displayParams = new PropertyMap();
      displayParams.addPropertyValue( DisplayProperties.SELECTED_USER, new EnumValue<>( IGUser.EDMOREGIS ) );
      displayParams.addPropertyValue( DisplayProperties.MEDIA_ORDER, new EnumValue<>( MediaOrder.CHRONOLOGICAL ) );
      displayParams.addPropertyValue( DisplayProperties.THUMBNAIL_SIZE, new IntegerValue( POST_SIZE ) );
      displayParams.addPropertyValue( DisplayProperties.LIST_CONTENT, new JComponentValue( contentPanel ) );

      // Set up the UI
      final JPanel outerPanel = new JPanel( new BorderLayout() );
      final JPanel topPanel = new JPanel( new BorderLayout() );

      topPanel.add( userPanel, BorderLayout.NORTH );
      topPanel.add( controlsPanel, BorderLayout.SOUTH );

      outerPanel.add( topPanel, BorderLayout.NORTH );
      outerPanel.add( contentPanel, BorderLayout.CENTER );
      outerPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

      // Create the user selection panel.
      final ButtonGroup group = new ButtonGroup();
      for( final Entry< IGUser, UserInfo > entry : userMap.entrySet() )
      {
         final JToggleButton userButton = createButton( entry.getKey(), entry.getValue(), displayParams );
         userButton.setSelected( displayParams.getPropertyValue( DisplayProperties.SELECTED_USER ) == entry.getKey() );
         group.add( userButton );
         userPanel.add( userButton );
      }

      // Create the control area
      final JComboBox< MediaOrder > order = new JComboBox<>( MediaOrder.values() );
      order.setSelectedItem( displayParams.getPropertyValue( DisplayProperties.MEDIA_ORDER ) );
      order.setUI( new ComboBoxUI() );

      final JSlider slider = new JSlider( SwingConstants.HORIZONTAL, 64, 512, POST_SIZE );

      controlsPanel.add( new JLabel( "Order by" ) );
      controlsPanel.add( order );
      controlsPanel.add( Box.createHorizontalStrut( 20 ) );
      controlsPanel.add( slider );

      // Set listeners
      order.addActionListener( a -> displayParams.setPropertyValue( DisplayProperties.MEDIA_ORDER,
                                                                    (MediaOrder) order.getSelectedItem() ) );

      slider.addChangeListener( new ChangeListener()
      {
         @Override
         public void stateChanged( final ChangeEvent e )
         {
            displayParams.setPropertyValue( DisplayProperties.THUMBNAIL_SIZE, slider.getValue() );
         }
      } );

      // If the selected user changes, then we have to rebuild the entire UI
      displayParams.addPropertyListener( DisplayProperties.SELECTED_USER, a -> rebuildUI( displayParams ) );
      displayParams.addPropertyListener( DisplayProperties.MEDIA_ORDER, a -> updateUI( displayParams ) );
      displayParams.addPropertyListener( DisplayProperties.THUMBNAIL_SIZE, a -> resizeUI( displayParams ) );

      // rebuild it for the first time.
      rebuildUI( displayParams );

      // frame.setContentPane( builder.getPanel() );
      frame.setContentPane( outerPanel );
   }

   private static void rebuildUI( final PropertyMap displayParams )
   {
      final JComponent contentPanel = displayParams.getPropertyValue( DisplayProperties.LIST_CONTENT );
      final IGUser user = displayParams.getPropertyValue( DisplayProperties.SELECTED_USER );

      LOGGER.log( Level.INFO, "Selected " + user.toString() ); //$NON-NLS-1$

      // Initially we remove all content and add a placeholder into the content panel
      contentPanel.removeAll();

      final JPanel placeholderPanel = new JPanel( new BorderLayout() );
      final Box box = Box.createHorizontalBox();
      box.add( Box.createHorizontalGlue() );
      box.add( new JLabel( "Loading..." ) );
      box.add( Box.createHorizontalGlue() );
      placeholderPanel.add( box );
      contentPanel.add( placeholderPanel );

      contentPanel.invalidate();
      contentPanel.revalidate();
      contentPanel.repaint();

      // Now we want to schedule the asynchronous loading of the results
      runAsynchronous( user, a -> MediaUtilities.getMediaFeed( a ), b -> updateUI( displayParams ) );
   }

   private static void updateUI( final PropertyMap displayParams )
   {
      final JComponent contentPanel = displayParams.getPropertyValue( DisplayProperties.LIST_CONTENT );
      final IGUser user = displayParams.getPropertyValue( DisplayProperties.SELECTED_USER );
      final Integer thumbnailSize = displayParams.getPropertyValue( DisplayProperties.THUMBNAIL_SIZE );
      final Instagram instagram = new InstaFactory( user ).create();
      final MediaFeed mediaFeed = MediaUtilities.getMediaFeed( instagram ); // this gets a cached value and does not need to fetch

      contentPanel.removeAll();

      final JPanel viewPane = new JPanel( new GalleryLayout( thumbnailSize ) );

      // We create a grid
      final MediaOrder mediaOrder = displayParams.getPropertyValue( DisplayProperties.MEDIA_ORDER );
      final List< MediaFeedData > listToSort = new ArrayList<>( mediaFeed.getData() );
      Collections.sort( listToSort, mediaOrder.getComparator() );
      for( final MediaFeedData media : listToSort )
      {
         viewPane.add( new GalleryButton( media ) );
      }
      final JScrollPane sp = new JScrollPane();
      sp.setBorder( null );
      sp.getVerticalScrollBar().setUnitIncrement( 5 );
      sp.setViewportView( viewPane );
      sp.getViewport().setViewSize( contentPanel.getSize() );
      contentPanel.add( sp );

      sp.addComponentListener( new ComponentAdapter()
      {
         @Override
         public void componentResized( final ComponentEvent e )
         {
            sp.getViewport().setViewSize( contentPanel.getSize() );
         }
      } );

      contentPanel.invalidate();
      contentPanel.revalidate();
      contentPanel.repaint();
   }

   private static void resizeUI( final PropertyMap displayParams )
   {
      final JComponent contentPanel = displayParams.getPropertyValue( DisplayProperties.LIST_CONTENT );
      final Integer thumbnailSize = displayParams.getPropertyValue( DisplayProperties.THUMBNAIL_SIZE );

      if( contentPanel.getComponentCount() > 0 )
      {
         final Component c = contentPanel.getComponent( 0 );
         if( c instanceof JScrollPane )
         {
            final JScrollPane sp = (JScrollPane) c;
            final JComponent viewPane = (JComponent) sp.getViewport().getView();
            viewPane.setLayout( new GalleryLayout( thumbnailSize ) );
            viewPane.invalidate();
            viewPane.revalidate();
            viewPane.repaint();
         }
      }
   }

   private static final Color SELECT_COLOR = new Color( 77, 182, 172 );

   @SuppressWarnings( "nls" )
   private static void setLaf()
   {
      try
      {
         UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

         UIManager.put( "List.selectionBackground", new ColorUIResource( SELECT_COLOR ) );
         UIManager.put( "ComboBox.selectionBackground", new ColorUIResource( SELECT_COLOR ) );

         UIManager.put( "ComboBox.buttonBackground", new ColorUIResource( SELECT_COLOR ) );
         UIManager.put( "ComboBox.buttonShadow", new ColorUIResource( SELECT_COLOR ) );
         UIManager.put( "ComboBox.buttonDarkShadow", new ColorUIResource( Color.BLACK ) );
         UIManager.put( "ComboBox.buttonHighlight", new ColorUIResource( SELECT_COLOR ) );
         UIManager.put( "ComboBox.border", new LineBorder( SELECT_COLOR, 3 ) );
      }
      catch( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception )
      {
         // If the System look and feel cannot be used, just go ahead and use the default look and feel.
      }
   }

   private static Map< IGUser, UserInfo > getUserMap()
   {
      final Map< IGUser, UserInfo > userMap = new TreeMap<>();
      for( final IGUser user : IGUser.values() )
      {
         try
         {
            final String accessToken = user.getAccessToken();
            final Instagram instagram = new Instagram( new Token( accessToken, AppConfig.CLIENT_SECRET ) );
            userMap.put( user, instagram.getUserInfo( user.getUserId() ) );
         }
         catch( final InstagramException exception )
         {
            LOGGER.log( Level.SEVERE, exception.getLocalizedMessage(), exception );
         }
      }
      return userMap;
   }

   private static < T > void runAsynchronous( final IGUser user,
                                              final Function< Instagram, T > instaFunc,
                                              final Consumer< T > consumer )
   {
      // We run the function on a new thread. Once that is done, we run the consumer on the Swing thread
      new Thread( () -> {
         final Instagram instagram = new InstaFactory( user ).create();
         final T result = instaFunc.apply( instagram );

         SwingUtilities.invokeLater( () -> consumer.accept( result ) );
      } ).start();
   }

   private static JToggleButton createButton( final IGUser user, final UserInfo info, final PropertyMap displayParams )
   {
      final Action action = new SelectUserAction( user, info, displayParams );
      final JToggleButton button = new JToggleButton( action );
      button.setUI( new UserButtonUI() );
      button.setBorderPainted( false );
      return button;
   }

   private static final class SelectUserAction extends AbstractAction
   {
      private final IGUser user;

      private final PropertyMap displayParams;

      private SelectUserAction( final IGUser user, final UserInfo info, final PropertyMap displayParams )
      {
         this.user = user;
         this.displayParams = displayParams;
         final String text = "<html><h3>#TAG#</h3>#NAME#</html>".replace( "#TAG#", info.getData().getUsername() ) //
                                                                .replace( "#NAME#", info.getData().getFullName() );
         final BufferedImage userImage = ImageUtilities.getImage( info.getData().getProfilePicture(), 128 );

         putValue( Action.NAME, text );
         putValue( Action.LARGE_ICON_KEY, new ImageIcon( userImage ) );
      }

      @Override
      public void actionPerformed( final ActionEvent e )
      {
         this.displayParams.setPropertyValue( DisplayProperties.SELECTED_USER, this.user );
      }
   }
}
