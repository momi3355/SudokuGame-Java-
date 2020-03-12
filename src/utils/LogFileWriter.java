package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * 로그파일을 쓰는 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class LogFileWriter {
   /** 프로그램 이름 */
   private final String PROGRAM_NAME;
   /** 로그파일의 파일이름 */
   private String FILE_NAME;
   
   /**
    * 로그레벨에 대한 열거형.
    * <p>
    * Trace : 상세한 디버그<br>
    * Debug : 일반 디버그<br>
    * Info : 중요 정보 로깅<br>
    * Warning : 경고 로그<br>
    * Error : 오류 로그<br>
    * Fatal : 치명적 오류 로그<br>
    * </p>
    * @author 이창현(momi3355@hotmail.com)
    */
   public enum LogLevel {
      /** 상세한 디버그 */
      TRACE,
      /** 일반 디버그 */
      DEBUG,
      /** 중요 정보 로깅 */
      INFO,
      /** 경고 로그 */
      WARNING,
      /** 오류 로그 */
      ERROR,
      /** 치명적 요류 로그 */
      FATAL,
   }
   
   /**
    * 데이터 포맷이 있는 인터페이스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   private interface IFormat {
      /** 날짜 포맷 */
      public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
      /** 시간 포맷 */
      public static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss.SSS");
   }
   
   /**
    * {@code LogFileWriter}의 생성자.
    * 
    * @param name 파일이름
    */
   public LogFileWriter(String name) {
      PROGRAM_NAME = name;
      setFileName();
   }
   
   /**
    * 로그의 내용을 기입하고 로그 파일(.log)을 만드는 함수.
    * <p>
    * [로그의 포맷]<br>
    * 년-월-일 시:분:초.ms [로그레벨] 클래스이름 로그내용.
    * </p>
    * @param level 로그레벨
    * @param name 함수나 클래스이름
    * @param message 로그내용
    * 
    * @see LogLevel 로그레벨
    */
   public void write(LogLevel level, String name, String message) {
      if (name == null) name = "";

      Date date = new Date(); //날짜
      String data = IFormat.DATE.format(date)+" "+IFormat.TIME.format(date)
            + " ["+level+"] "+name+": "+message+"\r\n";
      try {
         //로그 파일을 만들어 쓴다.
         setFileName(); //날짜가 바뀔 수도 있기 때문;
         FileWriter file = new FileWriter(getFileName(), true);
         file.write(data);
         file.close();
      } catch (IOException e) { //파일 입출력 에러
         JOptionPane.showMessageDialog(null, "로그 파일을 쓰는 도중에 에러가 났습니다.\n"
               + "(File IOException)", "파일 입출력 에러", JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }
   
   /**
    * 파일 이름을 리턴 하는 함수.
    * 
    * @return 파일 이름
    */
   public String getFileName() {
      return FILE_NAME;
   }
   
   /**
    * 파일 이름을 설정 하는 함수.
    * <p>
    * [파일 이름]<br>
    * 프로그램이름_날자.log
    * </p>
    */
   private void setFileName() {
      if (PROGRAM_NAME == null) { //프로그램 이름이 초기화되지 않았던 경우.
         System.err.println("파일 이름이나 프로그램 이름이 없습니다.");
         System.exit(0); //프로그램 종료.
      }
      FILE_NAME = PROGRAM_NAME.concat("_"+IFormat.DATE.format(new Date())+".log");
   }
}
