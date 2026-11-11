package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.beans.*;
import java.time.LocalDate;
import java.util.List;
public class EmployeTacheService extends AbstractService<EmployeTache> {
    public EmployeTacheService() { super(EmployeTache.class); }
    @Override public EmployeTache create(EmployeTache e) {
        if(e.getDateFinReelle()!=null) verifierDates(e.getDateDebutReelle(),e.getDateFinReelle());
        if(e.getHeuresPassees()<0) throw new IllegalArgumentException("Heures négatives");
        return super.create(e);
    }
}
