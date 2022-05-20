package nc.unc.application.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

  /**
   * utilisation d'un DateTimeFormatter pour mettre les dates au format français dans les grid contenant des dates
   * @param localDate la date
   * @return une date sous forme de String au format français
   */
  public static String frenchDateFormater(LocalDate localDate) {
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return DATE_FORMATTER.format(localDate);
  }
}
