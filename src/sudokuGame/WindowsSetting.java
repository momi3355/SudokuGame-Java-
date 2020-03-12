package sudokuGame;

import java.awt.event.*;

import javax.swing.JOptionPane;

/**
 * 창(windows)의 크기변경, 최소화, 최대화등 창에 대한 함수가 저장되어 있는 클래스이다.
 * <p>
 * {@code ComponentListener}에 추가를 한다.
 * </p>
 * @author 이창현(momi3355@hotmail.com)
 * @see ComponentListener
 */
public class WindowsSetting extends WindowAdapter {
   /**
    * 창 크기 변경 함수이다.
    */
   void windowsResize(SudokuGame frame) {
      frame.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            frame.setMainGameLocation();
         }
      });
   }
   
   /**
    * 끝내기(X)누르면 실행해야하는 함수.
    */
   public static void windowExit() {
      if (SudokuAction.isStart()) {
         int result = JOptionPane.showConfirmDialog(null, 
               "종료하시겠습니까 ?", "게임종료",
               JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (result != JOptionPane.YES_OPTION) return;
      }
      
      SudokuFile.saveGame();
      System.exit(0); //정상종료
   }
   
   /**
    * 창 닫기하면 실행하는 함수.
    */
   @Override
   public void windowClosing(WindowEvent e) {
      windowExit();
   }
}
