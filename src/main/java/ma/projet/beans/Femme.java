package ma.projet.beans;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@NamedQuery(name="Femme.auMoinsDeuxMariages", query="select m.femme from Mariage m group by m.femme having count(m.id) >= 2")
@NamedNativeQuery(name="Femme.nombreEnfants", query="select coalesce(sum(nbrEnfant),0) from Mariage where femme_id = :femmeId and dateDebut >= :debut and dateDebut <= :fin")
public class Femme extends Personne {
    public Femme() {}
}
