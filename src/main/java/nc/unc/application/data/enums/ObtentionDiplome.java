package nc.unc.application.data.enums;

public enum ObtentionDiplome {
  AJOURNE("AJOURNÉ"), OBTIENT("OBTIENT"), OBTIENT_AB("OBTIENT AB"), OBTIENT_B("OBTIENT B"),
  OBTIENT_TB("OBTIENT_TB"), SANSOBJ("SANS OBJET");

  private String enumStringify;

  ObtentionDiplome(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différents choix sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getObtentionDiplomeStr() {
    ObtentionDiplome[] enums =  ObtentionDiplome.values();
    String[] strings = new String[ObtentionDiplome.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
