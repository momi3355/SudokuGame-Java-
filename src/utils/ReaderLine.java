package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class ReaderLine {
   static final int EOF = -1;
   
   public static String[][] fileLine(FileReader data) throws IOException {
      Vector<String[]> str = new Vector<String[]>();
      String[] info = null;
      int c = 0;
      boolean isTitle = false;

      info = new String[2];
      for (int i = 0; i < info.length; i++) 
         info[i] = "";
      
      while ((c = data.read()) != EOF) {
         if ((char)c == '[') {
            info = new String[2];
            for (int i = 0; i < info.length; i++) 
               info[i] = "";
            isTitle = true;
            continue;
         } else if ((char)c == ']') {
            if (!info[0].equals(""))
               str.add(info);
            isTitle = false;
            continue;
         }
         
         if (isTitle) {
            info[0] = info[0].concat(String.valueOf((char)c));
            continue;
         } else if ((char)c == '\r') continue;
         
         if (info[1].equals("") && (char)c == '\n') continue;
         info[1] = info[1].concat(String.valueOf((char)c));
      }
      
      return str.toArray(new String[str.size()][1]);
   }
}
