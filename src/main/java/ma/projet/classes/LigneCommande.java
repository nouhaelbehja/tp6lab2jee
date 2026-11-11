package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"produit_id","commande_id"}))
public class LigneCommande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional=false)
    private Produit produit;
    @ManyToOne(optional=false)
    private Commande commande;

    private int quantite;
    @Column(precision=12, scale=2)
    private BigDecimal prixUnitaire;
    public LigneCommande() {}
    public Integer getId() { return id; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}
