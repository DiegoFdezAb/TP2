public class Play {

  //Add enum to get new types of plays

  public enum Type {
    tragedy, comedy
  }

  public String name;
  public String type;

  public Play(String name, String type) {
    this.name = name;
    this.type = isValidType(type);
  }

  // Check if the type of play is valid using public enum Type

  public String isValidType(String type) {
    for (Type t : Type.values()) {
      if (t.name().equals(type)) {
        return type;
      }
    }
    return "unknown type: ${play.type}";
  }
}
