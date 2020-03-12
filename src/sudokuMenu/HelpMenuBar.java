package sudokuMenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import sudokuGame.*;

/**
 * 도움메뉴의 클래스이다.
 * <p>
 * {@code HelpMenuBar}(도움말(H))<br>
 * ┣━{@code confirmMenuTiem}(확인(C))<br>
 * ┗━{@code aboutMenuTiem}(스도쿠 정보(A)          F1)<br>
 * </p>
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
public class HelpMenuBar extends SudokuMenu {
   private final static String[] MENU_ITEM = {
         "확인(C)",
         "스도쿠 정보(A)          F1"
   };
   
   public HelpMenuBar() {
      this("도움말(H)");
   }
   public HelpMenuBar(String name) {
      super(name, MENU_ITEM);
      
      for (int i = 0; i < GameItem.length; i++) {
         add(menuItem.get(i));
         if (i == 0) addSeparator();
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      int index = fundIndex(e.getActionCommand());
      
      switch (index) {
      case 0: //확인(C)
         SudokuValue.answerText();
         break;
      case 1: //스도쿠 정보(A)
         JOptionPane.showMessageDialog(null, 
               "Number Pad Info\n"
               + "N : Note | E : Erasure | C : Clear"
               , "도움말", JOptionPane.INFORMATION_MESSAGE);
         break;
      default:
         throw new IndexOutOfBoundsException("GameItem." +
               e.getActionCommand() + " Not found");
      }
   }
}
