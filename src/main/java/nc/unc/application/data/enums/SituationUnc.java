package nc.unc.application.data.enums;

public enum SituationUnc {
  ENFORMATION("EN COURS DE FORMATION"), BAC2("BAC+2 NON OBTENU"),
  DEM("DÉMISSION UNC EN COURS DE FORMATION "),
  DESIST("DÉSISTEMENT"), DESISTUNC("DESISTEMENT UNC (PAS DE CUA)"),
  FERMETURE("FERMETURE DE LA LP"), LISTECOMPL("SUR LISTE COMPLEMENTAIRE - NON APPELÉ"),
  TERMINE("TERMINÉ"), SANSOBJ("SANS OBJET");

  private String enumStringify;

  SituationUnc(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes situations sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getSituationsStr() {
    SituationUnc[] enums =  SituationUnc.values();
    String[] strings = new String[SituationUnc.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
