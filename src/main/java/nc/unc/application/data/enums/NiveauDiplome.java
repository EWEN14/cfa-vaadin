package nc.unc.application.data.enums;

public enum NiveauDiplome {

    UN("1"), DEUX("2"), TROIS("3"), QUATRE("4"), CINQ("5"), SIX("6"), SEPT("7"), HUIT("8");

    private String enumStringNd;

    NiveauDiplome(String enumStringNd) {
        this.enumStringNd = enumStringNd;
    }

    public String getEnumStringNd() {
        return this.enumStringNd;
    }

    /**
     * Tableau renvoyant les différentes situations sous forme de String.
     * @return tableau de chaînes de caractères qui sont les versions String de chaque Enum
     */
    public static String[] getNiveauDiplomeStr() {
        NiveauDiplome[] niveauDiplomesEnum =  NiveauDiplome.values();
        String[] niveauDiplomeString = new String[NiveauDiplome.values().length];
        for (int i = 0; i < niveauDiplomeString.length; i++) {
            niveauDiplomeString[i] = niveauDiplomesEnum[i].getEnumStringNd();
        }
        return niveauDiplomeString;
    }
}
