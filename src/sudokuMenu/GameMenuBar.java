package sudokuMenu;

import java.awt.event.ActionEvent;

import sudokuGame.*;
import sudokuGame.SudokuValue.GameLevel;

/**
 * 게임메뉴의 클래스이다.
 * <p>
 * {@code GameManuBar}(게임(G))<br>
 * ┣━{@code newGameMenuItem}(새 게임(F2))<br>
 * ┣━{@code separator()} //분리선<br>
 * ┣━{@code nomalGameMenuItem}(보통(N))<br>
 * ┣━{@code hardGameMenuItem}(어려움(H))<br>
 * ┣━{@code separator()} //분리선<br>
 * ┣━{@code scoresMenuItem}(기록...)<br>
 * ┣━{@code separator()} //분리선<br>
 * ┣━{@code settingsMenuItem}(설정(S))<br>
 * ┣━{@code separator()} //분리선<br>
 * ┗━{@code exitGame}(끝내기(X))<br>
 * </p>
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
public class GameMenuBar extends SudokuMenu {
   private final static String[] MENU_ITEM = {
         "새 게임(F2)",
         "보통(N)",
         "어려움(H)",
         "기록..",
         "설정(S)",
         "끝내기(X)"
   };
   
   public GameMenuBar() {
      this("게임(G)");
   }
   public GameMenuBar(String name) {
      super(name, MENU_ITEM);
      
      for (int i = 0; i < GameItem.length; i++) {
         add(menuItem.get(i));
         if (i == 0 || i == 2 || i == 3 || i == 4)
            addSeparator();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      int index = fundIndex(e.getActionCommand());
      
      switch (index) {
      case 0: //새 게임(F2)
         //바로 난이도 변경하고, 새 게임을 누를경우 다시시작이 2번 발생을 
         // SudokuAction.Start = ture로 변경함으로써 2번을 1번으로 된다.
         SudokuAction.setStart(true);
         SudokuValue.setBoard();
         break;
      case 1: //보통(N)
         if (!SudokuValue.level.equals(GameLevel.normal))
            SudokuValue.level = GameLevel.normal;
         break;
      case 2: //여러움(H)
         if (!SudokuValue.level.equals(GameLevel.hard))
            SudokuValue.level = GameLevel.hard;
         break;
      case 3: //기록
         SudokuValue.showScore();
         break;
      case 4: //설정(S)
         new SudokuSettings();
         break;
      case 5: //끝내기(X)
         WindowsSetting.windowExit();
         break;
      default:
         throw new IndexOutOfBoundsException("GameItem." +
               e.getActionCommand() + " Not found");
      }
   }
}
