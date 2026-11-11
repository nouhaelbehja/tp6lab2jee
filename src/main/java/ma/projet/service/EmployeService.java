package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class EmployeService extends AbstractService<Employe> {
    public EmployeService() { super(Employe.class); }
    public List<Tache> tachesRealisees(int id) {
        return lire(s -> s.createQuery("select distinct e.tache from EmployeTache e where e.employe.id=:id and e.dateFinReelle is not null",Tache.class).setParameter("id",id).list());
    }
    public List<Projet> projetsGeres(int id) {
        return lire(s -> s.createQuery("from Projet p where p.chef.id=:id order by p.id",Projet.class).setParameter("id",id).list());
    }
}
