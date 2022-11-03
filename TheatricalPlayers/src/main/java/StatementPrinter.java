import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
  public String print(Invoice invoice, Map<String, Play> plays) {
    // Manage directly the sums by floating and not in full /100 (currently, $ 100 = 10000).
    // This will allow to manage the rounding of the cents.

    float totalAmount = 0;
    int volumeCredits = 0;
    String result = String.format("Statement for %s\n", invoice.customer);
    StringBuffer sb = new StringBuffer();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      float thisAmount = amountFor(perf, play);
      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this order
      // Use StringBuffer instead of String to avoid string concatenation
      sb.append(String.format("  %s: %s (%d seats)\n", play.name, frmt.format(thisAmount), perf.audience));
      totalAmount += thisAmount;
    }
    // Use StringBuffer instead of String to avoid string concatenation
    sb.append(String.format("Amount owed is %s\n", frmt.format(totalAmount)));
    sb.append(String.format("You earned %d credits\n", volumeCredits));
    result += sb.toString();
    return result;
  }

  private int amountFor(Performance perf, Play play) {
    int thisAmount = 0;
    // Determine amount for each type of play
    switch (play.type) {
      case "tragedy":
        thisAmount = 400;
        if (perf.audience > 30) {
          thisAmount += 10 * (perf.audience - 30);
        }
        break;
      case "comedy":
        thisAmount = 300;
        if (perf.audience > 20) {
          thisAmount += 100 + 5 * (perf.audience - 20);
        }
        thisAmount += 3 * perf.audience;
        break;
      case "NoOne":
        throw new Error("unknown type: ${play.type}");
      default:
        throw new Error("unknown type: ${play.type}");
    }
    return thisAmount;
  }
}
