import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageFattore extends Observable implements Runnable
{
	File[] files;
	BufferedImage image;
	int fat;

	File outDir;

	public ImageFattore(File[] f, int q)
	{
		files = f;
		fat = q;
	}

	public void run() 
	{
		outDir = new File(files[0].getParentFile() + "" + File.separatorChar + "out" + File.separatorChar );
		outDir.mkdir();
		
		for ( int i = 0 ; i < files.length ; i++ )
		{
			try
			{		
				BufferedImage image = ImageIO.read( files[i] );
				BufferedImage resized = Manipola.convert(fat, image);
				
				ImageIO.write(resized, "jpg", new File(outDir.getPath() + "" + File.separatorChar + files[i].getName() ) );
			
				setChanged();
				notifyObservers();			
			
				Thread.sleep(100);
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null, "Errore di I/O ", "Errore!", JOptionPane.ERROR_MESSAGE );
			
				System.exit(1);				
			}
			catch(InterruptedException e)
			{
			}					
		}
		
		setChanged();
		notifyObservers("Fine");			
	}

}