package nc.unc.application.data.enums;

public enum Nationalite {
  FRANCAISE("FRANÇAISE"), VANUATAISE("VANUATAISE"), ETRANGERE("ÉTRANGÈRE");

  private String enumStringNationalite;

  Nationalite(String enumStringNationalite) {
    this.enumStringNationalite = enumStringNationalite;
  }

  public String getEnumStringNationalite() {
    return this.enumStringNationalite;
  }

  /**
   * Tableau renvoyant les différents nationalités sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getNationalitesStr() {
    Nationalite[] nationalitesEnum =  Nationalite.values();
    String[] nationalitesString = new String[Nationalite.values().length];
    for (int i = 0; i < nationalitesString.length; i++) {
      nationalitesString[i] = nationalitesEnum[i].getEnumStringNationalite();
    }
    return nationalitesString;
  }
}
