package ma.projet.util;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ma.projet.classes.*;
import ma.projet.beans.*;

public final class HibernateUtil {
    private HibernateUtil() {}
    private static class Holder {
        private static final SessionFactory INSTANCE = construire();
    }
    public static SessionFactory getSessionFactory() { return Holder.INSTANCE; }
    private static SessionFactory construire() {
        String fichier = System.getProperty("tp.config", "application.properties");
        Properties props = new Properties();
        try (InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream(fichier)) {
            if (in == null) throw new IllegalStateException("Configuration introuvable : " + fichier);
            props.load(in);
        } catch (IOException e) { throw new IllegalStateException(e); }
        remplacer(props, "DB_URL", "hibernate.connection.url");
        remplacer(props, "DB_USER", "hibernate.connection.username");
        remplacer(props, "DB_PASSWORD", "hibernate.connection.password");
        Configuration cfg = new Configuration().addProperties(props);
        for (Class<?> type : new Class<?>[]{Categorie.class, Produit.class, Commande.class,
                LigneCommande.class, Employe.class, Projet.class, Tache.class, EmployeTache.class,
                Personne.class, Homme.class, Femme.class, Mariage.class}) cfg.addAnnotatedClass(type);
        return cfg.buildSessionFactory();
    }
    private static void remplacer(Properties p, String env, String cle) {
        if (System.getenv(env) != null) p.setProperty(cle, System.getenv(env));
    }
    public static void shutdown() { getSessionFactory().close(); }
}
