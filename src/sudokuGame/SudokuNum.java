package sudokuGame;

import java.util.Vector;

import javax.swing.JButton;

/**
 * 스도쿠의 {@code SudokuValue.nums}값의 숫자가 들어갈 수 있는 수({@code value})를 추가한 클래스
 * 
 * @author 이창현(momi3355@hotmail.com)
 * 
 * @see SudokuValue.nums
 * @see value
 */
@SuppressWarnings("serial")
class SudokuNum extends JButton {
   /** 숫자가 들어갈 수 있는 수 */
   private Vector<Integer> value = new Vector<Integer>(SudokuValue.MAX_NUM);
   /** note가 입력된 수 */
   Vector<Integer> noteNums = new Vector<Integer>(SudokuValue.MAX_NUM);
   
   SudokuNum() {
      super();
   }
   SudokuNum(String name) {
      super(name);
   }
   
   Integer[] getValue() {
      return value.toArray(new Integer[value.size()]);
   }

   void addValue(int num) {
      value.add(num);
   }
   
   boolean isValue(int num) {
      return (value.indexOf(num) < 0) ? false : true;
   }
   boolean isValue(int index, int num) {
      return (value.elementAt(index) != num) ? false : true;
   }
   
   void removeValue(int num) {
      if (value.indexOf(num) >= 0)
         value.remove(value.indexOf(num));
   }
   
   void removeAllValue() {
      value.removeAllElements();
   }
}
