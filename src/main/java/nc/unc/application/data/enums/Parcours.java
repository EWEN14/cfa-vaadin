package nc.unc.application.data.enums;

public enum Parcours {
  ALTERNANCE("ALTERNANCE"), CONTRATDEQUALIFICATION("CONTRAT DE QUALIFICATION"), FORMATIONCONTINUE("FORMATION CONTINUE"),
  STAGE("STAGE"), VAE("VAE");

  private String enumStringify;

  Parcours(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes types de parcours sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getParcoursStr() {
    Parcours[] parcoursEnum = Parcours.values();
    String[] parcoursString = new String[Parcours.values().length];
    for (int i = 0; i < parcoursString.length; i++) {
      parcoursString[i] = parcoursEnum[i].getEnumStringify();
    }
    return parcoursString;
  }
}
