package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * 확장자(.ini)파일을 읽는 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
final public class INIFileReader extends ReaderLine {
   private Vector<String[][]> data = new Vector<String[][]>();
   /** 섹션의 이름이 저장되는 변수. */
   private String[] section;
   
   /**
    * 파일은 있는데 내용이 없는 경우 발생하는 예외처리 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   @SuppressWarnings("serial")
   public class FileEmptyException extends Exception {
      public FileEmptyException() {
         super("파일에 데이터가 없거나 잘못되어 있습니다.");
      }
   }
   
   /**
    * INIFileReader의 생성자.
    * 
    * @param data 확장자(.ini)파일의 읽을 수 있는 파일
    * @throws IOException 입출력 예외
    */
   public INIFileReader(FileReader data) throws IOException {
      String[][] fileData = fileLine(data);
      
      /* [이름 초기화] */
      section = new String[fileData.length];
      for (int i = 0; i < fileData.length; i++) {
         section[i] = fileData[i][0];
      }
      
      /* [내용 초기화] */
      for (int i = 0; i < section.length; i++) {
         this.data.add(StringLarge.arraySplit(fileData[i][1].split("\n"), "="));
      }
      
      /* [읽기 스트림 닫기] */
      data.close();
   }

   /**
    * 섹션의 이름을 리턴한다.
    * 
    * @return 섹션의 이름
    */
   public String[] getSection() {
      return section;
   }
   
   /**
    * 섹션의 번호를 입력받고, 그 섹션의 데이터를 리턴해 준다.
    * 
    * @param section 섹션의 번호(section index)
    * @return 섹션의 데이터
    * @throws FileEmptyException 파일 공백 예외
    */
   public String[][] getData(int section) throws FileEmptyException {
      try {
         return data.get(section);
      } catch (ArrayIndexOutOfBoundsException e) {
         throw new FileEmptyException();
      }
   }
   
   /**
    * [디버그용] section 출력
    */
   @Deprecated
   @SuppressWarnings("unused")
   private void printSction() {
      for (int i = 0; i < section.length; i++) {
         System.out.println(i+" = "+section[i]);
      }
   }
   
   /**
    * [디버그용] data 출력
    */
   @Deprecated
   @SuppressWarnings("unused")
   private void printData() {
      for (int i = 0; i < data.size(); i++) {
         System.out.println("[ "+i+" = "+section[i]+" ]");
         for (int j = 0; j < data.get(i).length; j++) {
            for (int h = 0; h < data.get(i)[j].length; h++) {
               System.out.println("["+j+", "+h+"] = "+data.get(i)[j][h]);
            }
         }
         System.out.println();
      }
   }
}
