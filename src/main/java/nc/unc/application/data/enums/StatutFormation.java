package nc.unc.application.data.enums;

public enum StatutFormation {
  NONFORME("NON FORMÉ"), FORME("FORMÉ"), HABILITE("HABILITÉ");

  private String enumStringify;

  StatutFormation(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes statuts de formation sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getStatutFormationStr() {
    StatutFormation[] enums =  StatutFormation.values();
    String[] strings = new String[StatutFormation.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
