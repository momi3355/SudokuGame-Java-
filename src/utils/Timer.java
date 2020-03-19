package utils;

import javax.swing.JLabel;

/**
 * 타이머에 대한 클래스이다.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class Timer extends Thread {
   /** 999s = 16m39s */
   public static final int MAX_SECOND = 999;
   /** 6039s = 99m99s */
   public static final int MAX_MINUTE = toSecond("99:99");
   
   /** {@code time}을 표시하는 {@code JLabel} */
   private JLabel label;
   /** 타이머 설정을 저장하는 변수. */
   private TimerType type;
   /** 타이머의 값이 저장되는 변수. */
   private int time;
   /** 타이머가 활성화 된지 확인 하는 변수. */
   private boolean isEnabled;
   
   /** 타이머 설정 열거형 */
   public enum TimerType { second, minute;
      public static String[] valuestoString() {
         String[] toString = new String[values().length];
         for (int i = 0; i < toString.length; i++)
            //toString[i] = capitalize(values()[i].toString());
            toString[i] = values()[i].toString().substring(0, 1).toUpperCase() 
                        + values()[i].toString().substring(1);
         return toString;
      }
   } //end enum Timer;
   
   public Timer(JLabel label) {
      this(label, 1, TimerType.second);
   }
   public Timer(JLabel label, int time) {
      this(label, time, TimerType.second);
   }
   public Timer(JLabel label, int time, TimerType type) {
      this.time = time;
      this.type = type;
      this.label = label;
      this.isEnabled = true;
   }
   
   /**
    * 타이머가 활성화 된지 확인 하는 함수.
    * 
    * @return 타이머 활성화
    */
   public boolean isEnabled() {
      return isEnabled;
   }
   
   /**
    * 타이머의 값을 리턴 하는 함수.
    * 
    * @return 타이머의 값(second)
    */
   public int getTime() {
      return time - 1;
   }
   
   /**
    * 타이머 설정을 리턴하는 함수.
    * 
    * @return 타이머 설정
    */
   public TimerType getType() {
      return type;
   }
   
   /**
    * 타이머의 설정의 재설정하는 함수.
    * <p>
    * 타이머의 설정의 재설정하고,<br>
    * {@code label}의 text를 {@code TimerType}포맷에 맞게 설정한다. 
    * </p>
    * @param type 타이머 설정
    */
   public void setType(TimerType type) {
      this.type = type;
      switch (this.type) {
      case second:
         label.setText("000");
         break;
      case minute:
         label.setText("00:00");
      }
   }
   
   /**
    * Minute에서 Second으로 변환 하는 함수.
    * 
    * @param min "00:00"이 포맷인 {@code String}
    * @return 분을 초로 변환 후 리턴.
    */
   public static int toSecond(String min) {
      try {
         return Integer.valueOf(min); //초만 있는 경우.
      } catch (NumberFormatException e) {
         String[] num = min.split(":");
         return (Integer.parseUnsignedInt((!num[0].equals("")) ? num[0] : "00") * 60) 
               + Integer.parseUnsignedInt((num.length > 1) ? num[1] : "00");
      }
   }
   
   /**
    * Second에서 Minute으로 변환 하는 함수.
    * 
    * @param sec 9999 미만인 값
    * @return "00:00"이 포맷인 {@code String}으로 리턴
    */
   public static String toMinute(int sec) {
      return String.format("%02d:%02d", (sec/60), (sec%60));
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
            if (time < MAX_MINUTE) {
               switch (type) {
               case second:
                     label.setText(String.format("%03d", time));
                  break;
               case minute:
                     label.setText(toMinute(time));
               }
               time++;
            }
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
      return "Timer [time=" + time + ", isEnabled=" + isEnabled + ", type=" + type + "]";
   }
}
