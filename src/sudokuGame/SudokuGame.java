package sudokuGame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import sudokuMenu.*;

/**
 * 수학게임의 수도쿠를 자바로 구현한 것이다.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class SudokuGame extends JFrame {
   private static final long serialVersionUID = -5149398519613944477L;
   
   private WindowsSetting window = new WindowsSetting();
   private JPanel mainGame = new JPanel();
   private JPanel numPad = new JPanel();
   private Point location = new Point();
   
   public SudokuGame() {
      super("스도쿠");
      //창닫기(X)를 눌렀을때 꺼지는 것을 방지.
      //꺼지는 함수는 WindowsSetting.windowExit()에 있다.
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
      setSize(new Dimension(470, 630));
      
      /* [LoadGame] */
      SudokuFile.loadGame();
      
      /* [setLocation] */
      setMainGameLocation();
      
      /* [init_mainGame] */
      mainGame.setLayout(new GridLayout(9, 9));
      mainGame.setSize(new Dimension(420, 420));
      SudokuValue.initBoard(mainGame);
      
      /* [init_numPad] */
      numPad.setLayout(new GridLayout(2, 6, 5, 5));
      numPad.setSize(new Dimension(420, 60));
      SudokuValue.initNumPad(numPad);
      
      /* [add_Component] */
      Container con = getContentPane();
      con.add(mainGame);
      con.add(numPad);
      con.add(SudokuValue.levelLabel);
      con.add(SudokuValue.timerLabel);
      con.setLayout(new BorderLayout());
      
      /* [add_JMenuBer] */
      JMenuBar sudokuMenu = new JMenuBar();
      sudokuMenu.add(new GameMenuBar());
      sudokuMenu.add(new HelpMenuBar());
      setJMenuBar(sudokuMenu);
      
      /* [init_WindowsSetting] */
      window.windowsResize(this);
      addWindowListener(window);
      
      setVisible(true);
      setResizable(false); //Frame 크기변경불가
   }

   public static void main(String[] args) {
      new SudokuGame();
   }
   
   /**
    * 스도쿠의 표 위치를 변경 시킨는 함수.
    */
   void setMainGameLocation() {
      location.setLocation(getSize().width/2 - mainGame.getSize().width/2 - 8, //Point.x
                           getSize().height/2 - mainGame.getSize().height/2 - 50); //Point.y
       
      mainGame.setLocation(location.getLocation());
      numPad.setLocation(location.getLocation().x, location.getLocation().y+430);

      SudokuValue.levelLabel.contentAlign(this.getSize(), 12, SwingConstants.CENTER, 20);
      SudokuValue.timerLabel.contentAlign(this.getSize(), 12, SwingConstants.RIGHT, 20);
   }
}


