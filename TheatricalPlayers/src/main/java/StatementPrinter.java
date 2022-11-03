import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
  public String print(Invoice invoice, Map<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;
    String result = String.format("Statement for %s\n", invoice.customer);
    StringBuffer sb = new StringBuffer();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int thisAmount = amountFor(perf, play);
      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this order
      // Use StringBuffer instead of String to avoid string concatenation
      sb.append(String.format("  %s: %s (%d seats)\n", play.name, frmt.format(thisAmount / 100), perf.audience));
      totalAmount += thisAmount;
    }
    // Use StringBuffer instead of String to avoid string concatenation
    sb.append(String.format("Amount owed is %s\n", frmt.format(totalAmount / 100)));
    sb.append(String.format("You earned %d credits\n", volumeCredits));
    result += sb.toString();
    return result;
  }

  private int amountFor(Performance perf, Play play) {
    int thisAmount = 0;
    // Determine amount for each type of play
    switch (play.type) {
      case "tragedy":
        thisAmount = 40000;
        if (perf.audience > 30) {
          thisAmount += 1000 * (perf.audience - 30);
        }
        break;
      case "comedy":
        thisAmount = 30000;
        if (perf.audience > 20) {
          thisAmount += 10000 + 500 * (perf.audience - 20);
        }
        thisAmount += 300 * perf.audience;
        break;
      default:
        throw new Error("unknown type: ${play.type}");
    }
    return thisAmount;
  }
}
