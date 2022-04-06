package nc.unc.application.data.enums;

public enum Admis {
  LISTE_PRINCIPALE("LISTE PRINCIPALE"), LC1("LC 1"), LC2("LC 2"), LC3("LC 3"), LC4("LC 4"), LC5("LC 5"),
  LC6("LC 6"), LC7("LC 7"), LC8("LC 8"), CANDIDATURE_SUPPL("CANDIDATURE SUPPLÉMENTAIRE"), VAE("VAE"),
  FORMATION_CONTINUE("FORMATION CONTINUE"), MASTER1("MASTER 1"), MASTER2("MASTER 2");

  private String enumStringAdmis;

  Admis(String enumStringAdmis) {
    this.enumStringAdmis = enumStringAdmis;
  }

  public String getEnumStringAdmis() {
    return this.enumStringAdmis;
  }

  /**
   * Tableau renvoyant les différents statuts d'admission sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getAdmisStr() {
    Admis[] admisEnum =  Admis.values();
    String[] admisString = new String[Admis.values().length];
    for (int i = 0; i < admisString.length; i++) {
      admisString[i] = admisEnum[i].getEnumStringAdmis();
    }
    return admisString;
  }
}
