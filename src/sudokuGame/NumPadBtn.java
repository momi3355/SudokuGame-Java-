package sudokuGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.State;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * 스도쿠의 숫자패드에 대한 클래스이다.
 * 
 * @author 이창현(momi3355@hotmail.com)
 * 
 * @see JButton
 * @see ActionListener
 */
@SuppressWarnings("serial")
class NumPadBtn extends JButton implements ActionListener {
   /** #3264FF */
   private static final Color NOTE_ENABLED = new Color(50, 100, 255);
   /** note 활성화 변수 */
   private static boolean isNoteEnabled = false;
   
   NumPadBtn(String name) {
      super(name);
      
      addActionListener(this);
   }
   
   @Override
   public void actionPerformed(ActionEvent event) {
      if (SudokuValue.timer.getState() == State.TERMINATED) return;
      else if (SudokuValue.timer.getState() == State.NEW) return;
      //타이머가 멈춰있으면(클리어 or 시작하지 않는), 무반응.
      Point select = null;
      try {
         select = selectNums(); //선택한 숫자를 찾는다.
         Integer num = Integer.valueOf(getText());
         
         if (num instanceof Number) { //getText()가 숫자면
            //글꼴이 굵게 표기된것은 변경할 수 없다.
            if (SudokuValue.nums[select.x][select.y].getFont().getStyle() != Font.BOLD) {
               if (!isNoteEnabled) { //note 버튼 비활성화 때.
                  //같은 숫자가 없으면.
                  if (!SudokuValue.nums[select.x][select.y].getText().equals(getText())) {
                     removeAllElementsNote(select.x, select.y);
                     SudokuValue.nums[select.x][select.y].setFont(new Font("Default", Font.PLAIN, 12));
                     SudokuValue.nums[select.x][select.y].setText(getText());
                  } else SudokuValue.nums[select.x][select.y].setText("");
               } else { //note 버튼 활성화 때.
                  SudokuValue.nums[select.x][select.y].setFont(new Font("돋움체", Font.PLAIN, 10));
                  setNote(select.x, select.y, getText());
               }
               SudokuValue.IBackGround.alterationColor(select.x, select.y);
            } //end if (getFont().getStyle() != Font.BOLD);
         } //end if (num instanceof Number);
      } catch (NumberFormatException e) { //getText()가 숫자아니면
         if (getFont().getStyle() == Font.BOLD) {
            switch (getText()) {
            case "N": //note
               if (!getBackground().equals(NOTE_ENABLED)) {
                  setBackground(NOTE_ENABLED);
                  setForeground(Color.WHITE);
                  isNoteEnabled = true;
               } else {
                  setBackground(null); //null이면 기본값;
                  setForeground(Color.BLACK);
                  isNoteEnabled = false;
               }
               break;
            case "E": //erasure
               SudokuValue.nums[select.x][select.y].setText("");
               SudokuValue.IBackGround.alterationColor(select.x, select.y);
               removeAllElementsNote(select.x, select.y);
               break;
            case "C": //clear
               int result = JOptionPane.showConfirmDialog(null, "지금 적혀있는 숫자가 삭제됩니다.\n"
                     + "     그래도 계속하시겠습니까?", 
                     "경고", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
               if (result != JOptionPane.YES_OPTION) return;
               
               for (int i = 0; i < SudokuValue.nums.length; i++) {
                  for (int j = 0; j < SudokuValue.nums[i].length; j++) {
                     if (SudokuValue.nums[i][j].getFont().getStyle() != Font.BOLD) {
                        SudokuValue.nums[i][j].setText("");
                     }
                  }
               }
               removeAllElementsNote();
               SudokuValue.IBackGround.alterationColor(select.x, select.y);
               break;
            default:
               throw new NumberPadNotFoundException(getText());
            }
         }
      } catch (SelectNumsNotFoundException e) { //선택한 숫자를 찾을 수 없을때.
         return; //(아무것도 안한다.)
      } finally {
         //클리어 한 경우.
         if (isGameClear()) {
            if (SudokuValue.clearBoard(false)) { //이때 타이머 중단이 됨.
               //신기록 한 경우.
               JOptionPane.showMessageDialog(null, "클리어 시간 : " 
                     + SudokuValue.timer.getTime() + " 초",
                     SudokuValue.level.getText() + " 신기록!!", JOptionPane.INFORMATION_MESSAGE);
            }
         } //end if (isGameClear());
      } //end try();
   }
   
   /**
    * 선택한 {@code nums}의 좌표를 찾는 함수.
    * 
    * @return 선택한 {@code nums}의 좌표
    * @throws SelectNumsNotFoundException 선택한 {@code nums}를 찾을수 없는 예외.
    */
   private Point selectNums() throws SelectNumsNotFoundException {
      for (int i = 0; i < SudokuValue.nums.length; i++) {
         for (int j = 0; j < SudokuValue.nums[i].length; j++) {
            //내가 선택한 버튼이라면
            if (SudokuValue.nums[i][j].getBackground()
                  .equals(SudokuValue.IBackGround.SELECT_COLOR)) {
               return new Point(i, j);
            }
         }
      }
      throw new SelectNumsNotFoundException();
   }

   /**
    * 게임 클리어 되었는지 확인하는 함수.
    * 
    * @return <code>true</code>면 게임 클리어했고,<br>
    * <code>false</code>면 클리어 하지 않았다.
    */
   private boolean isGameClear() {
      //getName()과 getText()를 비교 해서 맞는 경우.
      if (SudokuValue.isSolve()) {
         return true;
      //getName()과 getText()가 다르지만, 풀었는게 맞는 경우.
      } else if (!SudokuValue.isEquals()) {
         return true;
      } else return false;
   }
   
   /**
    * note의 {@code nums}에 추가.
    * 
    * @param x nums.x
    * @param y nums.y
    * @param num {@code String} 숫자
    */
   private void addNote(int x, int y, String num) {
      if (SudokuValue.nums[x][y].noteNums.indexOf(Integer.valueOf(num)) < 0)
         SudokuValue.nums[x][y].noteNums.add(Integer.valueOf(num));
      else SudokuValue.nums[x][y].noteNums.remove(Integer.valueOf(num));
   }
   
   /**
    * note의 {@code String}를 저장 하는 함수.
    * 
    * @param x nums.x
    * @param y nums.y
    * @param num {@code String} 숫자
    */
   private void setNote(int x, int y, String num) {
      addNote(x, y, num);
      final Vector<Integer> noteNums = SudokuValue.nums[x][y].noteNums;
      //setText()에서는 "\n"가 안된다.
      //html로 저장된다. (<br>를 사용하기 위함 이다.)
      String note = "<html>";
      for (int i = 1; i <= SudokuValue.MAX_NUM; i++) {
         if (!(i % 3 == 0))
            note = note.concat((noteNums.indexOf(i) >= 0) ? i+"&nbsp;" : "&nbsp;&nbsp;");
         else note = note.concat((noteNums.indexOf(i) >= 0) ? i+"<br />" : "&nbsp;<br />");
      }
      note = note.concat("</html>");
      SudokuValue.nums[x][y].setText(note);
   }

   /**
    * 모든 num에 있는 note의 모든 원소를 제거하는 함수.
    */
   private void removeAllElementsNote() {
      for (int x = 0; x < SudokuValue.nums.length; x++)
         for (int y = 0; y < SudokuValue.nums[x].length; y++)
            SudokuValue.nums[x][y].noteNums.removeAllElements();
   }
   /**
    * note의 모든 원소를 제거하는 함수.
    * 
    * @param x nums.x
    * @param y nums.y
    */
   private void removeAllElementsNote(int x, int y) {
      SudokuValue.nums[x][y].noteNums.removeAllElements();
   }
}
