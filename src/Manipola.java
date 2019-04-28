import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

/** Classe che consente la possibilita' di ridimensionare le immagini in input
*/

public class Manipola
{
	public static final int RES_640x480 = 0;
	public static final int RES_800x600 = 1;
	public static final int RES_1024x768 = 2;
	public static final int RES_1280x1024 = 3;

	public static final int RESIZE_2 = 2;
	public static final int RESIZE_3 = 3;
	public static final int RESIZE_4 = 4;
	public static final int RESIZE_5 = 5;
	public static final int RESIZE_6 = 6;		

	/** Riduce l'immagine ad un valore assoluto di risoluzione. Potrebbe non mantenere le proporzioni dell'immagine
		
		@param resolution La risoluzione desiderata. Usare una delle costanti RES_x_y
		@param image L'immagine da ridurre
		
		@return L'immagine ridimensionata		
	*/
	
	public static BufferedImage convertAbsolute ( int resolution, BufferedImage image )
	{
		int x, y;
		
		if ( image.getHeight() < image.getWidth() )
		{
			x = traslateX(resolution);
			y = traslateY(resolution);
		}
		else
		{
			x = traslateY(resolution);
			y = traslateX(resolution);
		}
		
		Image resized = image.getScaledInstance( /*traslateX(resolution)*/ x, /*traslateY(resolution)*/ y, 
													BufferedImage.SCALE_SMOOTH);
		
		return toBufferedImage(resized);
	}

	/** Riduce l'immagine di fattore di riduzione . Mantiene le proporzioni dell'immagine
		
		@param factor Il fattore di riduzione. Usare una delle costanti RESIZE_n
		@param image L'immagine da ridurre
		
		@return L'immagine ridimensionata		
	*/

	public static BufferedImage convert ( int factor, BufferedImage image )
	{
		Image resized = image.getScaledInstance( image.getWidth()  / factor, image.getHeight() / factor, 
													BufferedImage.SCALE_SMOOTH);
		
		return toBufferedImage(resized);
	}	

/* Gentilmente "offeto" da http://www.exampledepot.com/  */

	private static BufferedImage toBufferedImage(Image image) 
	{
        if (image instanceof BufferedImage) 
		{
            return (BufferedImage)image;
        }
		
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
		
		
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try 
		{
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
		
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
			image.getWidth(null), image.getHeight(null), transparency);
		
        } 
		catch (HeadlessException e) 
		{
            // The system does not have a screen
        }
		
        if (bimage == null) 
		{
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
		
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
		
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
		
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }

	private static int traslateX(int r)
	{
		if (r == RES_640x480)
			return 640;
		else if ( r == RES_800x600 )
			return 800;
		else if ( r == RES_1024x768 )
			return 1024;
		else if ( r == RES_1280x1024 )
			return 1280;
		else 
			throw new IllegalArgumentException();
	}

	private static int traslateY(int r)
	{
		if (r == RES_640x480)
			return 480;
		else if ( r == RES_800x600 )
			return 600;
		else if ( r == RES_1024x768 )
			return 768;
		else if ( r == RES_1280x1024 )
			return 1024;
		else 
			throw new IllegalArgumentException();
	}
}	