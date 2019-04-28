import java.io.*;

public class FiltroEstenzione implements FileFilter
 {
   String accettato;


   public FiltroEstenzione( String acc )
    {
      accettato = acc;
    }


   public boolean accept(File pathname) 
    {
      String str, suffisso;
      int i;

      str = pathname.getName(); 

      for ( i = 0 ; i < str.length() && str.charAt(i) != '.' ; i++ );

      suffisso = str.substring(i, str.length());

      return suffisso.equalsIgnoreCase( accettato );
    }

}