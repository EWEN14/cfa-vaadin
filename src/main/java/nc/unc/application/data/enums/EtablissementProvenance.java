package nc.unc.application.data.enums;

public enum EtablissementProvenance {
  CCI("CCI"), UNC("UNC"), LBP("LYCÉE BLAISE PASCAL"), LGN("LYCÉE DU GRAND NOUMÉA"),
  LJG("LYCÉE JULES GARNIER "), LLAP("LYCÉE LAPÉROUSE "), LMD("LYCÉE MONT DORE"),
  LPESCOF("LYCÉE PRO A. ESCOFFIER"), LMR("LYCÉE MICHEL ROCARD"), LAK("LYCÉE ANTOINE KELA "),
  LPAA("LYCÉE PRO APPOLINAIRE ANOVA"), LPFA("LYCÉE PRO FRANCOISE D'ASSISE"),
  LPPA("LYCÉE PRO PÉTRO ATTITI"), LPMC("LYCÉE PRO MARCELLIN CHAMPAGNAT"),
  LPSJC("LYCÉE PRO SAINT JOSEPH DE CLUNY"), CNAM("CNAM"), AUTREFR("AUTRE MÉTROPOLE"),
  AUTRENC("AUTRE NOUVELLE-CALÉDONIE"), AUTREETR("AUTRE ÉTRANGER");

  private String enumString;

  EtablissementProvenance(String enumString) {
    this.enumString = enumString;
  }

  public String getEnumString() {
    return this.enumString;
  }

  /**
   * Tableau renvoyant les différents établissements de provenance sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getEtablissementProvenanceStr() {
    EtablissementProvenance[] enums =  EtablissementProvenance.values();
    String[] strings = new String[EtablissementProvenance.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumString();
    }
    return strings;
  }
}
