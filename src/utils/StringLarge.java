package utils;

/**
 * String과 관련된 클래스.
 * 
 * @author 이창현(momi3355@hotmail.com)
 */
public class StringLarge {
   /**
    * 빈공간 만드는 함수.
    * 
    * @param spaceNum 빈공간 개수.
    * @return {@code String}의 빈공간 
    */
   public static String space(int spaceNum) {
      String str = "";
      for (int i = 0; i < spaceNum; i++)
         str += " ";
      
      return str;
   }
   
   /**
    * 파이썬(Python)의 {@code str.capitalize()}함수랑 기능이 같은 함수.
    * 
    * @param str {@code String}(문자열)
    * @return 첫 글자만 대문자로 변경 후 {@code String}으로 리턴.
    */
   public static String capitalize(String str) {
      return str.substring(0, 1).toUpperCase() + str.substring(1);
   }
   
   /**
    * {@code Arrays.toString()}의 {@code String} 1차 배열를<br>
    * {@code String} 2차 배열으로 변환하는 함수.
    * 
    * @param str {@code String}(문자열)
    * @return {@code String} 1차 배열
    */
   public static String[] toArray(String str) {
      if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
         return str.substring(1, str.length() - 1).trim().split(", "); //공백 제거
      } else return null;
   }
   
   /**
    * {@code String} 1차 배열의 각각 {@code String.Split()}하는 함수.
    * 
    * @param str {@code String}(문자열)
    * @param regex Split할 문자열
    * @return {@code String} 2차 배열
    */
   public static String[][] arraySplit(String[] str, String regex) {
      String[][] value = new String[str.length][];
      for (int i = 0; i < str.length; i++) {
         value[i] = str[i].split(regex);
      }
      
      return value;
   }
}
