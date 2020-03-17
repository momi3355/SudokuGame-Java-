package sudokuGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.lang.Thread.State;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import sudokuGame.SudokuFile.SudokuScore;
import utils.LogFileWriter;
import utils.StringLarge;
import utils.Timer;

/**
 * 스도쿠의 게임의 값들을 저장되는 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class SudokuValue {
   static final int MAX_NUM = 9;
   /** 게임 난이도에 대한 변수. */
   public static GameLevel level = GameLevel.normal;
   /** 파일 로그에 대한 변수. */
   static LogFileWriter log = new LogFileWriter(SudokuGame.class.getSimpleName());
   /**
    * 게임 난이도을 출력하는 라벨
    * <p>
    * class {@code ScoreLabel}을 통해서 초기화를 한다.
    * </p>
    * @see ScoreLabel
    */
   static ScoreLabel levelLabel = new ScoreLabel(level.getText(), 65, 35);
   /**
    * 타이머의 시간을 출력하는 라벨.
    * <p>
    * class {@code ScoreLabel}을 통해서 초기화를 한다.
    * </p>
    * @see ScoreLabel
    */
   static ScoreLabel timerLabel = new ScoreLabel("000", 55, 35);
   /**
    * 스도쿠의 시간을 측정할려고 있는 타이머.
    * <p>
    * {@code timerLabel}에 표기를 한다.
    * </p>
    * @see TimerLabel
    */
   static Timer timer = new Timer(timerLabel);
   static SudokuFile file = new SudokuFile();
   /**
    * 스도쿠의 실제로 들어가는 값이다.({@code JButon}으로 저장한다.)
    * 
    * @see JButton
    */
   static SudokuNum[][] nums = new SudokuNum[MAX_NUM][MAX_NUM];
   /**
    * 전부 두 가지이상의 경우의 수가 있으면, {@code nums}의 데이터를 저장하고 첫번째 수로 한다음 못풀면,<br>
    * {@code tempNums}를 마지막값(Last)을 불려와서 {@code nums}에 복제 한다음 두번째 수로 푸는 식으로 사용이 된다.
    * 
    * @see nums
    */
   private static Vector<Integer[][]> tempNums = new Vector<Integer[][]>();
   /**
    * 위 변수({@code tempNums})랑 같은 경우로, {@code tempNums}의 2가지 이상의 경우의 수의 계산한 수를 저장한다.<br>
    * 틀리면, {@code tempNums}를 nums로 대입시킬때, {@code chooceNum}에 없는 숫자로 선택하게된다.
    * 
    * @see tempNums
    */
   private static Vector<String> chooceNum = new Vector<String>();
   
   /**
    * 게임 난이도에 대한 열거형
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   public enum GameLevel {
      /** normal.showNum = 43; */
      normal(43),
      /** hard.showNum = 30; */
      hard(35);
      
      private int showNum;
      
      private GameLevel(int shownum) {
         this.showNum = shownum;
      }
      
      public int getShowNum() {
         return showNum;
      }
      
      public String getText() {
         return (showNum == normal.showNum) ? "보통" : "어려움";
      }
      
      /**
       * {@code GameLevel}의 전체요소의 갯수 리턴한다.
       * 
       * @return getSize() = 2;
       */
      public static int getSize() {
         return GameLevel.values().length;
      }
      
      /**
       * {@code GameLevel}의 index를 받아,<br>
       * 그 index의 {@code GameLevel}를 리턴한다.
       * 
       * @param index 인덱스
       * @return {@code GameLevel}의 index
       */
      public static GameLevel getIndex(int index) {
         return GameLevel.values()[index];
      }
   } // enum end GameLevel;
   
   /**
    * 컴포넌트의 Border의 기본값이 있는 인터페이스
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   interface IBorder {
      Border LINE = BorderFactory.createLineBorder(Color.GRAY);
      Border ACTIVE_3D = 
            BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.LIGHT_GRAY);
      Border DISABLE_3D = 
            BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.GRAY);
   } //end interface IBorder;
   
   /**
    * 스도쿠의 백그라운드(배경)을 설정하는 인터페이스
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   interface IBackGround {
      /** #A500FF */
      Color SELECT_COLOR = new Color(165, 0, 255);
      /** #00C878 */
      Color EQUALS_NUM = new Color(0, 200, 120);

      /**
       * 스도쿠 배경을 설정한다.
       * 
       * @param i
       * @param j
       */
      static void setNumBackground(int i, int j) {
         if ((!(i >= 3 && i <= 5) && !(j >= 3 && j <= 5)) ||
               ((i >= 3 && i <= 5) && (j >= 3 && j <= 5)))
            nums[i][j].setBackground(new Color(225, 225, 225));   //#E1E1E1
         else nums[i][j].setBackground(new Color(246, 246, 246)); //#F6F6F6
      }
      
      /**
       * 스도쿠의 룰을 찾아서 배경을 변경 한다.
       * 
       * @param x
       * @param y
       */
      static void selectRule(int x, int y) {
         /* [+선] */
         for (int i = 0; i < MAX_NUM; i++) {
            if (i != y) {
               if (nums[x][i].getFont().getStyle() == Font.BOLD)
                  nums[x][i].setBackground(new Color(140, 140, 0));   //#8C8C00
               else nums[x][i].setBackground(new Color(200, 200, 0)); //#C8C800
            }
            
            if (i != x) {
               if (nums[i][y].getFont().getStyle() == Font.BOLD)
                  nums[i][y].setBackground(new Color(140, 140, 0));   //#8C8C00
               else nums[i][y].setBackground(new Color(200, 200, 0)); //#C8C800
            }
         }
         
         /* [ㅁ공간] */
         int tempX = x/3*3; int tempY = y/3*3;
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
               if (tempX+i == x && tempY+j == y) continue;
                  
               if (nums[tempX+i][tempY+j].getFont().getStyle() == Font.BOLD)
                  nums[tempX+i][tempY+j].setBackground(new Color(140, 140, 0));   //#8C8C00
               else nums[tempX+i][tempY+j].setBackground(new Color(200, 200, 0)); //#C8C800
            }
         }
      }
      
      /**
       * 같은 숫자를 찾아서 배경을 변경 한다.
       * 
       * @param x
       * @param y
       */
      static void selectEqualsNums(int x, int y) {
         /* [같은 숫자] */
         if (nums[x][y].getText().equals("")) return;
         else if (nums[x][y].noteNums.size() > 0) return;
         
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
               if (x == i && y == j) continue;
               else if (nums[x][y].getText().equals(nums[i][j].getText()))
                  nums[i][j].setBackground(EQUALS_NUM);
            }
         }
      }
      
      /**
       * 선택한 숫자의 배경을 변경한다.
       * 
       * @param x
       * @param y
       */
      static void selectNum(int x, int y) {
         nums[x][y].setBackground(SELECT_COLOR);
      }

      /**
       * 스도쿠배경을 지운다.
       */
      static void clear() {
         for (int i = 0; i < MAX_NUM; i++) {
            for (int j = 0; j < MAX_NUM; j++) {
               setNumBackground(i, j);
            }
         }
      }
      
      /**
       * 다음 차레로 실행되는 함수이다.
       * <p>
       * 1. {@code Clear()}<br>
       * 2. {@code SelectRule(int, int)}<br>
       * 3. {@code SelectEqualsNums(int, int)}<br>
       * 4. {@code SelectNum(int, int)}<br>
       * </p>
       * @param x
       * @param y
       * 
       * @see Clear
       * @see SelectRule
       * @see SelectEqualsNums
       */
      static void alterationColor(int x, int y) {
         clear();
         selectRule(x, y);
         selectEqualsNums(x, y);
         selectNum(x, y);
      }
   } //end interface IBackGround;
   
   /**
    * 스도쿠 설정에 대한 클래스
    * <p>
    * [values]섹션에서 읽고, 쓰고<br>
    * 설정 바꾸는거는 {@code SudokuSettings}에서 한다.
    * </p>
    * @author 이창현(momi3355@hotmail.com)
    */
   public static class Settings {
      //TODO: 언어 변경 변수 추가 요함
      /** 타이머의 설정 */
      static Timer timer = Timer.second;
      /** 플래이 타입의 설정 */
      static PlayType playType = PlayType.cellFirst;
      /** 노트의 활성화 설정 */
      static boolean isNoteEnabled = true;
      /** 리셋 다이얼로그의 설정 */
      static boolean isResetDialogEnabled = true;
      
      /** 타이머 설정 열거형 */
      public enum Timer { second, minute;
         public static String[] valuestoString() {
            String[] toString = new String[values().length];
            for (int i = 0; i < toString.length; i++)
               toString[i] = StringLarge.capitalize(values()[i].toString());
            return toString;
         }
      } //end enum Timer;
      
      /** 플래이 타입 설정 열거형 */
      public enum PlayType { cellFirst, digitFirst;
         public static String[] valuestoString() {
            String[] toString = new String[values().length];
            for (int i = 0; i < toString.length; i++)
               toString[i] = StringLarge.capitalize(values()[i].toString());
            return toString;
         }
      } //end enum PlayType;

      /**
       * {@code isNoteEnabled}값을 리턴한다.
       * 
       * @return noteEnabled
       */
      public static boolean isNoteEnabled() {
         return isNoteEnabled;
      }
      
      /**
       * {@code isResetDialogEnabled}값을 리턴한다.
       * 
       * @return resetDialogEnabled
       */
      public static boolean isResetDialogEnabled() {
         return isResetDialogEnabled;
      }
      
      /**
       * {@code Timer} 열거형의 {@code toString()}을 리턴하는 함수.
       * 
       * @return 열거형의 .{@code toString()};
       */
      public static String getTimer() {
         return timer.toString();
      }
      
      /**
       * {@code PlayType} 열거형의 {@code toString()}을 리턴하는 함수.
       * 
       * @return 열거형의 .{@code toString()};
       */
      public static String getPlayType() {
         return playType.toString();
      }
   } //end class Settings;
   
   /**
    * 게임 난이도나, 타이머를 출력하는 라벨을 초기화 하는 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    * @see timerLabel
    */
   @SuppressWarnings("serial")
   static class ScoreLabel extends JLabel {
      ScoreLabel(String name, int width, int height) {
         this(name, new Dimension(width, height));
      }
      ScoreLabel(String name, Dimension size) {
         super(name, SwingConstants.CENTER);

         setSize(size);
         setBorder(IBorder.ACTIVE_3D);
      }
      
      /**
       * 프레임의 크기만큼 컴포넌트의 Point.x를 정렬 시킨다.
       * 
       * @param MAX_SIZE 프레임의 크기이다.
       * @param y {@code Point.y}
       * @param align {@code SwingConstants}의 값중 RIGHT, CENTER, LEFT이다.
       * @param space 빈칸의 공간이다.
       */
      void contentAlign(final Dimension MAX_SIZE, int y, int align, int space) {
         switch (align) {
         case SwingConstants.RIGHT:
            setLocation(new Point(MAX_SIZE.width - getSize().width - (space + 16), y));
            break;
         case SwingConstants.CENTER:
            setLocation(new Point(MAX_SIZE.width / 2 - getSize().width / 2 - 8, y));
            break;
         case SwingConstants.LEFT:
            setLocation(new Point(space, y));
            break;
         }
      }
   } //end class ScoreLabel;
   
   /**
    * 스도쿠해결의 알고리즘의 대한 클래스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   private static class SudokuSolve {
      /** 경우의 수를 넣을 수 없을때 리턴 되는 값. */
      private final static Point isImpossible = new Point(10, 10);
      
      /**
       * [디버그용]모든 nums를 출력하는 함수.
       */
      @Deprecated
      @SuppressWarnings("unused")
      private static void numsDisplay() {
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++)
               try {
               System.out.print((!nums[i][j].getName().equals("")) 
                     ? nums[i][j].getName() : " ");
               } catch (NullPointerException e) {
                  System.out.println("");
               }
            System.out.println();
         }
         System.out.println();
      }
      
      /**
       * [디버그용]모든 경우의 수를 출력하는 함수.
       */
      @Deprecated
      @SuppressWarnings("unused")
      private static void valueDisplay() {
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
               System.out.print("["+i+", "+j+"] ");
               
               Integer[] value = nums[i][j].getValue();
               for (int h = 0; h < nums[i][j].getValue().length; h++)
                  System.out.print(value[h]+" ");
               System.out.println();
            }
         }
      }
      
      /**
       * 모든 경우의 수를 제거한다.
       */
      private static void clearPossible() {
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
               nums[i][j].removeAllValue();
            }
         }
      }
      
      /**
       * 입력이 되었으면 [ㅁ칸], [+칸]의 경우의 수를 제거한다.
       * 
       * @param x
       * @param y
       * @param num
       */
      private static void clearPossible(int x, int y, int num) {
         int tempX = x/3*3; int tempY = y/3*3;
         nums[x][y].removeAllValue();
         // [ㅁ칸]
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
               if (tempX+i == x && tempY+j == y) continue;
               nums[tempX+i][tempY+j].removeValue(num);
            }
         }
         // [+칸]
         for (int i = 0; i < MAX_NUM; i++) {
            if (i != y) nums[x][i].removeValue(num);
            if (i != x) nums[i][y].removeValue(num);
         }
      }
      
      /**
       * [디버그용]경우의 수가 있는지 판별한다.
       * 
       * @param x
       * @param y
       * 
       * @return 경우의 수가 있으면 <code>true</code>, 없으면 <code>false</code>.
       */
      @Deprecated
      @SuppressWarnings("unused")
      private static boolean isPossible(int x, int y) {
         //빈 공간이고, 경우의 수가 없으면 에러.
         return (nums[x][y].getValue().length > 0) ? true : false;
      }
      
      /**
       * 경우의 수안에서 숫자가 있는지 판별한다.
       * 
       * @param x
       * @param y
       * @param num
       * 
       * @return 숫자 있으면 <code>true</code>, 없으면 <code>false</code>.
       */
      private static boolean isPossible(int x, int y, int num) {
         Integer[] value = nums[x][y].getValue();
         for (int i = 0; i < value.length; i++)
            if (value[i] == num) return true;
         return false; //경우의 수가 없으면 에러.
      }

      /**
       * 이 경로가 틀릴가능성이 있기때문에 저장을 한다. 
       * 
       * @return 경로가 2가지 이상인 {@code nums};
       * @see nums
       */
      private static Integer[][] numTempSave() {
         Integer[][] tempNums = new Integer[MAX_NUM][MAX_NUM];
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
               int num = nums[i][j].getName().equals("") 
                     ? 0 : Integer.parseInt(nums[i][j].getName());
               tempNums[i][j] = new Integer(num);
            }
         }
         return tempNums;
      }
      
      /**
       * 경우의 수를 검색후 클래스 {@code SudokuNum}의 {@code value}에 저장한다.
       * 
       * @param isFillNum <code>true</code>면 경우의 수를 입력이 되었고,<br>
       *  <code>false</code>면 경우의 수를 입력한다.
       * @return 경우의 수가 하나뿐인 경로<br>
       *  <code>null</code> : 빈공간<br>
       *  Point(10, 10) : 경우의 수가 2가지 이상.
       */
      private static Point findPossible(boolean isFillNum) {
         for (int x = 0; x < nums.length; x++) {
            for (int y = 0; y < nums[x].length; y++) {
               if (!nums[x][y].getName().equals("")) continue;
               
               for (int num = 1; num <= MAX_NUM; num++) {
                  int tempX = x/3*3; int tempY = y/3*3;
                  boolean isEquals = false;
                  // [ㅁ칸]
                  for (int i = 0; i < 3; i++) {
                     for (int j = 0; j < 3; j++) {
                        if (tempX+i == x && tempY+j == y) continue;
                        
                        if (isFillNum) {
                           if (nums[tempX+i][tempY+j].getName().equals("")) {
                              if (nums[tempX+i][tempY+j].isValue(num))
                                 isEquals = true;
                           } else if (nums[tempX+i][tempY+j].getName().equals(String.valueOf(num)))
                              isEquals = true;
                        } else if (nums[tempX+i][tempY+j].getName().equals(String.valueOf(num)))
                           isEquals = true;
                     }
                     if (isEquals) break;
                  }
                  // [+ 칸]
                  if (!isEquals) {
                     for (int i = 0; i < MAX_NUM; i++) {
                        if (isEquals) break;
                        else if (i != y) {
                           if (nums[x][i].getName().equals(String.valueOf(num)))
                              isEquals = true;
                        }

                        if (isEquals) break;
                        else if (i != x) {
                           if (nums[i][y].getName().equals(String.valueOf(num)))
                              isEquals = true;
                        }
                     }
                  }
                  //찾는 숫자가 없는 경우.
                  if (!isEquals) {
                     if (!isFillNum) {
                        nums[x][y].addValue(num);
                     } else {
                        clearPossible(x, y, num);
                        nums[x][y].addValue(num);
                        return new Point(x, y);
                     } //end if (isFileNum);
                  } //end if (!isEquals);
               } //end for (int num = 1; num <= MAX_NUM; num++);
            } 
         }
         return null;
      }
      
      /**
       * 가장적은 경우의 수의 경로를 찾는다.
       * 
       * @return 가장적은 경우의 수
       */
      private static Point findMinPossible() {
         int PossibleSize = MAX_NUM; //최대 경우의 수는 9다.
         Point MinPoint = null;
         
         for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
               if (!nums[i][j].getName().equals("")) continue;
               else if (PossibleSize > nums[i][j].getValue().length) {
                  PossibleSize = nums[i][j].getValue().length;
                  MinPoint = new Point(i, j);
               }
            }
         }
         if (MinPoint != null) {
            Integer[] tempValue = nums[MinPoint.x][MinPoint.y].getValue();
            int num = 0;
            
            if (PossibleSize > 1) {
               //System.out.println("< 최소경우의 수가 2다. >");
               //일딴 저장
               tempNums.add(numTempSave());
               
               if (chooceNum.size() < tempNums.size()) {
                  chooceNum.add(tempValue[0].toString());
                  num = tempValue[0];
               } else {
                  String tempChNum = chooceNum.get(chooceNum.size() - 1);
                  if (tempChNum.length() < tempValue.length) {
                     chooceNum.set(chooceNum.size() - 1,
                           tempChNum + tempValue[tempChNum.length()].toString());
                     num = tempValue[tempChNum.length()];
                  } else { //넣을 수 있는 경우의 수가 없으므로 전으로 이동한다.
                     tempNums.remove(tempNums.size() - 1);
                     chooceNum.remove(chooceNum.size() - 1);
                     return isImpossible;
                  }
               }
            } else {
               try {
                  num = tempValue[0];
               } catch (Exception e) { //넣을수 있는 경우의 수가 없다.
                  return isImpossible;
               }
            }
            
            clearPossible(MinPoint.x, MinPoint.y, num);
            nums[MinPoint.x][MinPoint.y].addValue(num);
         }
         return MinPoint;
      }
      
      /**
       * 스도쿠 해결 알고리즘이다.
       * 
       * @param isFillNum <code>true</code>면 경우의 수를 입력이 되었고,<br>
       *  <code>false</code>면 경우의 수를 입력한다.
       * @return 해결했으면 <code>true</code>, 오답이면 <code>false</code>.
       */
      static boolean solve(boolean isFillNum) {
         Point pt = findPossible(isFillNum); //유일한 경우의 수.
         
         if (pt == null) pt = findMinPossible(); //가장  경우의 수가 적은 좌표.
         //System.out.println("<tempNums.size() = "+tempNums.size()+">");
         if (pt == null) return true;
         else if (pt.equals(isImpossible)) { //경우의 수가 없음
            if (tempNums.size() > 0) { //저장한 것이 있으면
               //저장한 것을 불러오고
               for (int i = 0; i < nums.length; i++) {
                  for (int j = 0; j < nums[i].length; j++) {
                     String text = tempNums.lastElement()[i][j].equals(0) 
                           ? "" : tempNums.lastElement()[i][j].toString();
                     nums[i][j].setName(text);
                  }
               }
               tempNums.remove(tempNums.size() - 1); //삭제 한다.
               clearPossible(); //모든 경우의 수를 제거하고.
               if (solve(false)) { //다시푼다.
                  return true;
               }
            }
         } else {
            for (int n = 1; n <= MAX_NUM; n++) {
               if (!isPossible(pt.x, pt.y, n)) //불가능하면
                  continue;
               nums[pt.x][pt.y].setName(String.valueOf(n)); //숫자 입력
               nums[pt.x][pt.y].removeAllValue();
               if (solve(true)) { //채운걸 다시 품
                  return true;
               }
            }
            nums[pt.x][pt.y].setName(String.valueOf(0)); //풀 수 없는 구역.
         }
         throw new SudokuNotSolveException(pt.x, pt.y);
      }
   } //end class SudokuSolve;
   
   /**
    * 스도쿠의 표를 초기화를 하는 함수.
    * 
    * @param mainGame {@code JPanel}
    */
   static void initBoard(JPanel mainGame) {
      timerLabel.setFont(new Font("Default", Font.BOLD, 18));
      levelLabel.setFont(new Font("Default", Font.ITALIC, 15));
      for (int i = 0; i < nums.length; i++) {
         for (int j = 0; j < nums[i].length; j++) {
            nums[i][j] = new SudokuNum();
            nums[i][j].setBorder(IBorder.DISABLE_3D);
            nums[i][j].addActionListener(new SudokuAction(i, j));
            IBackGround.setNumBackground(i, j);
            mainGame.add(nums[i][j]);
         }
      }
   }
   
   /**
    * numPad를 초기화하는 함수.
    * 
    * @param numPad {@code JPanel}
    */
   static void initNumPad(JPanel numPad) {
      NumPadBtn[][] num = new NumPadBtn[2][6];
      String[] val = {
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "N", "E", "C"
      };
      int x = 0;
      for (int i = 0; i < num.length; i++) {
         for (int j = 0; j < num[i].length; j++) {
            num[i][j] = new NumPadBtn(val[x++]);
            
            numPad.add(num[i][j]);
         }
      }
   }
   
   /**
    * 스도쿠가 풀렸는지 확인 하는 함수.
    */
   public static boolean isSolve() {
      boolean isDifferent = false;
      for (int i = 0; i < nums.length; i++) {
         for (int j = 0; j < nums[i].length; j++) {
            //name과 text가 같으면, 정답이다.
            if (!nums[i][j].getText().equals(nums[i][j].getName()))
               isDifferent = true;
         }
      }
      return !isDifferent;
   }
   
   /**
    * {@code nums[][].getText()}에서<br>
    *  각각 x, y의 좌표의 num이 스토쿠 규칙에 맞는지 확인하는 함수.
    * 
    * @return x, y의 좌표에서 num에 스도쿠의 규칙에 맞으면(같은것이 없으면) <code>false</code>,<br>
    *  스도쿠의 규칙에 틀리면(같은것이 있으면) <code>true</code>.
    */
   public static boolean isEquals() {
      for (int x = 0; x < nums.length; x++) {
         for (int y = 0; y < nums[x].length; y++) {
            if (nums[x][y].getText().equals("")) return true;
            else if (nums[x][y].noteNums.size() > 0) return true;
            
            final String num = nums[x][y].getText();
            int tempX = x/3*3; int tempY = y/3*3;
            // [ㅁ칸]
            for (int i = 0; i < 3; i++) {
               for (int j = 0; j < 3; j++) {
                  if (tempX+i == x && tempY+j == y) continue;
                  
                  if (nums[tempX+i][tempY+j].getText().equals(num))
                     return true;
               }
            }
            // [+ 칸]
            for (int i = 0; i < MAX_NUM; i++) {
               if (i != y) {
                  if (nums[x][i].getText().equals(num))
                     return true;
               }

               if (i != x) {
                  if (nums[i][y].getText().equals(num))
                     return true;
               }
            } //end for (int i = 0; i < MAX_NUM; i++);
         }
      }
      return false;
   }
   
   /**
    * 스도쿠의 정답은 {@code getName()}에 저장 되어있기 때문에,
    * name을 불러오면 된다.
    */
   public static void answerText() {
      if (timer.getState().equals(State.TIMED_WAITING)) {
         int result = JOptionPane.showConfirmDialog(null, "기록에 저장되지 않습니다.\n     계속하시겠습니까?", 
               "경고", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

         if (result == JOptionPane.OK_OPTION) {
            clearBoard(true);
            for (int i = 0; i < nums.length; i++)
               for (int j = 0; j < nums[i].length; j++)
                  nums[i][j].setText(nums[i][j].getName());
         } //end if (result == JOptionPane.OK_OPTION);
      } //end if (timer.getState().equals(State.TIMED_WAITING));
   }
   
   /**
    * 스도쿠를 다 풀었을때 시작하기전으로 초기화하는 함수.
    * <p>
    * 1. 타이머 중단<br>
    * 2. 스코어 추가<br>
    * 3. 다 풀었으면, 자국을 지운다.<br>
    * 4. {@code SudokuAction.Start}를 <code>false</code>로 변경해준다.<br>
    * </p>
    * @param isAnswer 힌트나 정답을 보는 경우 = <code>true</code><br>
    *  직접 다 사람이 푼경우 = <code>false</code>
    * @return <code>true</code>이면 신기록이고,<br>
    * <code>false</code>이면 신기록이 아니다. 
    */
   public static boolean clearBoard(boolean isAnswer) {
      boolean isNewRecode = false;
      
      timer.timerStop(); //타이머 중단
      if (!isAnswer) //사람이 풀지 않은 경우는 점수기록을 하지 않는다.
         isNewRecode = SudokuScore.addScore(new Date(), timer.getTime()); //스코어 추가
      IBackGround.clear(); //다 풀었으면, 자국을 지운다.
      SudokuAction.setStart(false); //끝났므로, false로 변경해준다.
      return isNewRecode;
   }
   
   /**
    * 스도쿠의 표를 재설정를 하는 함수.
    */
   public static void setBoard() {
      byte[][] shape = new byte[MAX_NUM][MAX_NUM];
      //shape 초기화
      for (int i = 0; i < shape.length; i++)
         for (int j = 0; j < shape[i].length; j++)
            shape[i][j] = 0;
      
      if (!levelLabel.getText().equals(level.getText()))
         levelLabel.setText(level.getText());
      for (int n = 0; n < level.getShowNum(); n++) { //level.getShowNum()
         int tempX = 0; int tempY = 0;
         do {
            tempX = new Random().nextInt(MAX_NUM);
            tempY = new Random().nextInt(MAX_NUM);
         } while (shape[tempX][tempY] != 0);
         shape[tempX][tempY] = 1;
      }
      
      for (int i = 0; i < nums.length; i++) {
         for (int j = 0; j < nums[i].length; j++) {
            nums[i][j].setText(new String(""));
            nums[i][j].setFont((shape[i][j] == 1) 
                  ? new Font("Default", Font.BOLD, 12)
                  : new Font("Default", Font.PLAIN, 12));
            
            IBackGround.setNumBackground(i, j);
         }
      }
      
      tempNums.clear(); //전에 남아있는 값을 지운다.
      chooceNum.clear();
      setNums();
      startTimer();
   }
   
   /**
    * 각각 점수({@code TopScore}, {@code LatestScore})를 다이얼로그로 출력하는 함수.
    */
   public static void showScore() {
      String massage = "";
      /* [TopScore] */
      massage += showScore(SudokuFile.SudokuScore.getTopScore(),
      /* [Latest] */       SudokuFile.Section.Name.TopScore.toString());
      massage += showScore(SudokuFile.SudokuScore.getLatestScore(),
                           SudokuFile.Section.Name.Latest.toString());
      //기본글꼴을 저장한다.
      //가변 폭 때문에 OptionPane의 글꼴을 변경해준다.
      Font defaultFont = (Font)UIManager.get("OptionPane.messageFont");
      UIManager.put("OptionPane.messageFont", new Font("돋움체", Font.PLAIN, 14));
      
      JOptionPane.showMessageDialog(null, massage, "점수", JOptionPane.PLAIN_MESSAGE);
      UIManager.put("OptionPane.messageFont", defaultFont);
   }

   /**
    * 점수를 다이얼로그로 출력할수 있게 {@code String}으로 리턴하는 함수.
    * <p>
    * {@code String}에서 <code>sctionName</code>을 입력하고,
    *       <code>scoreInfo</code>를 <code>massage</code>에 저장하고 리턴하는 함수.
    * </p>
    * @param score 스도쿠의 점수
    * @param sectionName 점수가 저장되어있는 섹션이름
    * @return 점수 다이얼로그에 출력할 수 있는 {@code String}
    */
   private static String showScore(final String[][] scoreInfo, String sectionName) {
      String massage = "";
      massage = "["+sectionName+"]\n";
      massage += "_______________________________________\n";
      if (scoreInfo != null) {
         for (int i = 0; i < scoreInfo.length; i++) {
            massage += String.format("%-8s %s %6s\n",
                  scoreInfo[i][SudokuFile.IScoreInfo.LEVEL].toUpperCase(),
                  scoreInfo[i][SudokuFile.IScoreInfo.DAY],
                  scoreInfo[i][SudokuFile.IScoreInfo.SCORE]);
         }
      }
      massage += "\n";
      return massage;
   }
   
   /**
    * nums.name 초기화 하는 함수.
    */
   private static void setNums() {
      List<Integer> value = new Vector<Integer>(MAX_NUM); //리스트의 크기는 MAX_NUM
      for (int n = 1; n <= MAX_NUM; n++) value.add(n);
      
      for (int i = 0; i < nums.length; i++) {
         for (int j = 0; j < nums[i].length; j++) {
            if (i == 0) {
               int n = new Random().nextInt(value.size());
               nums[i][j].setName(Integer.toString(value.get(n)));
               value.remove(n);
            } else if (i == nums.length - 1) {
               if (value.isEmpty()) 
                  for (int n = 1; n <= MAX_NUM; n++) value.add(n);
               int n = 0;
               do {
                  n = new Random().nextInt(value.size());
                  if (value.size() == 1 && //경우의 수가 하나 밖에 없는데 위에 있는 경우
                        nums[0][j].getName().equals(value.get(n).toString())) {
                     setNums(); //다시 검색하고,
                     return;   //종료한다.
                  }
               } while (nums[0][j].getName().equals(value.get(n).toString()));
               nums[i][j].setName(Integer.toString(value.get(n)));
               value.remove(n);
            } else nums[i][j].setName(new String(""));
         }
      }
      SudokuSolve.solve(false);
      for (int i = 0; i < nums.length; i++) {
         for (int j = 0; j < nums[i].length; j++) {
            if (nums[i][j].getFont().getStyle() == Font.BOLD)
               nums[i][j].setText(nums[i][j].getName());
         }
      }     
   }
   
   /**
    * 타이머를 시작시키는 함수.
    */
   private static void startTimer() {
      switch (timer.getState()) {
      case TIMED_WAITING: //작동중일때
         timer.timerStop();
      case TERMINATED:   //끝날때
         timer = new Timer(timerLabel);
      case NEW:         //시작을 하지 않았을때
         timer.start();
         break;
      default:
         break;
      }
   }
}
