import java.io.IOException;
import java.util.*;


//Changed methods by new methods used in Invoice class
public class StatementPrinter {
  public String toText(Invoice invoice, Map<String, Play> plays) {
    Invoice invoice2 = new Invoice(invoice, plays);
    return invoice2.toText();
  }

  public void toHTML(Invoice invoice, Map<String, Play> plays) throws IOException {
    Invoice invoice2 = new Invoice(invoice, plays);
    invoice2.toHTML();
  }
}
