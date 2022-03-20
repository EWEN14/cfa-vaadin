package nc.unc.application.data.enums;

public enum SituationAnneePrecedente {
  ETUDIANT("ÉTUDIANT"), ALTERNANT("ALTERNANT"),
  SALARIE("SALARIÉ"), SANS_EMPLOI("SANS EMPLOI"), AUTRE("AUTRE");

  private String enumStringify;

  SituationAnneePrecedente(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes situations sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getSituationAnneePrecedenteStr() {
    SituationAnneePrecedente[] situationAnneePrecedenteEnum = SituationAnneePrecedente.values();
    String[] situationAnneePrecedenteString = new String[SituationAnneePrecedente.values().length];
    for (int i = 0; i < situationAnneePrecedenteString.length; i++) {
      situationAnneePrecedenteString[i] = situationAnneePrecedenteEnum[i].getEnumStringify();
    }
    return situationAnneePrecedenteString;
  }
}
