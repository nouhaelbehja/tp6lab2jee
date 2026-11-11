package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class MariageService extends AbstractService<Mariage> {
    public MariageService() { super(Mariage.class); }
    @Override public Mariage create(Mariage m) {
        if(m.getDateDebut()==null || m.getNbrEnfant()<0) throw new IllegalArgumentException("Mariage invalide");
        if(m.getDateFin()!=null) verifierDates(m.getDateDebut(),m.getDateFin());
        return super.create(m);
    }
    public List<Homme> hommesQuatreEpousesEntre(LocalDate debut, LocalDate fin) {
        verifierDates(debut,fin);
        return lire(s -> {
            var cb=s.getCriteriaBuilder();
            var q=cb.createQuery(Homme.class);
            var h=q.from(Homme.class);
            var count=q.subquery(Long.class);
            var m=count.from(Mariage.class);
            count.select(cb.countDistinct(m.get("femme").get("id")));
            count.where(cb.equal(m.get("homme"),h),
                cb.lessThanOrEqualTo(m.<LocalDate>get("dateDebut"),debut),
                cb.or(cb.isNull(m.get("dateFin")),cb.greaterThanOrEqualTo(m.<LocalDate>get("dateFin"),fin)));
            q.select(h).where(cb.equal(count,4L)).orderBy(cb.asc(h.get("id")));
            return s.createQuery(q).list();
        });
    }
    public List<Mariage> parHomme(int id) {
        return lire(s -> s.createQuery("from Mariage m where m.homme.id=:id order by m.dateDebut",Mariage.class).setParameter("id",id).list());
    }
    public void afficherMariages(int id) {
        Homme h=new HommeService().findById(id);
        if(h==null) throw new IllegalArgumentException("Homme introuvable : "+id);
        System.out.println("Nom : "+h.getNom()+" "+h.getPrenom());
        List<Mariage> mariages=parHomme(id);
        for(boolean enCours : new boolean[]{true,false}) {
            System.out.println(enCours ? "Mariages En Cours :" : "Mariages échoués :");
            int n=1;
            for(Mariage m:mariages) if((m.getDateFin()==null)==enCours)
                System.out.printf("%d. Femme : %s %s  Date Début : %s  %s Nbr Enfants : %d%n",n++,m.getFemme().getNom(),m.getFemme().getPrenom(),m.getDateDebut(),enCours ? "" : "Date Fin : "+m.getDateFin(),m.getNbrEnfant());
        }
    }
}
