public class Play {

  public String name;
  public String type;

  
  public Play(String name, String type) {
    this.name = name;
    this.type = isValidType(type);
  }

  // Create static variables for differents play types and add control over play types at object creation, etc.

  public static final String TRAGEDY = "tragedy";
  public static final String COMEDY = "comedy";

  public static final String[] PLAY_TYPES = {TRAGEDY, COMEDY};
  
  public static String isValidType(String type) {
    for (String playType : PLAY_TYPES) {
      if (playType.equals(type)) {
        return type;
      }
    }
    return "NoOne";
  }
}
