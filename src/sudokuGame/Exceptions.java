package sudokuGame;

import javax.swing.JOptionPane;

import utils.*;
import utils.LogFileWriter.LogLevel;

/**
 * 예외처리(Exception)에 대한 클래스들
 * 
 * @see Exception
 */

/**
 * {@code Exception}에 text를 추가한 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
abstract class TextException extends Exception {
   private String text;
   public TextException(String message, String text) {
      super(message);
      this.text = text;
   }
   public String getText() {
      return text;
   }
}

/**
 * {@code TextException}에 상세메시지를 추가한 클래스.
 * <p>
 * 상세메세지는 메시지(getMessage())를 더하고,<br>
 * 틀린부분도 같이 적는 메시지이다.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
abstract class DetailException extends TextException {
   private String detailMessage;
   public DetailException(String message, String text) {
      super(message, text);
      detailMessage = message;
   }
   protected void setDetailMessage(String message) {
      detailMessage = getMessage().concat("\r\n"+StringLarge.space(5)+message);
   }
   public String getDetailMessage() {
      return detailMessage;
   }
}

/**
 * .ini파일의 섹션의 이름 부분이 잘못 되었을 때 발생하는 예외 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class SectionNameWrongException extends DetailException {
   SectionNameWrongException(String section) {
      super("파일을 읽는 도중에 섹션의 이름이 잘못되어 있습니다.", section);
      setDetailMessage("Wrong Part : "+getText()+" ����");
   }
}

/**
 * {@code Enum}의 원소가 없을때 발생하는 예외클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class EnumNotFoundException extends DetailException {
   EnumNotFoundException(String note, String section) {
      super("파일을 읽는 도중에 변수 값("+note+")이 잘못되어 있습니다.", section);
      setDetailMessage("Wrong Part : "+getText()+" 섹션");
   }
}

/**
 * 파일 읽기 도중에 발생하는 점수포맷 예외 클래스.
 * <p>
 * 점수의 포멧({@code String}[])의 게임 난이도, 날자, 점수 중<br>
 * 하나라도 잘못 되어 있는 경우 발생하는 예외처리.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class ScoreFormatException extends DetailException {
   ScoreFormatException(String note, String wrong) {
      super("파일를 읽는 도중에 "+note+"가 잘못되어 있습니다.", wrong);
      setDetailMessage("Wrong Part : "+getText());
   }
}

/**
 * {@code GameLevel}를 찾을 수 없는 예외클래스.
 * <p>
 * {@code SudokuValue.GameLevel}에 있는 열거형의 값이<br>
 * 없는 경우 발생하는 예외처리.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class GameLevelNotFoundException extends Exception {
   GameLevelNotFoundException(String note) {
      super("GameLevel."+note+"를 찾을 수 없습니다.");
   }
}

/**
 * 선택한 {@code nums}를 찾을 수 없는 예외클래스.
 * <p>
 * 게임을 시작할때 바로 NumberPad를 누를때 발생하는 예외처리.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class SelectNumsNotFoundException extends Exception {
   SelectNumsNotFoundException() {
      super("선택한 숫자를 찾을 수 없습니다.");
   }
}

/**
 * 스토쿠를 해결하지 못하는 예외클래스.
 * <p>
 * 스토쿠를 만드는 데 컴퓨터가 풀 수 없는 경우 발생하는 예외처리.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class SudokuNotSolveException extends RuntimeException {
   SudokuNotSolveException(int x, int y) {
      super("스도쿠를 만드는 부분("+x+", "+y+")에서 에러가 났습니다.");
      SudokuValue.log.write(LogLevel.FATAL, getClass().getSimpleName(), getMessage());
   }
}

/**
 * numberPad에 없는기능 실행하는 예외클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
class NumberPadNotFoundException extends RuntimeException {
   NumberPadNotFoundException(String text) {
      super("number pad에 없는 기능 입니다.");
      SudokuValue.log.write(LogLevel.FATAL, getClass().getSimpleName(), getMessage()+"\r\n"
            +StringLarge.space(5)+"Wrong Part : "+text);
      SudokuValue.timer.interrupt();
      SudokuAction.setStart(false);
      JOptionPane.showMessageDialog(null, getMessage(), "number pad 에러", JOptionPane.ERROR_MESSAGE);
   }
}