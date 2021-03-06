package sudokuGame;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import sudokuGame.SudokuValue.GameLevel;
import utils.*;
import utils.INIFileReader.FileEmptyException;
import utils.LogFileWriter.LogLevel;

/**
 * 스토쿠의 파일관리자 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
final class SudokuFile {
   /** 날자를 저장하는 글자 포멧. */
   public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd(E)HH:mm:ss");
   /**
    * 스도쿠의 설정파일 이름.
    * <p>
    * 게임과 같은 경로를 사용한다.
    * </p>
    */
   private static final String FILE_NAME = "SudokuSettingFile.ini";
   
   /**
    * <code>ScoreInfo</code>에 대한 인터페이스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   interface IScoreInfo {
      int LEVEL = 0;
      int DAY = 1;
      int SCORE = 2;
   } //end interface IScoreInfo;
   
   /**
    * 파일 섹션에 대한 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   static class Section {
      /**
       * 파일 섹션의 이름에 대한 열거형.
       * 
       * @author 이창현(momi3355@hotmail.com)
       */
      enum Name {
         /** Values.index = 0; */
         Values(0),
         /** TopScore.index = 1; */
         TopScore(1),
         /** Latest.index = 2; */
         Latest(2);
         
         private int index;
         
         private Name(int index) {
            this.index = index;
         }
         
         public int getIndex() {
            return index;
         }
         
         /**
          * {@code Name}의 index를 받아,<br>
          * 그 index의 {@code Name}의 toString()를 리턴한다.
          * 
          * @param index 인덱스
          * @return {@code Name}의 toString()
          */
         public static String getIndex(int index) {
            return Name.values()[index].toString();
         }
      } // enum end Name;
   
      /**
       * 파일 섹션별 값에 대한 클래스.
       * 
       * @author 이창현(momi3355@hotmail.com)
       */
      static class Key {
         enum Values { DefaultLevel, TimerType, PlayType, NoteEnabled, ResetDialogEnabled }
         enum TopScore { NormalScore, HardScore }
         enum Latest { LatestScore }
      } //end class Value;
   } //end class Section;
   
   /**
    * 스도쿠의 점수에 대한 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   static final class SudokuScore {
      private static final int MAX_SCORE = 5;
      
      private static Vector<String[]> topScore = new Vector<String[]>(GameLevel.getSize());
      private static Vector<String[]> latestScore = new Vector<String[]>(MAX_SCORE);
      
      /**
       * {@code TopScore}의 초기화 {@code String}[]
       * 
       * @param level {@code GameLevel}의 열거형
       * @return 초기화된 Score
       */
      private static String[] initScore(GameLevel level) {
         String[] initScore = {
               String.valueOf(level), "0000-00-00(  )00:00:00", "99:99"
         };
         return initScore;
      }

      /**
       * {@code topScore}를 초기화하는 함수.
       */
      private static void initTopScore() {
         topScore.add(initScore(GameLevel.normal));
         topScore.add(initScore(GameLevel.hard));
      }
      
      /**
       * [디버그용]{@code topScore}를 출력하는 함수.
       */
      @Deprecated
      @SuppressWarnings("unused")
      private static void topScoreDisplay() {
         for (int i = 0; i < topScore.size(); i++) {
            System.out.print(topScore.get(i)[IScoreInfo.LEVEL]+", ");
            System.out.print(topScore.get(i)[IScoreInfo.DAY]+", ");
            System.out.println(topScore.get(i)[IScoreInfo.SCORE]+";");
         }
      }
      
      /**
       * {@code topScore}를 리턴한다.
       * 
       * @return topScore를 String[][]변환.
       */
      static String[][] getTopScore() {
         String[][] score = new String[GameLevel.getSize()][];
         for (int i = 0; i < topScore.size(); i++) {
            score[i] = topScore.get(i);
         }
         return score;
      }
      /**
       * {@code topScore}의 게임난이도를 검색하여,<br>
       *  그 게임 난이도의 {@code topScore}를 리턴한다.
       *  
       * @param level {@code topScore}의 게임난이도.
       * @return {@code topScore}의 게임난이도의 <code>scoreInfo</code>
       * @throws GameLevelNotFoundException {@code GameLevel}를 찾을 수 없는 예외
       */
      static String[] getTopScore(GameLevel level) throws GameLevelNotFoundException {
         for (int i = 0; i < topScore.size(); i++) {
            if (topScore.get(i)[IScoreInfo.LEVEL].equals(level.toString()))
               return topScore.get(i);
         }
         throw new GameLevelNotFoundException(level.toString());
      }
      
      /**
       * {@code latestScore}를 읽기 전용으로 리턴한다.
       * 
       * @return latestScore를 {@code String}[][]변환.
       */
      static String[][] getLatestScore() {
         String[][] score = new String[latestScore.size()][];
         for (int i = 0; i < latestScore.size(); i++) {
            score[i] = latestScore.get(i);
         }
         return score;
      }
      /**
       * {@code LatestScore}의 인덱스를 받아서 그 인덱스의 값을 리턴한다.
       * 
       * @param index 인덱스
       * @return {@code LatestScore}의 인덱스의 {@code String}[]
       * @throws ArrayIndexOutOfBoundsException 배열 인덱스 초과 예외
       */
      static String[] getLatestScore(int index) throws ArrayIndexOutOfBoundsException {
         return latestScore.get(index);
      }
      
      /**
       * {@code latestScore}를 날자순서 대로 정렬하는 함수.
       * 
       * @param isAscend <code>true</code>이면 오름차순 이고,<br>
       * <code>false</code>이면 내림차순 이다.
       */
      static void SortLatestScore(boolean isAscend) {
         //type검사(Format)를 이미 진행을 한 상태이기 때문에 구지 검사를 하지 않겠다.
         Collections.sort(latestScore, new Comparator<String[]>() {
            @Override
            public int compare(String[] s1, String[] s2) {
               if (!isAscend) {
                  //스왑(Swap)하면 내림차순이 된다.
                  String[] temp = s1;
                  s1 = s2;
                  s2 = temp;
               }
               return s1[IScoreInfo.DAY].compareTo(s2[IScoreInfo.DAY]);
            }
         });
      }
      
      /**
       * {@code latest}에 입력(추가)하는 함수.
       * 
       * @param scoreInfo 점수의 정보
       */
      static void putlatest(String[] scoreInfo) {
         if (latestScore.size() >= MAX_SCORE)
            latestScore.remove(latestScore.size() - 1);
         latestScore.add(0, scoreInfo); //가장앞에 입력(추가).
      }
      
      /**
       * {@code topScore}에 입력(추가)하는 함수.
       * 
       * @param scoreInfo 점수의 정보
       * @return <code>true</code>이면 신기록이고,<br>
       * <code>false</code>이면 신기록이 아니다. 
       */
      static boolean newRecord(String[] scoreInfo) {
         boolean isNewRecord = false;
         for (int i = 0; i < topScore.size(); i++) {
            //레벨(게임 난이도)이 같을 때.
            if (topScore.get(i)[IScoreInfo.LEVEL].equals(scoreInfo[IScoreInfo.LEVEL])) {
               //신기록 일때.
               int scoreX = Timer.toSecond(scoreInfo[IScoreInfo.SCORE]);
               int scoreY = Timer.toSecond(topScore.get(i)[IScoreInfo.SCORE]);
               
               if (scoreX < scoreY) { //입력 안된 기록이 작을 때.
                  topScore.set(i, scoreInfo);
                  isNewRecord = true;
               }
            }
         } //end for (int i = 0; i < topScore.size(); i++);
         return isNewRecord;
      }
      
      /**
       * 스도쿠의 점수를 추가하는 함수.
       * 
       * @param level {@code GameLevel}
       * @param today 날자
       * @param score 기록(기간)
       * 
       * @return <code>true</code>이면 신기록이고,<br>
       * <code>false</code>이면 신기록이 아니다. 
       */
      static boolean addScore(Date today, int score) {
         String[] scoreInfo = new String[3];
         scoreInfo[IScoreInfo.LEVEL] = String.valueOf(SudokuValue.level);
         scoreInfo[IScoreInfo.DAY] = DATE.format(today);
         scoreInfo[IScoreInfo.SCORE] = Timer.toMinute(SudokuValue.timer.getTime());
         
         putlatest(scoreInfo);
         return newRecord(scoreInfo);
      }
   } //end class SudokuScore;
   
   /**
    * 파일관리자의 로드 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   private static class LoadFile {
      /** .ini File의 데이터. */
      private INIFileReader setting;
      
      /**
       * LoadFile의 생성자.
       * 
       * @param data 파일을 읽을 수 있는 파일.
       * @throws IOException 입출력 예외
       * @throws SectionNameWrongException 섹션이름 예외
       */
      LoadFile(FileReader data) throws IOException, SectionNameWrongException {
         this.setting = new INIFileReader(data);
         String[] sectionName = this.setting.getSection();
         for (int i = 0; i < sectionName.length; i++) {
            if (!sectionName[i].equals(Section.Name.getIndex(i))) //섹션의 이름이 잘못됨.
               throw new SectionNameWrongException(sectionName[i]);
         }
      }
      
      /**
       * {@code String}을 통해서 boolean을 리턴하는 함수.
       * 
       * @param name boolean.toString();
       * @return {@code String}을 boolean으로 변환후 리턴
       * @throws IllegalArgumentException 잘못된 인자 예외
       */
      private Boolean getBoolean(String name) throws IllegalArgumentException {
         if (!(name.equals("false") || name.equals("true")))
            throw new IllegalArgumentException();
         return Boolean.parseBoolean(name);
      }
      
      /**
       * values섹션이 맞게 <code>data</code>가 저장 되는지 확인하고, 틀리면 값(<code>value</code>)에<br>
       * {@code Enum} = 첫번째값;
       * {@code Boolean} = <code>true</code>;
       * 
       * @param <E> {@code Enum} or {@code Boolean}
       * @param value values섹션의 값의 변수.
       * @param data values섹션의 값.
       */
      @SuppressWarnings("unchecked")
      private <E> E valuesCheck(E value, String data) {
         try {
            if (value instanceof Enum) { //열거형 이면.
               try {
                  //GameLevel or TimerType or PlayType
                  value = (E)Enum.valueOf(((Enum<?>)value).getClass(), data);
                  //System.out.println(value+", "+((Enum<?>)value).getClass().getSimpleName());
               } catch (IllegalArgumentException e) { //값이 잘못 되어을 때.
                  value = (E)((Enum<?>)value).getClass().getEnumConstants()[0]; //가장 첫번째값.
                  throw new EnumNotFoundException(data, Section.Name.Values.toString());
               }
            } else if (value instanceof Boolean) { //bool 이면.
               try {
                  value = (E)getBoolean(data);
               } catch (IllegalArgumentException e) {
                  value = (E)Boolean.TRUE; //true
                  throw new EnumNotFoundException(data, Section.Name.Values.toString());
               }
            }
         } catch (EnumNotFoundException e) {
            SudokuValue.log.write(LogLevel.WARNING, getName(), e.getDetailMessage());
            System.err.println(e.getMessage());
         }
         return value;
      }
      
      /**
       * 섹션 Values의 읽는 함수.
       * 
       * @throws EnumNotFoundException Enum의 원소 예외
       * @throws FileEmptyException 파일 공백 예외
       */
      void values() throws EnumNotFoundException, FileEmptyException {
         final String[][] valuesData = setting.getData(Section.Name.Values.getIndex());
         
         for (int i = 0; i < valuesData.length; i++) {
            if (valuesData[i][0].isEmpty()) continue; //아무 것도 없으면, 다음.
            try {
               switch (Section.Key.Values.valueOf(valuesData[i][0])) {
               case DefaultLevel:
                  SudokuValue.level = valuesCheck(SudokuValue.level, valuesData[i][1]);
                  SudokuValue.levelLabel.setText(SudokuValue.level.getText());
                  break;
               case TimerType:
                  SudokuValue.Settings.timerType = 
                        valuesCheck(SudokuValue.Settings.timerType, valuesData[i][1]);
                  SudokuValue.timer.setType(SudokuValue.Settings.timerType);
                  break;
               case PlayType:
                  SudokuValue.Settings.playType = 
                        valuesCheck(SudokuValue.Settings.playType, valuesData[i][1]);
                  break;
               case NoteEnabled:
                  SudokuValue.Settings.isNoteEnabled = 
                        valuesCheck(SudokuValue.Settings.isNoteEnabled, valuesData[i][1]);
                  break;
               case ResetDialogEnabled:
                  SudokuValue.Settings.isResetDialogEnabled = 
                        valuesCheck(SudokuValue.Settings.isResetDialogEnabled, valuesData[i][1]);
                  break;
               }
            } catch (IllegalArgumentException e) {
               throw new EnumNotFoundException(valuesData[i][0], Section.Name.Values.toString());
            }
         }
      }
      
      /**
       * 점수가 대체로 입력되었는지, 확인하는 함수.
       * 
       * @param scoreInfo 점수에 대한 정보.
       * @return <code>true</code> = 잘못되지 않았다, <code>throw</code> {@code Exception} = 잘못되었다.
       * @throws ScoreFormatException 점수포맷 예외
       */
      private boolean isScoreFormat(String[] scoreInfo) throws ScoreFormatException {
         //scoreInfo[0] 게임 난이도.
         if (!scoreInfo[IScoreInfo.LEVEL].equals("")) //아무것도 입력되어 있지 않는 경우.
            if (!scoreInfo[IScoreInfo.LEVEL].equals(GameLevel.normal.toString()))
               if (!scoreInfo[IScoreInfo.LEVEL].equals(GameLevel.hard.toString()))
                  throw new ScoreFormatException("게임 난이도", scoreInfo[IScoreInfo.LEVEL]);
         
         //scoreInfo[1] 날자(시간).
         if (!scoreInfo[IScoreInfo.DAY].equals("")) { //아무것도 입력되어 있지 않는 경우.
            try {
               /*다시 String을 
               // Date로 변경해서. 에러검출 */
               DATE.parse(scoreInfo[IScoreInfo.DAY]);
            } catch (ParseException e) { //날자가 잘못된 경우.
               throw new ScoreFormatException("날자", scoreInfo[IScoreInfo.DAY]);
            }
         }
         
         //scoreInfo[2] 점수.
         try {
            int score = Timer.toSecond(scoreInfo[IScoreInfo.SCORE]);
            if (!(score >= 0 && score <= Timer.MAX_MINUTE)) { //0 ~ 6039가 아닌 경우
               throw new ScoreFormatException("점수", scoreInfo[IScoreInfo.SCORE]);
            } else {
               scoreInfo[IScoreInfo.SCORE] = Timer.toMinute(score); //재정렬
            }
         } catch (NumberFormatException e) { //아무것도 없거나, 숫자가 아닌 경우.
            throw new ScoreFormatException("점수의 숫자", scoreInfo[IScoreInfo.SCORE]);
         }
         
         return true;
      }
      
      /**
       * 섹션 TopScore의 읽는 함수.
       * 
       * @throws ScoreFormatException 점수포맷 예외
       * @throws EnumNotFoundException Enum의 원소 예외
       * @throws FileEmptyException 파일 공백 예외
       */
      void topScore() throws ScoreFormatException, EnumNotFoundException, FileEmptyException {
         final String[][] valuesData = setting.getData(Section.Name.TopScore.getIndex());

         for (int i = 0; i < valuesData.length; i++) {
            if (valuesData[i][0].isEmpty()) continue; //아무 것도 없으면, 다음.
            try {
               switch (Section.Key.TopScore.valueOf(valuesData[i][0])) {
               case NormalScore:
               case HardScore:
                  final String[] scoreInfo = valuesData[i][1].split(",");
                  if (isScoreFormat(scoreInfo)) { //포멧이 맞는 경우.
                     SudokuScore.newRecord(scoreInfo);
                  }
                  break;
               }
            } catch (IllegalArgumentException e) {
               throw new EnumNotFoundException(valuesData[i][0], Section.Name.TopScore.toString());
            }
         }
      }
      
      /**
       * 섹션 Latest의 읽는 함수.
       * 
       * @throws ScoreFormatException 점수포맷 예외
       * @throws EnumNotFoundException Enum의 원소 예외
       * @throws FileEmptyException 파일 공백 예외
       */
      void latest() throws ScoreFormatException, EnumNotFoundException, FileEmptyException {
         final String[][] valuesData = setting.getData(Section.Name.Latest.getIndex());
         
         for (int i = 0; i < valuesData.length; i++) {
            if (valuesData[i][0].isEmpty()) continue; //아무 것도 없으면, 다음.
            try {
               switch (Section.Key.Latest.valueOf(valuesData[i][0])) {
               case LatestScore:
                  final String[] scoreInfo = valuesData[i][1].split(",");
                  if (isScoreFormat(scoreInfo)) {
                     //현재 로컬시간의 2개월 전 계산.
                     Calendar cal = Calendar.getInstance();
                     cal.add(Calendar.MONTH, -2);
                     
                     Date ScoreDate = DATE.parse(scoreInfo[IScoreInfo.DAY]);
                     if (ScoreDate.after(cal.getTime())) { //2개월 이내.
                        SudokuScore.putlatest(scoreInfo);
                     }
                  }
                  break;
               }
            } catch (IllegalArgumentException e) {
               throw new EnumNotFoundException(valuesData[i][0], Section.Name.Latest.toString());
            } catch (ParseException e) {
               //이미 isScoreFormat()을 지났기 때문에 발생가능성은 없을 것이다.
               String[] scoreInfo = valuesData[i][1].split(",");
               SudokuValue.log.write(LogLevel.FATAL, getName(),
                     "파일 읽는 도중에 시간이 잘못 되어 있습니다.\r\n"
                     +StringLarge.space(5)+"Wrong Part : "+scoreInfo[IScoreInfo.DAY]);
               e.printStackTrace();
               return;
            }
         }
         SudokuScore.SortLatestScore(false);
      }
   } //end class LoadFile;
   
   /**
    * [디버그용] value섹션 값의 logging하는 함수.
    */
   @Deprecated
   @SuppressWarnings("unused")
   private static void valuesLogging() {
      SudokuValue.log.write(LogLevel.DEBUG, getName(), "timerType - "
            +SudokuValue.Settings.getTimerType());
      SudokuValue.log.write(LogLevel.DEBUG, getName(), "playType - "
            +SudokuValue.Settings.getPlayType());
      SudokuValue.log.write(LogLevel.DEBUG, getName(), "noteEnabled - "
            +SudokuValue.Settings.isNoteEnabled());
      SudokuValue.log.write(LogLevel.DEBUG, getName(), "ResetDialogEnabled - "
            +SudokuValue.Settings.isResetDialogEnabled());
   }
   
   /**
    * ..SudokuGame.ini에 대한 파일 읽기 함수.
    */
   static void loadGame() {
      LoadFile load = null;
      try {
         SudokuScore.initTopScore(); //topScore 초기화;
         load = new LoadFile(new FileReader(FILE_NAME));
         load.values();    //Values 섹션 읽기.
         load.topScore(); //TopScore 섹션 읽기.
         load.latest();  //Latest 섹션 읽기.
      } catch (FileNotFoundException e) { //파일이 없을 때.
         //null; (파일이 없기 때문에 아무것도 발생하지 않는다.)
      } catch (FileEmptyException e) { //파일은 있는데 내용이 없을 때.
         SudokuValue.log.write(LogLevel.WARNING, getName(), FILE_NAME.concat(" "+e.getMessage()));
         System.err.println(e.getMessage());
      } catch (DetailException e) { //각종 읽기 에러
         SudokuValue.log.write(LogLevel.WARNING, getName(), e.getDetailMessage());
         System.err.println(e.getMessage());
      } catch (IOException e) { //입출력 에러
         IOExLog(e); //IO 에러를 로그 입력
      }
   }
   
   /**
    * 파일관리자의 저장 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   private static class SaveFile {
      private static final String CRLF = "\r\n";
      /** 파일 쓰기 스트림 */
      private FileWriter data;
      
      /**
       * SaveFile의 생성자.
       * 
       * @param data 파일을 쓸수 있는 파일.
       */
      SaveFile(FileWriter data) {
         this.data = data;
      }
      
      /**
       * 쓰기 스트림 닫는 함수.
       * <p>
       * {@code data}를 사용했으니, 닫아야하기 때문.
       * </p>
       */
      void close() {
         try {
            data.close();
         } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "파일을 읽고쓰는 도중에 에러가 났습니다.\n"
                  + "(File IOException)", "파일 입출력 에러", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
         }
      }
      
      /**
       * 섹션 Value의 저장 하는 함수.
       * 
       * @throws IOException 입출력 예외
       */
      void values() throws IOException {
         data.write("[Values]"+CRLF); //섹션의 제목
         data.write(Section.Key.Values.DefaultLevel+" = "+SudokuValue.level+CRLF); //처음시작 난이도
         data.write(Section.Key.Values.TimerType+" = "+SudokuValue.Settings.getTimerType()+CRLF);
         data.write(Section.Key.Values.PlayType+" = "+SudokuValue.Settings.getPlayType()+CRLF);
         data.write(Section.Key.Values.NoteEnabled+" = "+SudokuValue.Settings.isNoteEnabled()+CRLF);
         data.write(Section.Key.Values.ResetDialogEnabled+" = "
                     +SudokuValue.Settings.isResetDialogEnabled()+CRLF);
      }
      
      /**
       * 섹션 TopScore의 저장 하는 함수.
       * 
       * @throws IOException 입출력 예외
       * @throws GameLevelNotFoundException 게임레벨 예외
       */
      void topScore() throws IOException, GameLevelNotFoundException {
         data.write("[TopScore]"+CRLF); //섹션의 제목
         for (int i = 0; i < SudokuScore.getTopScore().length; i++) {
            final String[] scoreInfo = SudokuScore.getTopScore(GameLevel.getIndex(i));
            if (scoreInfo[IScoreInfo.SCORE] != "99:99") { //최대값이 아니면,
               String valueName = StringLarge.capitalize(GameLevel.getIndex(i).toString())
                     + StringLarge.capitalize("score");
               //ex) NormalScore
               data.write(valueName+" = "); //난이도 이름
               data.write(scoreInfo[IScoreInfo.LEVEL]+", ");
               data.write(scoreInfo[IScoreInfo.DAY]+", ");
               data.write(scoreInfo[IScoreInfo.SCORE]+CRLF);
            }
         }
      }
      
      /**
       * 섹션 Latest의 저장하는 함수.
       * 
       * @throws IOException 입출력 예외
       */
      void latest() throws IOException {
         data.write("[Latest]"+CRLF); //섹션의 제목
         for (int i = 0; i < SudokuScore.getLatestScore().length; i++) {
            final String[] scoreInfo = SudokuScore.getLatestScore(i);
            //scoreInfo = SudokuScore.getLatestScore()[i];
            data.write(Section.Key.Latest.LatestScore+" = "); //값 이름
            data.write(scoreInfo[IScoreInfo.LEVEL]+", ");
            data.write(scoreInfo[IScoreInfo.DAY]+", ");
            data.write(scoreInfo[IScoreInfo.SCORE]+CRLF);
         }
      }
   } //end class SudokuSave;
   
   /**
    * ..SudokuGame.ini에 대한 파일 쓰는 함수.
    */
   static void saveGame() {
      SaveFile save = null;
      try {
         save = new SaveFile(new FileWriter(FILE_NAME));
         save.values();    //Values 섹션 저장.
         save.topScore(); //TopScore 섹션 저장.
         save.latest();  //Latest 섹션 저장.
      } catch (GameLevelNotFoundException e) { //게임레벨을 찾을 수 없을 때.
         SudokuValue.log.write(LogLevel.ERROR, getName(), e.getMessage());
         System.err.println(e.getMessage());
      } catch (IOException e) { //입출력 에러
         IOExLog(e); //IO에러를 로그 입력
      } finally {
         save.close(); //쓰기 스트림 닫기.
      }
   }
   
   /**
    * {@code IOException}에 대해서 로그(.log)에 입력하고 종료하는 함수.
    * 
    * @param IOException 입출력 예외
    */
   private static void IOExLog(IOException e) {
      SudokuValue.log.write(LogLevel.FATAL, getName(), FILE_NAME.concat(" 파일을 읽고 쓰는 도중에 에러가 났습니다."));
      e.printStackTrace();
   }
   
   /**
    * 클래스이름을 리턴하는 함수.
    * 
    * @return 클래스이름
    */
   public static String getName() {
      return SudokuFile.class.getSimpleName();
   }
}
