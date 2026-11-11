package ma.projet.beans;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity

public class Mariage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional=false)
    private Homme homme;
    @ManyToOne(optional=false)
    private Femme femme;
    @Column(nullable=false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    private int nbrEnfant;

    private String lieu;
    public Mariage() {}
    public Integer getId() { return id; }
    public Homme getHomme() { return homme; }
    public void setHomme(Homme homme) { this.homme = homme; }
    public Femme getFemme() { return femme; }
    public void setFemme(Femme femme) { this.femme = femme; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public int getNbrEnfant() { return nbrEnfant; }
    public void setNbrEnfant(int nbrEnfant) { this.nbrEnfant = nbrEnfant; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}
