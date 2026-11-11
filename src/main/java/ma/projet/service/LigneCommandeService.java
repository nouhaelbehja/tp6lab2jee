package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class LigneCommandeService extends AbstractService<LigneCommande> {
    public LigneCommandeService() { super(LigneCommande.class); }
    @Override public LigneCommande create(LigneCommande ligne) {
        if (ligne.getQuantite() <= 0 || ligne.getPrixUnitaire() == null || ligne.getPrixUnitaire().signum() < 0)
            throw new IllegalArgumentException("Quantité positive et prix unitaire positif ou nul requis");
        return super.create(ligne);
    }
}
