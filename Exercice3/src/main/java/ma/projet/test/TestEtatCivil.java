package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestEtatCivil {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(HibernateUtil.class);

        HommeService hommeService = (HommeService) ctx.getBean("hommeService");
        FemmeService femmeService = (FemmeService) ctx.getBean("femmeService");
        MariageService mariageService = (MariageService) ctx.getBean("mariageService");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("=== Création des données ===\n");

            // Créer 10 femmes
            Femme f1 = new Femme("RAMI", "SALIMA", "0661111111", "Casablanca", sdf.parse("15/03/1970"));
            Femme f2 = new Femme("ALI", "AMAL", "0662222222", "Rabat", sdf.parse("20/05/1975"));
            Femme f3 = new Femme("ALAOUI", "WAFA", "0663333333", "Fes", sdf.parse("10/08/1980"));
            Femme f4 = new Femme("ALAMI", "KARIMA", "0664444444", "Marrakech", sdf.parse("25/12/1968"));
            Femme f5 = new Femme("BENANI", "LAILA", "0665555555", "Tanger", sdf.parse("05/01/1985"));
            Femme f6 = new Femme("FASSI", "NAJAT", "0666666666", "Meknes", sdf.parse("30/07/1972"));
            Femme f7 = new Femme("IDRISSI", "SAMIRA", "0667777777", "Agadir", sdf.parse("12/11/1978"));
            Femme f8 = new Femme("TAZI", "FATIMA", "0668888888", "Oujda", sdf.parse("18/04/1983"));
            Femme f9 = new Femme("BENJELLOUN", "HOUDA", "0669999999", "Kenitra", sdf.parse("22/09/1976"));
            Femme f10 = new Femme("CHRAIBI", "MALIKA", "0660000000", "Safi", sdf.parse("08/06/1965"));

            femmeService.create(f1);
            femmeService.create(f2);
            femmeService.create(f3);
            femmeService.create(f4);
            femmeService.create(f5);
            femmeService.create(f6);
            femmeService.create(f7);
            femmeService.create(f8);
            femmeService.create(f9);
            femmeService.create(f10);

            // Créer 5 hommes
            Homme h1 = new Homme("SAFI", "SAID", "0671111111", "Casablanca", sdf.parse("10/01/1965"));
            Homme h2 = new Homme("AMRANI", "MOHAMED", "0672222222", "Rabat", sdf.parse("15/05/1970"));
            Homme h3 = new Homme("BENNANI", "KARIM", "0673333333", "Fes", sdf.parse("20/08/1968"));
            Homme h4 = new Homme("IDRISSI", "YOUSSEF", "0674444444", "Marrakech", sdf.parse("25/03/1972"));
            Homme h5 = new Homme("ALAMI", "HASSAN", "0675555555", "Tanger", sdf.parse("30/11/1975"));

            hommeService.create(h1);
            hommeService.create(h2);
            hommeService.create(h3);
            hommeService.create(h4);
            hommeService.create(h5);

            // Créer des mariages
            // Mariages de h1 (SAFI SAID)
            Mariage m1 = new Mariage(sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0, h1, f4);
            Mariage m2 = new Mariage(sdf.parse("03/09/1990"), h1, f1);
            m2.setNbrEnfant(4);
            Mariage m3 = new Mariage(sdf.parse("03/09/1995"), h1, f2);
            m3.setNbrEnfant(2);
            Mariage m4 = new Mariage(sdf.parse("04/11/2000"), h1, f3);
            m4.setNbrEnfant(3);

            mariageService.create(m1);
            mariageService.create(m2);
            mariageService.create(m3);
            mariageService.create(m4);

            // Mariages de h2
            Mariage m5 = new Mariage(sdf.parse("15/06/1992"), h2, f5);
            m5.setNbrEnfant(3);
            Mariage m6 = new Mariage(sdf.parse("20/03/1998"), h2, f6);
            m6.setNbrEnfant(2);

            mariageService.create(m5);
            mariageService.create(m6);

            // Mariages de h3
            Mariage m7 = new Mariage(sdf.parse("10/01/1990"), sdf.parse("15/05/1995"), 1, h3, f7);
            Mariage m8 = new Mariage(sdf.parse("20/06/1996"), h3, f8);
            m8.setNbrEnfant(2);

            mariageService.create(m7);
            mariageService.create(m8);

            // Mariage multiple pour f7 (mariée 2 fois)
            Mariage m9 = new Mariage(sdf.parse("01/01/2000"), h4, f7);
            m9.setNbrEnfant(1);
            mariageService.create(m9);

            // Mariage de h5
            Mariage m10 = new Mariage(sdf.parse("10/07/1998"), h5, f9);
            m10.setNbrEnfant(2);
            mariageService.create(m10);

            System.out.println("Données créées avec succès!\n");
            System.out.println("=".repeat(80));

            // Tests

            // 1. Afficher la liste des femmes
            System.out.println("\n1. Liste des femmes:");
            System.out.println("-".repeat(80));
            femmeService.afficherFemmes();

            // 2. Afficher la femme la plus âgée
            System.out.println("\n" + "-".repeat(80));
            System.out.println("2. Femme la plus âgée:");
            System.out.println("-".repeat(80));
            Femme oldest = femmeService.findOldestFemme();
            if (oldest != null) {
                System.out.println("La femme la plus âgée est: " + oldest.getPrenom() + " " +
                        oldest.getNom() + " (Née le: " + sdf.format(oldest.getDateNaissance()) + ")");
            }

            // 3. Afficher les épouses d'un homme entre deux dates
            System.out.println("\n" + "-".repeat(80));
            System.out.println("3. Épouses d'un homme entre deux dates:");
            System.out.println("-".repeat(80));
            hommeService.afficherEpousesEntreDates(h1.getId(),
                    sdf.parse("01/01/1990"),
                    sdf.parse("31/12/2000"));

            // 4. Afficher le nombre d'enfants d'une femme entre deux dates
            System.out.println("\n" + "-".repeat(80));
            System.out.println("4. Nombre d'enfants d'une femme entre deux dates:");
            System.out.println("-".repeat(80));
            int nbrEnfants = femmeService.countEnfantsBetweenDates(f1.getId(),
                    sdf.parse("01/01/1990"),
                    sdf.parse("31/12/2000"));
            System.out.println("Nombre d'enfants de " + f1.getPrenom() + " " + f1.getNom() +
                    " entre 01/01/1990 et 31/12/2000: " + nbrEnfants);

            // 5. Afficher les femmes mariées deux fois ou plus
            System.out.println("\n" + "-".repeat(80));
            System.out.println("5. Femmes mariées deux fois ou plus:");
            System.out.println("-".repeat(80));
            List<Femme> femmesMultiMariees = femmeService.findFemmesMarriedTwiceOrMore();
            if (femmesMultiMariees.isEmpty()) {
                System.out.println("Aucune femme mariée deux fois ou plus.");
            } else {
                for (Femme f : femmesMultiMariees) {
                    System.out.println("  - " + f.getPrenom() + " " + f.getNom() +
                            " (Nombre de mariages: " + f.getMariages().size() + ")");
                }
            }

            // 6. Afficher les hommes mariés à quatre femmes entre deux dates
            System.out.println("\n" + "-".repeat(80));
            System.out.println("6. Hommes mariés à quatre femmes entre deux dates:");
            System.out.println("-".repeat(80));
            hommeService.afficherHommesMarieQuatreFemmes(
                    sdf.parse("01/01/1989"),
                    sdf.parse("31/12/2001"));

            // 7. Afficher les mariages d'un homme avec tous les détails
            System.out.println("\n" + "-".repeat(80));
            System.out.println("7. Mariages d'un homme avec détails:");
            System.out.println("-".repeat(80));
            hommeService.afficherMariagesHomme(h1.getId());

            System.out.println("\n" + "=".repeat(80));

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }
}