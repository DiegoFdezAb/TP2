import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class Invoice {
  public String customer;
  public List<Performance> performances;
  public float totalAmount;
  public int volumeCredits;

  public Invoice(String customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
    this.totalAmount = 0;
    this.volumeCredits = 0;
  }

  List <Info> info = new ArrayList<Info>();
  StringBuffer sb = new StringBuffer();
  String customerName= "";
NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
  public Invoice(Invoice invoice, Map<String, Play> plays) {   

    
    customerName = invoice.customer;
    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      float thisAmount = amountFor(perf, play);
      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // i stock the information of the play name, the amount and the audience in a list of Info objects

      Info i = new Info(play.name, frmt.format(thisAmount), perf.audience);

      totalAmount += thisAmount;
      info.add(i);
    }
  }

  private float amountFor(Performance perf, Play play) {
    float thisAmount = 0;
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

  public String toText() {
    sb.append("Statement for " + customerName + "\n");
    for (Info i : info) {
      sb.append("  " + i.name + ": " + i.amount + " (" + i.audience + " seats)\n");
    }
    sb.append("Amount owed is " + frmt.format(totalAmount) + "\n");
    sb.append("You earned " + volumeCredits + " credits" + "\n");
    return sb.toString();
  }

  public void toHTML() throws IOException {
    // Create a new file
    File f = new File("statement.html");
    // Create a new buffered writer
    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
    // Write the HTML in file with tab and visible line break
    


    bw.write("<h1>Invoice</h1>");
    //Write "(dot) Client :" in bold and customername normal
    bw.write("<b>Client :</b> " + customerName + "");   

    bw.write("<table border = 1>");
    bw.write("<tr><th>Piece</th><th>Seats sold</th><th>Price</th></tr>");
    for (Info i : info) {
      bw.write("<tr><td>" + i.name + "</td><td>" + i.audience + "</td><td>" + i.amount + "</td></tr>");
    }

    // Amount owed and You earned in the tab
    bw.write("<tr><td colspan = 2>Total owed is:</td><td>" + frmt.format(totalAmount) + "</td></tr>");
    bw.write("<tr><td colspan = 2>Fidelity points earned:</td><td>" + volumeCredits + "</td></tr>");
    


    bw.write("</table>");
    //Write "Payment is required under 30 days. We can break your knees if you don't do so".
    bw.write("<p>Payment is required under 30 days. We can break your knees if you don't do so</p>");


    // Close the buffered writer
    bw.close();
  }
}
