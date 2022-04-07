package nc.unc.application.data.enums;

public enum Commune {
  NOUMEA("NOUMÉA"),
  FRANCE("FRANCE"),
  DUMBEA("DUMBÉA"),
  PAITA("PAÏTA"),
  MONTDORE("MONT-DORE"),
  BELEP("BÉLEP"),
  BOULOUPARIS("BOULOUPARIS"),
  BOURAIL("BOURAIL"),
  CANALA("CANALA"),
  FARINO("FARINO"),
  HIENGHENE("HIENGHÈNE"),
  HOUAILOU("HOUAÏLOU"),
  ILEDESPINS("ÎLE DES PINS"),
  KAALAGOMEN("KAALA-GOMEN"),
  KONE("KONÉ"),
  KOUAOUA("KOUAOUA"),
  KOUMAC("KOUMAC"),
  LAFOA("LA FOA"),
  LIFOU("LIFOU"),
  MARE("MARÉ"),
  MOINDOU("MOINDOU"),
  OUEGOA("OUÉGOA"),
  OUVEA("OUVÉA"),
  POINDIMIE("POINDIMIÉ"),
  PONERIHOUEN("PONERIHOUEN"),
  POUEBO("POUÉBO"),
  POUEMBOUT("POUEMBOUT"),
  POUM("POUM"),
  POYA("POYA"),
  SARRAMEA("SARRAMÉA"),
  THIO("THIO"),
  TOUHO("TOUHO"),
  VOH("VOH"),
  YATE("YATÉ"),
  AFRIQUE("AFRIQUE"),
  MADAGASCAR("MADAGASCAR"),
  MARTINIQUE("MARTINIQUE"),
  SUISSE("SUISSE"),
  TAHITI("TAHITI"),
  THAILANDE("THAÏLANDE"),
  VANUATU("VANUATU"),
  WALLIS("WALLIS"),
  AUTRE("AUTRE");

  private String enumStringify;

  Commune(String enumStringify) {
    this.enumStringify = enumStringify;
  }

  public String getEnumStringify() {
    return this.enumStringify;
  }

  /**
   * Tableau renvoyant les différentes communes sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getCommunesStr() {
    Commune[] communesEnum = Commune.values();
    String[] communesStr = new String[Commune.values().length];
    for (int i = 0; i < communesStr.length; i++) {
      communesStr[i] = communesEnum[i].getEnumStringify();
    }
    return communesStr;
  }
}
