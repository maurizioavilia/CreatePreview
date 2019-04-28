import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;

public class RiduciImmagini extends JFrame implements Observer
{
	JProgressBar progressBar;

	JButton convertAbsolute;
	JButton convert;

	JRadioButton[] risoluzione;
	JRadioButton[] fattore;

	private static final int MAX_RIS = 4;
	private static final int MAX_FAT = 5;
		
	ButtonGroup gruppoRisoluzione;
	ButtonGroup gruppoFattore;
	
	JLabel progress;
	
	FiltroEstenzione filtro;
	File file;
	File[] files;
	
	double newVal;

	public RiduciImmagini ()
	{
		//scanFile();
		createObject();
		disposeObject();
		newVal = 0;
		
		setJMenuBar(createMenu());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle("Creazione preview foto");
		
		pack();
		setVisible(true);	
	}	

	private JMenuBar createMenu()
	{
		AscoltaFileMenu amFile = new AscoltaFileMenu(this);
		AscoltaHelpMenu amHelp = new AscoltaHelpMenu();
		
		JMenuBar menuBar = new JMenuBar();		
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add( creaVoceMenu("Apri", amFile) );
		fileMenu.addSeparator();
		fileMenu.add( creaVoceMenu("Exit", amFile) );
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add( creaVoceMenu("About", amHelp) );
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
	
		return menuBar;
		
	}

	private JMenuItem creaVoceMenu(String name, ActionListener al)
	{
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(al);
		
		return item;
	}

	private void scanFile()
	{
		file = new File (".");
		filtro = new FiltroEstenzione(".jpg");
		files = file.listFiles( filtro );
		
		if (files.length == 0)
		{
			JOptionPane.showMessageDialog(null, "Nella directory corrente non esistono file JPEG", 											"Errore!", JOptionPane.ERROR_MESSAGE );
			System.exit(1);
		}	
	}


	private void createObject()
	{
		progressBar = new JProgressBar(0, 100);
		
		risoluzione = new JRadioButton[4];
		fattore = new JRadioButton[5];
		
		risoluzione[0] = new JRadioButton("640x480");
		risoluzione[1] = new JRadioButton("800x600");
		risoluzione[2] = new JRadioButton("1024x768");
		risoluzione[3] = new JRadioButton("1280x1024");
		
		gruppoRisoluzione = new ButtonGroup();		
		
		for (int i = 0 ; i < MAX_RIS ; i++)
			gruppoRisoluzione.add(risoluzione[i]);
		
		fattore[0] = new JRadioButton("2");
		fattore[1] = new JRadioButton("3");
		fattore[2] = new JRadioButton("4");
		fattore[3] = new JRadioButton("5");
		fattore[4] = new JRadioButton("6");
		
		gruppoFattore = new ButtonGroup();
		
		for (int i = 0 ; i < MAX_FAT ; i++)
			gruppoFattore.add(fattore[i]);
		
		convertAbsolute = new JButton("Converti alla risoluzione");
		convertAbsolute.addActionListener( new AscoltaBottoni(this) );
		convertAbsolute.setEnabled(false);
		
		convert = new JButton("Converti di fattore");
		convert.addActionListener( new AscoltaBottoni(this) );
		convert.setEnabled(false);

		
		progress = new JLabel("0");		
	}

	private void disposeObject()
	{
		JPanel main = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		
		/* Pannello riduzione in base alla risoluzione */
		JPanel ris = new JPanel();
		ris.setLayout(new GridLayout(MAX_RIS+1, 1) );
		
		for (int i = 0 ; i < MAX_RIS ; i++)
			ris.add(risoluzione[i]);
			
		ris.add(convertAbsolute);
		
		ris.setBorder( new TitledBorder( new EtchedBorder(), "Risoluzione" ) );		
		
		/* Pannello riduzione in base al fattore di riduzione */		
		JPanel fat = new JPanel();		
		
		fat.setBorder( new TitledBorder( new EtchedBorder(), "Fattore di Riduzione" ) );		
		
		fat.setLayout(new GridLayout(MAX_FAT+1, 1) );
		
		for (int i = 0 ; i < MAX_FAT ; i++)
			fat.add(fattore[i]);
		
		fat.add(convert);
		
		
		/* Disposizione dei pannelli in quello principale*/ 
		main.setLayout( new BorderLayout() );	
		
		center.add(ris);
		center.add(fat);
		south.add( new JLabel("Progress Bar") );
		south.add(progressBar);
		south.add(progress);
		
		main.add(center, BorderLayout.CENTER);		
		main.add(south, BorderLayout.SOUTH);
		
		add(main);
	}

	public void update(Observable o, Object arg) 
	{
		if ( arg == null )
		{
			double step = 100.0 / ((double)files.length);
			newVal += step;
			
			progressBar.setValue( (int)newVal );
			progress.setText((int)newVal + " %");
		}
		else
		{
			progressBar.setValue( 100 );
			progress.setText(""+100);
			
			JOptionPane.showMessageDialog(this, "Conversione ultimata", "Finish!!! ;)", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void reset()
	{
		progressBar.setValue( 0 );
		progress.setText("0");
		newVal = 0;
	}

	private class AscoltaBottoni implements ActionListener
	{	
		JFrame parent;
		
		public AscoltaBottoni (JFrame p)
		{
			parent = p;
		}
		
		
		public void actionPerformed( ActionEvent e )
		{
			String cmd = e.getActionCommand();
			ButtonModel bm;
			String tmp;
			
			reset();
			
			if ( cmd.equals("Converti alla risoluzione") )
			{
				bm = gruppoRisoluzione.getSelection();
				risoluzione( bm.getActionCommand() );			
				
			}
			else
			{
				bm = gruppoFattore.getSelection();
				fattore( bm.getActionCommand() );
				
			}
		}
		
		
		private void risoluzione (String t)
		{	
			int quale = 0;
			
			for ( int i = 0 ; i < MAX_RIS ; i++ )
				if ( risoluzione[i].isSelected() )
					quale = i;		
			
			ImageRisoluzione convertitore = new ImageRisoluzione(files, quale);
			convertitore.addObserver((Observer)parent);
		
			new Thread( convertitore ).start();
		}
	
		
		private void fattore (String t)
		{
			int quale = 0;
			
			for ( int i = 0 ; i < MAX_FAT ; i++ )
				if ( fattore[i].isSelected() )
					quale = i;	

			ImageFattore convertitore = new ImageFattore(files, quale+2);
			convertitore.addObserver((Observer)parent);
		
			new Thread( convertitore ).start();
		}	
	}

	public class AscoltaFileMenu implements ActionListener
	{	
		JFrame parent;
		
		public AscoltaFileMenu(JFrame parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed( ActionEvent e )
		{
			String cmd = e.getActionCommand();
		
			if ( cmd.equals("Exit") )
			{
				System.exit(0);
			}
			else if (cmd.equals("Apri") )
			{
				JFileChooser fc = new JFileChooser();
				
				fc.setFileFilter( new FileFilterFileChooser("jpg", "file JPEG") );		
				int risultato = fc.showOpenDialog(parent);
				
				if (risultato == JFileChooser.APPROVE_OPTION)
				{					
					file = fc.getCurrentDirectory();
				
					filtro = new FiltroEstenzione(".jpg");
					files = file.listFiles( filtro );
					
					convertAbsolute.setEnabled(true);
					convert.setEnabled(true);
				}
			}
		}
	}

	public class AscoltaHelpMenu implements ActionListener
	{	
		public void actionPerformed( ActionEvent e )
		{
			String cmd = e.getActionCommand();		
		
			if ( cmd.equals("About") )
			{
				String about = "Programma per la creazione delle anteprime delle foto";
				String tmp;
				
				try
				{
					BufferedReader in = new BufferedReader( new FileReader("about.info") );
					
					
					while ( (tmp = in.readLine()) != null )
						about += tmp + "\n";
					
					
					JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE );
				}
				catch( IOException ex)
				{
					JOptionPane.showMessageDialog(null, "File di about.info corrotto o mancante", "Error!", JOptionPane.ERROR_MESSAGE );
				}
			}
		}
	}

	public static void main(String[] arg)
	{
		new RiduciImmagini();
	}
}