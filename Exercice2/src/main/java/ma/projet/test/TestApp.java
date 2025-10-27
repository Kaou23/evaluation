package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.time.LocalDate;

public class TestApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(HibernateUtil.class);

        EmployeService es = (EmployeService) ctx.getBean("employeService");
        ProjetService ps = (ProjetService) ctx.getBean("projetService");
        TacheService ts = (TacheService) ctx.getBean("tacheService");
        EmployeTacheService ets = (EmployeTacheService) ctx.getBean("employeTacheService");

        try {
            runTests(es, ps, ts, ets);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    private static void runTests(EmployeService es, ProjetService ps,
                                 TacheService ts, EmployeTacheService ets) {
        // Create employees
        Employe e1 = new Employe("Ali", "Fakir", "0661234567");
        Employe e2 = new Employe("Hassan", "Alami", "0662345678");
        es.create(e1);
        es.create(e2);

        // Create project
        Projet p1 = new Projet("Gestion de stock", LocalDate.of(2013, 1, 14), e1);
        ps.create(p1);

        // Create tasks
        Tache t1 = new Tache("Analyse", 1200,
                LocalDate.of(2013, 2, 10),
                LocalDate.of(2013, 2, 20), p1);
        Tache t2 = new Tache("Conception", 800,
                LocalDate.of(2013, 3, 10),
                LocalDate.of(2013, 3, 15), p1);
        Tache t3 = new Tache("Développement", 1500,
                LocalDate.of(2013, 4, 10),
                LocalDate.of(2013, 4, 25), p1);

        ts.create(t1);
        ts.create(t2);
        ts.create(t3);

        // Assign tasks to employees
        ets.create(new EmployeTache(e1, t1,
                LocalDate.of(2013, 2, 5),
                LocalDate.of(2013, 2, 20)));
        ets.create(new EmployeTache(e1, t3,
                LocalDate.of(2013, 4, 5),
                LocalDate.of(2013, 4, 25)));
        ets.create(new EmployeTache(e2, t2,
                LocalDate.of(2013, 3, 10),
                LocalDate.of(2013, 3, 15)));

        // Tests
        System.out.println("1. Liste des tâches planifiées:");
        ps.afficherTachesProjet(p1.getId());

        System.out.println("\n2. Liste des tâches réalisées avec dates réelles:");
        ps.afficherTachesRealisees(p1.getId());

        System.out.println("\n3. Tâches réalisées par l'employé " + e1.getNom() + ":");
        es.afficherTachesRealisees(e1.getId());

        System.out.println("\n4. Projets gérés par l'employé " + e1.getNom() + ":");
        es.afficherProjetsGeres(e1.getId());

        System.out.println("\n5. Tâches dont le prix > 1000 DH:");
        ts.afficherTachesSup1000();

        System.out.println("\n6. Tâches réalisées entre deux dates:");
        ts.afficherTachesEntreDates(
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 4, 30)
        );
    }
}