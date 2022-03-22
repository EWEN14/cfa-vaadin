package nc.unc.application.data.enums;

public enum Commune {
  NOUMEA("NOUMÉA"),PAITA("PAÏTA"),DUMBEA("DUMBÉA"),MONTDORE("MONT-DORE"),YATE("YATÉ"),ILEDESPINS("ÎLE DES PINS"),
  BOULOUPARIS("BOULOUPARIS"),LAFOA("LA FOA"),SARRAMEA("SARRAMÉA"),THIO("THIO"),
  FARINO("FARINO"),MOINDOU("MOINDOU"),MARE("MARÉ"),BOURAIL("BOURAIL"),POYA("POYA"),POUEMBOUT("POUEMBOUT"),
  KONE("KONÉ"),VOH("VOH"),KAALAGOMEN("KAALA-GOMEN"),KOUMAC("KOUMAC"),POUM("POUM"),
  BELEP("BÉLEP"),OUEGOA("OUÉGOA"),POUEBO("POUÉBO"),HIENGHENE("HIENGHÈNE"),TOUHO("TOUHO"),
  POINDIMIE("POINDIMIÉ"),PONERIHOUEN("PONERIHOUEN"),HOUAILOU("HOUAÏLOU"),KOUAOUA("KOUAOUA"),
  FRANCE("FRANCE"),MARTINIQUE("MARTINIQUE"),CANALA("CANALA"),OUVEA("OUVÉA"),LIFOU("LIFOU"),WALLIS("WALLIS"),
  SUISSE("SUISSE"),MADAGASCAR("MADAGASCAR"),AFRIQUE("AFRIQUE"),
  THAILANDE("THAILANDE"),VANUATU("VANUATU"),TAHITI("TAHITI"), AUTRE("AUTRE");

  private String enumStringify;

  Commune(String enumStringify) {
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
    Commune[] communesEnum = Commune.values();
    String[] communesStr = new String[Commune.values().length];
    for (int i = 0; i < communesStr.length; i++) {
      communesStr[i] = communesEnum[i].getEnumStringify();
    }
    return communesStr;
  }
}
