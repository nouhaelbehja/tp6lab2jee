package ma.projet.test;
import ma.projet.util.HibernateUtil;
public final class Demonstration {
    public static void main(String[] args) {
        try {
            String choix=args.length==0 ? "tout" : args[0];
            switch(choix) {
                case "stock" -> Scenarios.stock();
                case "projets" -> Scenarios.projets();
                case "etat-civil" -> Scenarios.etatCivil();
                case "tout" -> { Scenarios.stock(); Scenarios.projets(); Scenarios.etatCivil(); }
                default -> throw new IllegalArgumentException("Choix : stock, projets, etat-civil ou tout");
            }
        } finally { HibernateUtil.shutdown(); }
    }
}
