package nc.unc.application.data.enums;

public enum DernierDiplomeObtenu {

  AUTREDIPLOME_ETR("Autre diplôme obtenu à l'étranger"),
  AUTREDIPLOME_MET("Autre diplôme obtenu en Métropole"),
  AUTREDIPLOME_NC("Autre diplôme obtenu en Nouvelle-Calédonie"),
  BAC("Baccalauréat"),
  BACHELOR("Bachelor"),
  BTS_ASS_GES("BTS Assistant de gestion PME-PMI"),
  BTS_ASS_TEC("BTS Assistant technique d'ingénieur"),
  BTS_BAT("BTS Bâtiment"),
  BTS_BIO("BTS Bioanalyses et contrôles"),
  BTS_CI("BTS Commerce International"),
  BTS_COM("BTS Communication"),
  BTS_COMPTA("BTS Comptabilité et gestion des organisations"),
  BTS_ELEC("BTS Electrotechnique"),
  BTS_EEC("BTS Etude et économie de la construction"),
  BTS_MUC("BTS Management des unités commerciales"),
  BTS_NDRC("BTS Négociation et digitalisation de la relation client"),
  BTS_PP("BTS Pilote de Procédés"),
  BTS_SIO("BTS Services informatiques aux organisations"),
  BTS_SP3S("BTS SP3S"),
  BTS_SAM("BTS Support à l'action managériale"),
  BTS_SN("BTS Systèmes numériques"),
  BTS_TC("BTS Technico-Commercial"),
  BTS_T("BTS Tourisme"),
  BTS_TPL("BTS Transport et prestations logistiques"),
  BTS_DARC("Développement de l'agriculture des régions chaudes"),
  DAEU("DAEU"),
  DUT_GEA("DUT Gestion des entreprises et des administration"),
  DUT_MMI("DUT Métiers du Multimédia et de l'Internet"),
  L2_EG("L2 Economie et gesttion"),
  L2_M("L2 Mathématiques"),
  L3_EG("L3 Economie et gestion"),
  LP("Licence Professionnelle"),
  M1_MAE("Master 1 Management et Administration des Entreprises"),
  M2_MAE("Master 2 Management et Administration des Entreprises"),
  M1_MIAGE("Master 1 Méthodes Informatiques Appliqués à la Gestion des Entreprises"),
  M2_MIAGE("Master 2 Méthodes Informatiques Appliqués à la Gestion des Entreprises");

  private String enumString;

  DernierDiplomeObtenu(String enumString) {
    this.enumString = enumString;
  }

  public String getEnumString() {
    return this.enumString;
  }

  /**
   * Tableau renvoyant les différents diplômes sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getDiplomeStr() {
    DernierDiplomeObtenu[] enums =  DernierDiplomeObtenu.values();
    String[] strings = new String[DernierDiplomeObtenu.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getEnumString();
    }
    return strings;
  }
}
