package sudokuGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sudokuGame.SudokuValue.Settings.PlayType;

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
         if (SudokuValue.Settings.playType == PlayType.cellFirst)
            SudokuValue.IBackGround.alterationColor(x, y);
         else if (SudokuValue.Settings.playType == PlayType.digitFirst) {
            if (NumPadBtn.isSelectedNum()) {
               SudokuValue.nums[x][y].setText(NumPadBtn.selectedNum().getText());
               SudokuValue.IBackGround.alterationColor(x, y);
            }
         }
      } //end if (!isStart);
   }
}
