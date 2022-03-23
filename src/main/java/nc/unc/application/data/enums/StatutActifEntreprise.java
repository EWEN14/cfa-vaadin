package nc.unc.application.data.enums;

public enum StatutActifEntreprise {
  DEMARCHEE("Démarchée"), ENTREPRISE_ACTIVE("Entreprise Active"), ANCIENNE_ENTREPRISE("Ancienne Entreprise");

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
    StatutActifEntreprise[] statutActifEntrepriseEnum = StatutActifEntreprise.values();
    String[] statutActifEntrepriseString = new String[SituationAnneePrecedente.values().length];
    for (int i = 0; i < statutActifEntrepriseString.length; i++) {
      statutActifEntrepriseString[i] = statutActifEntrepriseEnum[i].getEnumStringify();
    }
    return statutActifEntrepriseString;
  }
}
