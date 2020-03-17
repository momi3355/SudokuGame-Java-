package sudokuGame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import sudokuGame.SudokuValue.Settings.PlayType;
import utils.LogFileWriter.LogLevel;
import utils.StringLarge;
import utils.Timer.TimerType;

/**
 * 스도쿠 설정을 바꾸는 {@code JFrame}클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
@SuppressWarnings("serial")
public class SudokuSettings extends JFrame implements ActionListener {
   /** 스토쿠의 셋팅할때 필요한 컴퍼넌트들 */
   Component[][] components = new Component[4][2];
   
   /**
    * 컴퍼넌트의 타입에 대한 인터페이스.
    * 
    * @author 이창현(momi3355@hotmail.com)
    */
   private interface ICompType {
      /** 타이머 */
      int TIME = 0;
      /** 플래이 타입 */
      int PLAY_TYPE= 1;
      /** 노트의 활성화 */
      int NOTE_ENABLED = 2;
      /** 리셋 다이얼로그 */
      int RESET_DIALOG = 3;
   } //end interface ICompType;
   
   /**
    * {@code SudokuSettings}의 생성자.
    */
   public SudokuSettings() {
      super("스도쿠 설정");
      setSize(new Dimension(250, 245));
      initComponents(components); //components 초기화
      
      Container c = getContentPane();
      c.setLayout(new BorderLayout());
      c.add(getMainPanel(), BorderLayout.CENTER);
      c.add(getCommitBtn(), BorderLayout.SOUTH);
      
      setVisible(true);
      setResizable(false); //Frame 크기변경불가
   }
   
   /**
    * Component들을 초기화하는 함수.
    * 
    * @param com component[][]
    */
   private void initComponents(Component[][] com) {
      JComboBox<String> comBox = new JComboBox<String>(TimerType.valuestoString());
      comBox.setSelectedItem(StringLarge.capitalize(SudokuValue.Settings.getTimerType()));
      com[0][0] = new JLabel("시간", SwingConstants.RIGHT);
      com[0][1] = comBox;
                         
      comBox = new JComboBox<String>(PlayType.valuestoString());
      comBox.setSelectedItem(StringLarge.capitalize(SudokuValue.Settings.getPlayType()));
      com[1][0] = new JLabel("플래이 타입", SwingConstants.RIGHT);
      com[1][1] = comBox;
      
      com[2][0] = new JLabel("노트 활성화", SwingConstants.RIGHT);
      com[2][1] = new JCheckBox("", SudokuValue.Settings.isNoteEnabled());
      
      com[3][0] = new JLabel("리셋 다이얼로그", SwingConstants.RIGHT);
      com[3][1] = new JCheckBox("", SudokuValue.Settings.isResetDialogEnabled());
   }
   
   /**
    * Setting의 메인 페널을 셋팅하고, 리턴하는 함수.
    * 
    * @return Setting의 메인 페널
    */
   private JPanel getMainPanel() {
      JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
      panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); //빈 테두리;
      for (int i = 0; i < components.length; i++)
         for (int j = 0; j < components[i].length; j++)
            panel.add(components[i][j]);
      return panel;
   }
   
   /**
    * commit버튼과, cancel버튼을 넣은 Panel을 리턴 하는 함수.
    * 
    * @return commit버튼과, cancel버튼을 넣은 Panel
    */
   private JPanel getCommitBtn() {
      JPanel panel = new JPanel();
      JButton commitBtn = new JButton("적용");
      JButton cancelBtn = new JButton("취소");
      commitBtn.addActionListener(this);
      cancelBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            setVisible(false); //settting의 Frame 숨기기.
         }
      });
      panel.add(commitBtn);
      panel.add(cancelBtn);
      
      return panel;
   }
   
   /**
    * {@code Setting}화면의 commit버튼의 이벤트 함수.
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < components.length; i++) {
         if (components[i][1] instanceof JComboBox) {
            //JComboBox<String>;
            JComboBox<?> source = (JComboBox<?>)components[i][1];
            //capitalize한걸 다시 푼다.
            String selectItem = ((String)source.getSelectedItem()).substring(0, 1).toLowerCase()
                              + ((String)source.getSelectedItem()).substring(1);
            if (i == ICompType.TIME) {
               SudokuValue.Settings.timerType = TimerType.valueOf(selectItem);
               SudokuValue.timer.setType(SudokuValue.Settings.timerType);
            } else if (i == ICompType.PLAY_TYPE) {
               SudokuValue.Settings.playType = PlayType.valueOf(selectItem);
            }
         } else if (components[i][1] instanceof JCheckBox) {
            boolean selected = ((JCheckBox)components[i][1]).isSelected();
            if (i == ICompType.NOTE_ENABLED) {
               SudokuValue.Settings.isNoteEnabled = selected;
            } else if (i == ICompType.RESET_DIALOG) {
               SudokuValue.Settings.isResetDialogEnabled = selected;
            }
         } //end if (components[i][1] instanceof JComboBox);
      } //end for (int i = 0; i < components.length; i++);
      
      //Logging.
      SudokuValue.log.write(LogLevel.DEBUG, getClass().getSimpleName(), "timer = "
            +SudokuValue.Settings.getTimerType());
      SudokuValue.log.write(LogLevel.DEBUG, getClass().getSimpleName(), "playType = "
            +SudokuValue.Settings.getPlayType());
      SudokuValue.log.write(LogLevel.DEBUG, getClass().getSimpleName(), "noteEnabled = "
            +SudokuValue.Settings.isNoteEnabled());
      SudokuValue.log.write(LogLevel.DEBUG, getClass().getSimpleName(), "resetDialogEnabled = "
            +SudokuValue.Settings.isResetDialogEnabled());
      setVisible(false); //settting의 Frame 숨기기.
   }
}
