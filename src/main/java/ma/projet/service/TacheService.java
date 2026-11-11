package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class TacheService extends AbstractService<Tache> {
    public TacheService() { super(Tache.class); }
    public List<Tache> prixSuperieur1000() {
        return lire(s -> s.createNamedQuery("Tache.prixSuperieur1000",Tache.class).list());
    }
    public List<Tache> realiseesEntre(LocalDate debut, LocalDate fin) {
        verifierDates(debut,fin);
        return lire(s -> s.createQuery("select distinct e.tache from EmployeTache e where e.dateDebutReelle >= :d and e.dateFinReelle <= :f",Tache.class).setParameter("d",debut).setParameter("f",fin).list());
    }
}
