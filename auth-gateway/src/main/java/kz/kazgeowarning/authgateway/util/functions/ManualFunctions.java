package kz.kazgeowarning.authgateway.util.functions;

public class ManualFunctions {

   private static final String key = "DDAF35A193617ABACC417349AE20413112E6FA4E89A97EA20A9EEEE64B55D39A2192992A274FC1A836BA3C23A3FEEBBD454D4423643CE80E2A9AC94FA54CA49F";

   public static String getSha256String(){
//      String originalString =  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
//      return Hashing.sha256()
//              .hashString(originalString, StandardCharsets.UTF_8)
//              .toString();
      return key;
   }

   public static boolean isNumeric(String str) {
      for (char c : str.toCharArray()) {
         if (!Character.isDigit(c)) return false;
      }
      return true;
   }
}
