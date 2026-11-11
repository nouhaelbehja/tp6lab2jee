package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.beans.*;
import ma.projet.service.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.UUID;

/** Jeux de données synthétiques ; chaque appel ajoute ses propres données. */
public final class Scenarios {
    private Scenarios() {}
    private static LocalDate date(String v) { return LocalDate.parse(v); }
    private static void verifier(boolean ok, String message) {
        if(!ok) throw new AssertionError(message);
        System.out.println("[OK] "+message);
    }
    public static void stock() {
        String suffix=UUID.randomUUID().toString();
        var cs=new CategorieService(); var ps=new ProduitService();
        var co=new CommandeService(); var ls=new LigneCommandeService();
        Categorie c=new Categorie(); c.setCode("INFO-"+suffix); c.setLibelle("Informatique"); c.setDescription("Périphériques"); cs.create(c);
        Commande cmd=new Commande(); cmd.setDate(date("2025-03-14")); cmd.setClient("Client démonstration"); cmd.setStatut("VALIDEE"); co.create(cmd);
        int i=0;
        for(String ref:new String[]{"ES12","ZR85","EE85"}) {
            Produit p=new Produit(); p.setReference(ref+"-"+suffix); p.setCategorie(c);
            p.setPrix(new BigDecimal(new String[]{"120","100","200"}[i]));
            p.setMarque("Atlas"); p.setStockMinimal(5); ps.create(p);
            LigneCommande l=new LigneCommande(); l.setCommande(cmd); l.setProduit(p);
            l.setQuantite(new int[]{7,14,5}[i++]); l.setPrixUnitaire(p.getPrix()); ls.create(l);
        }
        verifier(ps.produitsParCategorie(c.getId()).size()==3,"Trois produits par catégorie");
        verifier(ps.produitsParCommande(cmd.getId()).size()==3,"Trois produits dans la commande");
        var ids=ps.produitsParCommande(cmd.getId()).stream().map(Produit::getId).toList();
        verifier(ps.produitsCommandesEntre(cmd.getDate(),cmd.getDate()).stream().filter(p->ids.contains(p.getId())).count()==3,"Dates de commande inclusives");
        verifier(ps.produitsPrixSuperieur100().stream().filter(p->ids.contains(p.getId())).count()==2,"Requête nommée : prix strictement supérieur à 100");
        verifier(ps.produitsCommandesEntre(date("1900-01-01"),date("1900-01-02")).isEmpty(),"Intervalle sans commande");
        ps.afficherProduitsCommande(cmd.getId());
        c.setDescription("Description mise à jour"); cs.update(c);
        verifier("Description mise à jour".equals(cs.findById(c.getId()).getDescription()),"CRUD : modification et recherche");
        Categorie vide=new Categorie(); vide.setCode("TEMP-"+suffix); cs.create(vide); int id=vide.getId(); cs.delete(vide);
        verifier(cs.findById(id)==null,"CRUD : suppression");
    }
    public static void projets() {
        var es=new EmployeService(); var ps=new ProjetService(); var ts=new TacheService(); var ets=new EmployeTacheService();
        Employe e=new Employe(); e.setNom("EL AMRANI"); e.setPrenom("Nora"); e.setTelephone("0600000000"); e.setEmail("nora@example.test"); e.setSpecialite("Java"); es.create(e);
        Projet p=new Projet(); p.setNom("Gestion de stock"); p.setChef(e); p.setDateDebut(date("2025-01-14")); p.setDateFin(date("2025-06-30")); p.setBudget(new BigDecimal("20000")); p.setStatut("EN_COURS"); ps.create(p);
        int i=0;
        for(String nom:new String[]{"Analyse","Conception","Développement","Recette"}) {
            Tache t=new Tache(); t.setNom(nom); t.setProjet(p); t.setDateDebut(date("2025-02-01").plusMonths(i)); t.setDateFin(t.getDateDebut().plusDays(20));
            t.setPrix(new BigDecimal(i==0?"1000":"1500")); t.setPriorite("HAUTE"); ts.create(t);
            EmployeTache et=new EmployeTache(); et.setEmploye(e); et.setTache(t); et.setDateDebutReelle(t.getDateDebut()); et.setHeuresPassees(16);
            if(i<3) et.setDateFinReelle(t.getDateFin()); ets.create(et); i++;
        }
        verifier(es.projetsGeres(e.getId()).size()==1,"Projet géré par employé");
        verifier(es.tachesRealisees(e.getId()).size()==3,"Tâches terminées de l'employé");
        verifier(ps.tachesPlanifiees(p.getId()).size()==4,"Quatre tâches planifiées");
        verifier(ps.tachesRealisees(p.getId()).size()==3,"Dates réelles et exclusion de la tâche inachevée");
        verifier(ts.prixSuperieur1000().stream().filter(t->t.getProjet().getId().equals(p.getId())).count()==3,"Requête nommée : prix strictement supérieur à 1000");
        verifier(ts.realiseesEntre(date("2025-02-01"),date("2025-04-30")).stream().filter(t->t.getProjet().getId().equals(p.getId())).count()==3,"Tâches réalisées dans l'intervalle");
        ps.afficherTachesRealisees(p.getId());
    }
    public static void etatCivil() {
        var fs=new FemmeService(); var hs=new HommeService(); var ms=new MariageService();
        String suffix=UUID.randomUUID().toString();
        Femme[] femmes=new Femme[10]; Homme[] hommes=new Homme[5];
        for(int i=0;i<10;i++) {
            Femme x=new Femme(); x.setNom("FAMILLE"+i); x.setPrenom("Femme"+i); x.setDateNaissance(date("1960-01-01").plusYears(i));
            x.setCin("F"+i+suffix); x.setVille("Casablanca"); x.setAdresse("Adresse fictive"); x.setTelephone("0600000000"); fs.create(x); femmes[i]=x;
        }
        for(int i=0;i<5;i++) {
            Homme x=new Homme(); x.setNom("HOMME"+i); x.setPrenom("Prénom"+i); x.setDateNaissance(date("1955-01-01").plusYears(i));
            x.setCin("H"+i+suffix); x.setVille("Rabat"); hs.create(x); hommes[i]=x;
        }
        // Quatre unions simultanées ; une ancienne union terminée avec une cinquième femme.
        for(int i=0;i<4;i++) mariage(ms,hommes[0],femmes[i],"2000-01-01",null,i+1);
        mariage(ms,hommes[0],femmes[4],"1990-01-01","1995-01-01",0);
        mariage(ms,hommes[1],femmes[4],"2001-01-01",null,2);
        fs.findAll().forEach(x->System.out.println(x.getId()+" "+x.getNom()+" "+x.getPrenom()));
        Femme agee=fs.plusAgee();
        System.out.println("Femme la plus âgée : "+agee.getNom()+" "+agee.getDateNaissance());
        verifier(!agee.getDateNaissance().isAfter(femmes[0].getDateNaissance()),"Femme la plus âgée");
        verifier(hs.epouses(hommes[0].getId()).size()==5,"Épouses : historique complet");
        verifier(hs.epousesEntre(hommes[0].getId(),date("2005-01-01"),date("2006-01-01")).size()==4,"Épouses sur une période");
        verifier(fs.nombreEnfantsEntre(femmes[0].getId(),date("2000-01-01"),date("2000-01-01"))==1,"Nombre d'enfants, requête native nommée");
        verifier(fs.nombreEnfantsEntre(femmes[9].getId(),date("2000-01-01"),date("2020-01-01"))==0,"Femme sans mariage : zéro enfant");
        verifier(fs.marieesAuMoinsDeuxFois().stream().anyMatch(x->x.getId().equals(femmes[4].getId())),"Femmes mariées au moins deux fois");
        var quatre=ms.hommesQuatreEpousesEntre(date("2005-01-01"),date("2006-01-01"));
        verifier(quatre.stream().anyMatch(x->x.getId().equals(hommes[0].getId())),"Criteria : quatre épouses simultanées");
        verifier(ms.hommesQuatreEpousesEntre(date("1991-01-01"),date("1992-01-01")).stream().noneMatch(x->x.getId().equals(hommes[0].getId())),"Ancienne période sans quatre unions");
        verifier(fs.nombreHommesQuatreEpousesEntre(date("2005-01-01"),date("2006-01-01"))==quatre.size(),"Comptage des hommes");
        quatre.forEach(x->System.out.println("Quatre épouses : "+x.getNom()));
        hs.afficherMariages(hommes[0].getId());
    }
    private static void mariage(MariageService service,Homme h,Femme f,String d,String fin,int enfants) {
        Mariage m=new Mariage(); m.setHomme(h); m.setFemme(f); m.setDateDebut(date(d)); m.setDateFin(fin==null?null:date(fin)); m.setNbrEnfant(enfants); m.setLieu("Casablanca"); service.create(m);
    }
}
