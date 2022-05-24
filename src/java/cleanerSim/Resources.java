package cleanerSim;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class to reference and help use the resources in the game
 * It helps with the access of the language files and the images
 *
 */
public class Resources {

    /**
     * The singleton instance that can be accessed
     */
    public static Resources Instance;

    private BufferedImage image;

    /**
     * Constructor that assigns itself to the Instance static parameter, so it cna be access anywhere
     */
    public Resources() {
        Instance = this;

        try {
            image = ImageIO.read(Resources.Instance.getFileFromResources("images/icons.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns a resource file from the path given
     *
     * @param fileName The path to the file including it's name and extension
     * @return The file
     */
    public File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    /**
     * Get's a translated string
     *
     * @param key The string key
     * @return The string requested
     */
    public String getString(String key){
        return ResourceBundle.getBundle("lang/lang", Locale.getDefault()).getString(key);
    }

    /**
     * Returns a string from the languagef iles, formatted with the given arguments
     *
     * @param key The string key
     * @param args The format parameters
     * @return The formated string
     */
    public String getFormattedString(String key, Object... args){
        return String.format(getString(key), args);
    }

    /**
     * Returns a Icon image by it's id in the icon map
     *
     * @param id The ID
     * @return The image as a BufferedImage
     */
    public BufferedImage getIconByID(int id){

        BufferedImage off_Image =
                new BufferedImage(48, 48,
                        BufferedImage.TYPE_INT_ARGB);
        int row = 33;

        int pos_row = id / row;
        int pos_coll = id - (pos_row*row);

        int icon_h = 48;
        int icon_w = 48;



        off_Image.createGraphics().drawImage(image,0,0,48,48,pos_coll*icon_w,pos_row*icon_h,(pos_coll+1)*icon_w,(pos_row+1)*icon_h,null);

        return off_Image;
    }

}
