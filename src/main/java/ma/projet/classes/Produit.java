package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@NamedQuery(name="Produit.prixSuperieur100", query="from Produit p where p.prix > 100 order by p.reference")
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false, unique=true)
    private String reference;
    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal prix;
    @ManyToOne(optional=false)
    private Categorie categorie;

    private String marque;

    private int stockMinimal;
    public Produit() {}
    public Integer getId() { return id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public int getStockMinimal() { return stockMinimal; }
    public void setStockMinimal(int stockMinimal) { this.stockMinimal = stockMinimal; }
}
