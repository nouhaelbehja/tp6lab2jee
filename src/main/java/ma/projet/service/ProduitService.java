package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class ProduitService extends AbstractService<Produit> {
    public ProduitService() { super(Produit.class); }
    public List<Produit> produitsParCategorie(int id) {
        return lire(s -> s.createQuery("from Produit p where p.categorie.id=:id order by p.reference", Produit.class).setParameter("id",id).list());
    }
    public List<Produit> produitsCommandesEntre(LocalDate debut, LocalDate fin) {
        verifierDates(debut,fin);
        return lire(s -> s.createQuery("select distinct l.produit from LigneCommande l where l.commande.date between :d and :f", Produit.class).setParameter("d",debut).setParameter("f",fin).list());
    }
    public List<Produit> produitsParCommande(int id) {
        return lire(s -> s.createQuery("select l.produit from LigneCommande l where l.commande.id=:id order by l.produit.reference", Produit.class).setParameter("id",id).list());
    }
    public List<Produit> produitsPrixSuperieur100() {
        return lire(s -> s.createNamedQuery("Produit.prixSuperieur100",Produit.class).list());
    }
    public void afficherProduitsCommande(int id) {
        lire(s -> {
            Commande c=s.get(Commande.class,id);
            if(c==null) throw new IllegalArgumentException("Commande introuvable : "+id);
            System.out.printf("Commande : %d     Date : %s%nListe des produits :%nRéférence   Prix    Quantité%n",id,c.getDate());
            s.createQuery("from LigneCommande l where l.commande.id=:id order by l.produit.reference",LigneCommande.class)
                .setParameter("id",id).list().forEach(l -> System.out.printf("%s   %s DH   %d%n",l.getProduit().getReference(),l.getPrixUnitaire(),l.getQuantite()));
            return null;
        });
    }
}
