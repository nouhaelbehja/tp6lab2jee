package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class ProjetService extends AbstractService<Projet> {
    public ProjetService() { super(Projet.class); }
    public List<Tache> tachesPlanifiees(int id) {
        return lire(s -> s.createQuery("from Tache t where t.projet.id=:id order by t.dateDebut",Tache.class).setParameter("id",id).list());
    }
    public List<EmployeTache> tachesRealisees(int id) {
        return lire(s -> s.createQuery("from EmployeTache e where e.tache.projet.id=:id and e.dateFinReelle is not null order by e.tache.id",EmployeTache.class).setParameter("id",id).list());
    }
    public void afficherTachesRealisees(int id) {
        Projet p=findById(id);
        if(p==null) throw new IllegalArgumentException("Projet introuvable : "+id);
        System.out.printf("Projet : %d  Nom : %s  Date début : %s%nListe des tâches :%nNum  Nom  Date Début Réelle  Date Fin Réelle%n",id,p.getNom(),p.getDateDebut());
        tachesRealisees(id).forEach(e -> System.out.printf("%d  %s  %s  %s%n",e.getTache().getId(),e.getTache().getNom(),e.getDateDebutReelle(),e.getDateFinReelle()));
    }
}
