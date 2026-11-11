package ma.projet;
import org.junit.jupiter.api.*;
import ma.projet.test.Scenarios;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TpTest {
    @BeforeAll static void configurer() { System.setProperty("tp.config",System.getProperty("tp.test.config","application-test.properties")); }
    @AfterAll static void fermer() { HibernateUtil.shutdown(); }
    @Test void stock() { Scenarios.stock(); }
    @Test void projets() { Scenarios.projets(); }
    @Test void etatCivil() { Scenarios.etatCivil(); }
    @Test void intervalleInverseRefuse() {
        assertThrows(IllegalArgumentException.class,()->new ProduitService().produitsCommandesEntre(LocalDate.of(2025,2,1),LocalDate.of(2025,1,1)));
    }
}
