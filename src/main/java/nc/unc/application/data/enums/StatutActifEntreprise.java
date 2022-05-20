package nc.unc.application.data.enums;

public enum StatutActifEntreprise {
  DEMARCHEE("DÉMARCHÉE"),
  ENTREPRISE_ACTIVE("ENTREPRISE ACTIVE"),
  ANCIENNE_ENTREPRISE("ANCIENNE ENTREPRISE");

  private String enumStringify;

  StatutActifEntreprise(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes situations sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getStatutActifEntrepriseStr() {
    StatutActifEntreprise[] enums = StatutActifEntreprise.values();
    String[] strings = new String[StatutActifEntreprise.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
