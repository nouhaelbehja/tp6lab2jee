package ma.projet.beans;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Personne {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    private String prenom;

    private String telephone;

    private String adresse;

    private LocalDate dateNaissance;
    @Column(unique=true)
    private String cin;

    private String ville;
    public Personne() {}
    public Integer getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
}
