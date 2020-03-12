package sudokuGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 스도쿠의 마우스이벤트(MouseEvent)에 대한 클래스이다.
 * 
 * @author 이창현(momi3355@hotmail.com)
 * 
 * @see ActionListener
 */
public class SudokuAction implements ActionListener {
   private static boolean isStart = false;
   private int x;
   private int y;
   
   SudokuAction(int x, int y) {
      this.x = x;
      this.y = y;
   }
   
   public static boolean isStart() {
      return isStart;
   }
   
   public static void setStart(boolean bool) {
      isStart = bool;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      if (!isStart) { //시작하기 전
         SudokuValue.setBoard();
         isStart = true;
      } else if (SudokuValue.timer.isEnabled()) { //게임 중..
         SudokuValue.IBackGround.alterationColor(x, y);
      }
   }
}
