package nc.unc.application.data.enums;

public enum SituationEntreprise {
  CUA("CUA"), SANSOBJ("SANS OBJET"), RUP_CUA_AP_ESSAI("RUPTURE CUA APRÈS LA PÉRIODE D'ESSAI"),
  RUP_CUA_ESSAI("RUPTURE CUA PENDANT LA PÉRIODE D'ESSAI"),
  RUP_CUA_FIN("RUPTURE CUA EN VUE D'UNE EMBAUCHE / FIN DE FORMATION"),
  CONV_STAGE("CONVENTION DE STAGE"), CONV_FC("CONVENTION FC"),
  AVNCHANGTUT("AVENANT CHANGEMENT DE TUTEUR"), AUTREAVN("AUTRE AVENANT"),
  COTUT("CO TUTEUR"), CONTR_APR("CONTRAT D'APPRENTISSAGE"), CONTR_QUAL("CONTRAT DE QUALIFICATION");

  private String enumStringify;

  SituationEntreprise(String enumStringify) {
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
    SituationEntreprise[] enums =  SituationEntreprise.values();
    String[] strings = new String[SituationEntreprise.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumStringify();
    }
    return strings;
  }
}
