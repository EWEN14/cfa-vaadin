package nc.unc.application.data.enums;

import org.aspectj.apache.bcel.classfile.Code;

public enum CodeContrat {

  ENATTENTEDECUA("0. EN ATTENTE DECUA"),
  SANSCONTRAT("1. SANS CONTRAT"),
  CUAENCOURS("2. CUA EN COURS"),
  CUADEPOT("3. CUA - DÉPÔT ALFRESCO"),
  RUPTURE("4. RUPTURE + DÉMISSION UNC"),
  CHANGEMENTENTR("5. CHANGEMENT D'ENTREPRISE"),
  STAGE("6. STAGE");

  private String codeContrat;

  CodeContrat(String codeContrat){
    this.codeContrat = codeContrat;
  }

  public String getCodeContrat(){
    return this.codeContrat;
  }

  /**
   * Tableau renvoyant les différents code de contrats sous forme de String.
   * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
   */
  public static String[] getCodeContratStr() {
    CodeContrat[] enums =  CodeContrat.values();
    String[] strings = new String[CodeContrat.values().length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = enums[i].getCodeContrat();
    }
    return strings;
  }
}
