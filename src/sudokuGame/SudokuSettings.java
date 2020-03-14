package sudokuGame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 스도쿠 설정을 바꾸는 {@code JFrame}클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
public class SudokuSettings extends JFrame {
   public SudokuSettings() {
      super("스도쿠 설정");
      setSize(new Dimension(470, 630));
      
      JLabel timerLal = new JLabel(SudokuValue.Settings.getTimer());
      JLabel playTypeLal = new JLabel(SudokuValue.Settings.getPlayType());

      Container con = getContentPane();
      con.setLayout(new FlowLayout());
      con.add(timerLal);
      con.add(playTypeLal);
      
      setVisible(true);
      setResizable(false); //Frame 크기변경불가
   }
}
