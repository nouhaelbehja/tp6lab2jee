package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class FemmeService extends AbstractService<Femme> {
    public FemmeService() { super(Femme.class); }
    public Femme plusAgee() {
        return lire(s -> s.createQuery("from Femme f where f.dateNaissance is not null order by f.dateNaissance, f.id",Femme.class).setMaxResults(1).uniqueResult());
    }
    public long nombreEnfantsEntre(int id, LocalDate debut, LocalDate fin) {
        verifierDates(debut,fin);
        return lire(s -> ((Number)s.createNamedQuery("Femme.nombreEnfants").setParameter("femmeId",id).setParameter("debut",debut).setParameter("fin",fin).getSingleResult()).longValue());
    }
    public List<Femme> marieesAuMoinsDeuxFois() {
        return lire(s -> s.createNamedQuery("Femme.auMoinsDeuxMariages",Femme.class).list());
    }
    public long nombreHommesQuatreEpousesEntre(LocalDate debut, LocalDate fin) {
        return new MariageService().hommesQuatreEpousesEntre(debut,fin).size();
    }
}
