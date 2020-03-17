package utils;

import javax.swing.JLabel;

/**
 * 타이머에 대한 클래스이다.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class Timer extends Thread {
   private JLabel label;
   private int time;
   private boolean isEnabled;
   
   public Timer(JLabel label) {
      this(label, 1);
   }
   public Timer(JLabel label, int time) {
      this.time = time;
      this.label = label;
      this.isEnabled = true;
   }
   
   public boolean isEnabled() {
      return isEnabled;
   }
   
   public int getTime() {
      return time - 1;
   }
   
   /**
    * 타이머 비활성화 시키는 함수.
    */
   public synchronized void timerStop() {
      isEnabled = false;
   }

   @Override
   public void run() {
      while (isEnabled) {
         try {
            if (time < 999)
               label.setText(String.format("%03d", time++));
            Thread.sleep(1000); //1.0s
            if (isEnabled == false) interrupt(); //인터럽트 발생
         } catch (InterruptedException e) {
            isEnabled = false;
            break; //인터럽트가 발생되면 타이머 중단.
         }
      }
   }

   @Override
   public String toString() {
      return "Timer [time=" + time + ", isEnabled=" + isEnabled + "]";
   }
}
