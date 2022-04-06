package nc.unc.application.data.enums;

public enum PriseChargeFraisInscription {
  ALTERNANT("ALTERNANT"), EMPLOYEUR("EMPLOYEUR"), SANSOBJ("SANS OBJET");

  private String enumStringify;

  PriseChargeFraisInscription(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différents choix sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getSituationsStr() {
    PriseChargeFraisInscription[] enums =  PriseChargeFraisInscription.values();
    String[] strings = new String[PriseChargeFraisInscription.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
