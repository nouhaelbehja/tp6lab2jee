package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class HommeService extends AbstractService<Homme> {
    public HommeService() { super(Homme.class); }
    public List<Femme> epousesEntre(int id, LocalDate debut, LocalDate fin) {
        verifierDates(debut,fin);
        return lire(s -> s.createQuery("select distinct m.femme from Mariage m where m.homme.id=:id and m.dateDebut <= :f and (m.dateFin is null or m.dateFin >= :d)",Femme.class).setParameter("id",id).setParameter("d",debut).setParameter("f",fin).list());
    }
    public List<Femme> epouses(int id) {
        return lire(s -> s.createQuery("select distinct m.femme from Mariage m where m.homme.id=:id",Femme.class).setParameter("id",id).list());
    }
    public void afficherMariages(int id) { new MariageService().afficherMariages(id); }
}
