import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageRisoluzione extends Observable implements Runnable
{
	File[] files;
	BufferedImage image;
	int ris;

	File outDir;

	public ImageRisoluzione(File[] f, int q)
	{
		files = f;
		ris = q;
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
				BufferedImage resized = Manipola.convertAbsolute(ris, image);
				
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