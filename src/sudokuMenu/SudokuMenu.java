package sudokuMenu;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
abstract class SudokuMenu extends JMenu implements ActionListener {
   /** 메뉴의 항목이 저장되어 있음. */
   protected Vector<JMenuItem> menuItem = new Vector<JMenuItem>();
   /** {@code menuItem}의 Item의 이름이 저장되어 있음. */
   protected final String[] GameItem;
   
   protected SudokuMenu(String name, String[] gameItem) {
      super(name);
      
      GameItem = gameItem;
      for (int i = 0; i < GameItem.length; i++) {
         menuItem.add(new JMenuItem(GameItem[i]));
         menuItem.get(i).addActionListener(this);
      }
   }
   
   /**
    * {@code GameItem}의 인덱스값을 찾는 함수.
    * 
    * @param item {@code GameItem}의 값이다.
    * @return {@code GameItem}의 인덱스값을 리턴한다.<br>
    *         -1를 리턴하면 인덱스값이 없는 것이다.
    */
   protected int fundIndex(String item) {
      for (int i = 0; i < GameItem.length; i++)
         if (GameItem[i].equals(item))
            return i;
      return -1;
   }
}
