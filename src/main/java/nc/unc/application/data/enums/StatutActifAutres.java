package nc.unc.application.data.enums;

public enum StatutActifAutres {
  ACTIF("ACTIF"),
  INACTIF("INACTIF");

  private String enumStringify;

  StatutActifAutres(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différents status sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getStatutActifAutresStr() {
    StatutActifAutres[] enums = StatutActifAutres.values();
    String[] strings = new String[StatutActifAutres.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
