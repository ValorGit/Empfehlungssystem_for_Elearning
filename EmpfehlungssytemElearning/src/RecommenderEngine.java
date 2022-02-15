
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.neo4j.driver.Config.TrustStrategy.trustAllCertificates;

public class RecommenderEngine implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(RecommenderEngine.class.getName());
    private final Driver driver;
    private final String url = "neo4j+s://ad5badef.databases.neo4j.io";
    private final String userCurrent = "neo4j";
    private final String passwordCurrent = "JOgvjfXTDzlBxHK42FvqroNlC5A6DbkGRCP6X8BJtJs";

    // Festgelegt vorher: public RecommenderEngine(String uri, String user, String
    // password, Config config)
    public RecommenderEngine() {
        // The driver is a long living object and should be opened during the start of
        // your application
        driver = GraphDatabase.driver(url, AuthTokens.basic(userCurrent, passwordCurrent), Config.defaultConfig());
    }

    @Override
    public void close() throws Exception {
        // The driver object should be closed before the application ends.
        driver.close();
    }

    // public void createFriendship(final String Lerner1Name, final String
    // abgeschlossenerKurs) {
    // // To learn more about the Cypher syntax, see
    // https://neo4j.com/docs/cypher-manual/current/
    // // The Reference Card is also a good resource for keywords
    // https://neo4j.com/docs/cypher-refcard/current/
    // String createFriendshipQuery = "CREATE (l1:Lerner { name: $Lerner1_name })\n"
    // +
    // "CREATE (l2:Lerner { name: $Lerner2_name })\n" +
    // "CREATE (l1)-[:KNOWS]->(l2)\n" +
    // "RETURN l1, l2";

    // Map<String, Object> params = new HashMap<>();
    // params.put("Lerner1_name", Lerner1Name);
    // params.put("Lerner2_name", abgeschlossenerKurs);

    // try (Session session = driver.session()) {
    // // Write transactions allow the driver to handle retries and transient errors
    // Record record = session.writeTransaction(tx -> {
    // Result result = tx.run(createFriendshipQuery, params);
    // return result.single();
    // });
    // System.out.println(String.format("Created friendship between: %s, %s",
    // record.get("l1").get("name").asString(),
    // record.get("l2").get("name").asString()));
    // // You should capture any errors along with the query and data for
    // traceability
    // } catch (Neo4jException ex) {
    // LOGGER.log(Level.SEVERE, createFriendshipQuery + " raised an exception", ex);
    // throw ex;
    // }
    // }

    public void kursAbgeschlossen(final String LernerName, final String abgeschlossenerKurs) {
        // To learn more about the Cypher syntax, see
        // https://neo4j.com/docs/cypher-manual/current/
        // The Reference Card is also a good resource for keywords
        // https://neo4j.com/docs/cypher-refcard/current/
        String kursAbgeschlossenQuery = "match (l:Lerner), (k:Kurs)\n" +
                "where l.name = $Lerner_name And k.name = $Kurs_name\n" +
                "merge (l)-[r:HAT_BESUCHT]->(k)\n" +
                "return r, l, k";

        Map<String, Object> params = new HashMap<>();
        params.put("Lerner_name", LernerName);
        params.put("Kurs_name", abgeschlossenerKurs);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(kursAbgeschlossenQuery, params);
                return result.single();
            });
            System.out.println(String.format("Lerner %s hat %s abgeschlossen",
                    record.get("l").get("name").asString(),
                    record.get("k").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, kursAbgeschlossenQuery + " raised an exception", ex);
            throw ex;
        }
    }

    public void neuerUser(String name, String email, String position, List<String> skills){
        String neuerUserQuery = "merge (l:Lerner {email: $email})\n" +
                "On Create set l.name = $name \n" +
                "with l \n" +
                "match (p:Position {name: $position}) \n" +
                "merge (l)-[r:HAT_POSITION]->(p)\n" +
                "return l, p";

         Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("position", position);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(neuerUserQuery, params);
                return result.single();
            });
            System.out.println(String.format("User %s wurde erstellt",
                    record.get("l").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, neuerUserQuery + " raised an exception", ex);
            throw ex;
        }
        this.userSkillZuweisung(name, skills);

    }

    public void userSkillZuweisung(String user, List<String> skills){
      this.userSkillLoeschen(user);
        
        String userSkillQuery = "match (l:Lerner {name: $user})\n" +
                " Unwind $skills AS gewaehlte match (s:Skill {name: gewaehlte}) merge (l)-[:LERNT_SKILL]->(s) return l";

         Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("skills", skills);

        try (Session session = driver.session()) {
 
            // Write transactions allow the driver to handle retries and transient errors
            List<Record> record = session.writeTransaction(tx -> {
                Result result = tx.run(userSkillQuery, params);
                return result.list();
            });
            
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, userSkillQuery + " raised an exception", ex);
            throw ex;
        }

    }

     public void userSkillLoeschen(String user){
        String userSkillLoeschenQuery = "match (l:Lerner {name: $user})\n" +
                " match (s:Skill) match (l)-[r:LERNT_SKILL]->(s) delete r";
        
       
         Map<String, Object> params = new HashMap<>();
        params.put("user", user);

        try (Session session = driver.session()) {
 
            // Write transactions allow the driver to handle retries and transient errors
            List<Record> record = session.writeTransaction(tx -> {
                Result result = tx.run(userSkillLoeschenQuery, params);
                return result.list();
            });
            
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, userSkillLoeschenQuery + " raised an exception", ex);
            throw ex;
        }

    }

    public void hatThema(final String kurs, final String unterthema) {
        // To learn more about the Cypher syntax, see
        // https://neo4j.com/docs/cypher-manual/current/
        // The Reference Card is also a good resource for keywords
        // https://neo4j.com/docs/cypher-refcard/current/

        String hatThemaQuery = "match (k:Kurs {name: $kurs})\n" +
                "merge (t:Subthema {name: $unterthema})\n" +
                "merge (k)-[r:HAT_THEMA]->(t)\n" +
                "return t, k";

        Map<String, Object> params = new HashMap<>();
        params.put("unterthema", unterthema);
        params.put("kurs", kurs);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(hatThemaQuery, params);
                return result.single();
            });
            System.out.println(String.format("Kurs %s wurde %s zugewiesen",
                    record.get("k").get("name").asString(),
                    record.get("t").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, hatThemaQuery + " raised an exception", ex);
            throw ex;
        }
    }

    // Weist ein Subthema ein Übergeordnetes Thema zu. Falls noch nicht vorhanden,
    // wird ein neues Thema angelegt
    public void istUnterthema(final String unterthema, final String thema) {
        // To learn more about the Cypher syntax, see
        // https://neo4j.com/docs/cypher-manual/current/
        // The Reference Card is also a good resource for keywords
        // https://neo4j.com/docs/cypher-refcard/current/

        String istUnterthemaQuery = "match (st:Subthema {name: $unterthema})\n" +
                "merge (t:Thema {name: $thema})\n" +
                "merge (st)-[r:IST_UNTERTHEMA]->(t)\n" +
                "return t, st";

        Map<String, Object> params = new HashMap<>();
        params.put("unterthema", unterthema);
        params.put("thema", thema);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(istUnterthemaQuery, params);
                return result.single();
            });
            System.out.println(String.format("Thema %s ist nun Unterthema von %s",
                    record.get("st").get("name").asString(),
                    record.get("t").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, istUnterthemaQuery + " raised an exception", ex);
            throw ex;
        }
    }

    public void erstelleKurs(final String KursName, final String beschreibung, final int niveau, final String link,
            final String medium, final String typ) {
        String erstelleKursQuery = "MERGE (k1:Kurs { name: $KursName }) \n" +
        "set k1.beschreibung = $beschreibung, k1.niveau = $niveau , k1.typ = $typ \n" +
                "RETURN k1";

        Map<String, Object> params = new HashMap<>();
        params.put("KursName", KursName);
        params.put("beschreibung", beschreibung);
        params.put("niveau", niveau);
        params.put("typ", typ);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(erstelleKursQuery, params);
                return result.single();
            });
            System.out.println(String.format("Kurs: %s erfolgreich erstellt",
                    record.get("k1").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, erstelleKursQuery + " raised an exception", ex);
            throw ex;
        }

    }

    public void findeLerner(final String LernerName) {
        String readLernerByNameQuery = "MATCH (l:Lerner)\n" +
                "WHERE l.name = $Lerner_name\n" +
                "RETURN l.name AS name";

        Map<String, Object> params = Collections.singletonMap("Lerner_name", LernerName);

        try (Session session = driver.session()) {
            Record record = session.readTransaction(tx -> {
                Result result = tx.run(readLernerByNameQuery, params);
                return result.single();
            });
            System.out.println(String.format("Found Lerner: %s", record.get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readLernerByNameQuery + " raised an exception", ex);
            throw ex;
        }
    }

    public void erstelleSkill(final String skill) {
        String erstelleSkillQuery = "merge (s:Skill {name: $skillname}) RETURN s";

        Map<String, Object> params = Collections.singletonMap("skillname", skill);

        try (Session session = driver.session()) {
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(erstelleSkillQuery, params);
                return result.single();
            });

            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, erstelleSkillQuery + " raised an exception", ex);
            throw ex;
        }
    }

    public void erstelleSkillZuweisung(String kurs, String skill) {

        String skillZuweisungQuery = "match (k:Kurs {name: $kurs})\n" +
                "match (s:Skill {name: $skill})\n" +
                "merge (k)-[r:FOERDERT]->(s) return s";

        Map<String, Object> params = new HashMap<>();
        params.put("skill", skill);
        params.put("kurs", kurs);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(skillZuweisungQuery, params);
                return result.single();
            });
            System.out.println(String.format("Kurs fördert %s",
                    record.get("s").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, skillZuweisungQuery + " raised an exception", ex);
            throw ex;
        }

    }

    public void erstelleNotwendigeSkillZuweisung(String position, String skill) {

        String skillZuweisungQuery = "match (p:Position {name: $position})\n" +
                "match (s:Skill {name: $skill})\n" +
                "merge (p)-[r:ERFORDERT_SKILL]->(s) return p,s";

        Map<String, Object> params = new HashMap<>();
        params.put("skill", skill);
        params.put("position", position);

        try (Session session = driver.session()) {
            // Write transactions allow the driver to handle retries and transient errors
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(skillZuweisungQuery, params);
                return result.single();
            });
            System.out.println(String.format("Position %s erfordert %s",
                    record.get("p").get("name").asString(),
                    record.get("s").get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, skillZuweisungQuery + " raised an exception", ex);
            throw ex;
        }

    }

    public void erstelleKursAusCSV(String csvUrl) {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader csvReader = new BufferedReader(new FileReader(csvUrl))) {
            String row;
            while ((row = csvReader.readLine()) != null) {
                String bereinigt = row.replace("\"", "");
                String[] data = bereinigt.split(";");
                records.add(Arrays.asList(data));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Datei nicht gefunden");
        }

        for (int i = 0; i < records.size(); i++) {

            // try (RecommenderEngine app = new RecommenderEngine()) {
                String kurs = records.get(i).get(0);
                String skills = records.get(i).get(1);
                String thema = records.get(i).get(2);
                String beschreibung = records.get(i).get(3);
                int niveau = Integer.parseInt(records.get(i).get(4));
                String typ = "";
                if(records.get(i).size() >= 6){
                typ = records.get(i).get(5);
                }
                this.erstelleKurs(kurs, beschreibung, niveau, "", "", typ);

                this.hatThema(kurs, thema);
                StringTokenizer stk = new StringTokenizer(skills, ",");

                while (stk.hasMoreTokens()) {

                    this.erstelleSkillZuweisung(kurs, stk.nextToken());
                }

            // } catch (Exception e) {
            //     System.out.println("Verbindung nicht möglich");
            // }
        }

    }

    public List<Record> empfehleKurs(String lerner) {
        String readLernerByNameQuery = "match (l:Lerner {name: $lerner}) match (k:Kurs) match (p:Position) match (l)-[:HAT_POSITION]->(p)<-[r:RELEVANT_FUER]-(k) match (l)-[r2:HAT_KOMPETENZ]->(ut:Subthema)<-[:HAT_THEMA]-(k) WHERE r2.kompetenzniveau>= k.niveau AND Not (l)-[:HAT_BESUCHT]->(k) return k,r  ORDER BY r.gewichtung DESC limit 5";

        List<Record> record;
        Map<String, Object> params = Collections.singletonMap("lerner", lerner);

        try (Session session = driver.session()) {
            record = session.readTransaction(tx -> {
                Result result = tx.run(readLernerByNameQuery, params);
                return result.list();
            });
            for (int i = 0; i < record.size(); i++) {

                System.out.println(String.format("Kurs: %s", record.get(i).values().get(0).get("name").asString()));
            }
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readLernerByNameQuery + " raised an exception", ex);
            throw ex;
        }
        return record;
    }

    public void berechneRelevanzPosition(String position) {

        String berechneRelevanzQuery = "match (p:Position {name: $position}) match (k:Kurs) match (s:Skill) match (ut:Subthema)  match (p)-[r1:ERFORDERT_THEMA]->(ut)<-[:HAT_THEMA]-(k) merge (k)-[:RELEVANT_FUER {gewichtung: r1.gewichtung}]->(p)";

        Map<String, Object> params = Collections.singletonMap("position", position);

        try (Session session = driver.session()) {
            Record record = session.writeTransaction(tx -> {
                Result result = tx.run(berechneRelevanzQuery, params);
                return result.single();
            });

            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, berechneRelevanzQuery + " raised an exception", ex);
            throw ex;
        }

    }

    public String gibaktuellePosition(final String lernerName) {

        String aktuellePosition = "";
        String readLernerByNameQuery = "MATCH (l:Lerner {name: $lernerName}) match (p:Position) match (l)-[:HAT_POSITION]->(p) return p.name as name";

        Map<String, Object> params = Collections.singletonMap("lernerName", lernerName);

        try (Session session = driver.session()) {
            Record record = session.readTransaction(tx -> {
                Result result = tx.run(readLernerByNameQuery, params);
                return result.single();
            });
            aktuellePosition = record.get("name").asString();

            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readLernerByNameQuery + " raised an exception", ex);
            throw ex;
        }
        return aktuellePosition;
    }

    public List<String> gibAlleSkills(){
        String readSkillsQuery = "match (s: Skill) with s ORDER by s.name  return collect(s.name)  AS skills";
        List<String> skills = new ArrayList<String>();

         try (Session session = driver.session()) {
            Record record = session.readTransaction(tx -> {
                Result result = tx.run(readSkillsQuery);
                return result.single();
            });
            for(int i = 0; i < record.get("skills").size(); i++){
                skills.add(record.get("skills").get(i).asString());
                System.out.println(skills.get(i));
            }
            // System.out.println(String.format("Found Lerner: %s", record.get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readSkillsQuery + " raised an exception", ex);
            throw ex;
        }
        return skills;
    }

        public List<String> gibLernerSkills(String lerner){
        String readSkillsQuery = "match (l:Lerner {name : $lerner}) match (s: Skill) match (l)-[:LERNT_SKILL]->(s) with s ORDER by s.name  return collect(s.name)  AS skills";
        List<String> skills = new ArrayList<String>();

         Map<String, Object> params = Collections.singletonMap("lerner", lerner);

         try (Session session = driver.session()) {
            Record record = session.readTransaction(tx -> {
                Result result = tx.run(readSkillsQuery,params);
                return result.single();
            });
            for(int i = 0; i < record.get("skills").size(); i++){
                skills.add(record.get("skills").get(i).asString());
                System.out.println(skills.get(i));
            }
            // System.out.println(String.format("Found Lerner: %s", record.get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readSkillsQuery + " raised an exception", ex);
            throw ex;
        }
        return skills;
    }

    public List<String> gibAllePositionen(){
        String readPositionenQuery = "match (p: Position) with p ORDER by p.name  return collect(p.name)  AS positionen";
        List<String> positionen = new ArrayList<String>();

         try (Session session = driver.session()) {
            Record record = session.readTransaction(tx -> {
                Result result = tx.run(readPositionenQuery);
                return result.single();
            });
            for(int i = 0; i < record.get("positionen").size(); i++){
                positionen.add(record.get("positionen").get(i).asString());
                System.out.println(positionen.get(i));
            }
            // System.out.println(String.format("Found Lerner: %s", record.get("name").asString()));
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, readPositionenQuery + " raised an exception", ex);
            throw ex;
        }
        return positionen;
    }

    // public static void main(String... args) throws Exception {
    //     // // Aura queries use an encrypted connection using the "neo4j+s" protocol
    //     // String uri = "neo4j+s://ad5badef.databases.neo4j.io";

    //     // String user = "neo4j";
    //     // String password = "JOgvjfXTDzlBxHK42FvqroNlC5A6DbkGRCP6X8BJtJs";

    //     /*
    //      * match (l:Lerner {name:"Alexander Dünne"}) match (k:Kurs) match (a:Abteilung)
    //      * match (ut:Subthema) where
    //      * (l)-[:ARBEITET_IN]->(a)-[:`BENÖTIGT`]->(ut)<-[:HAT_THEMA]-(k) AND Not
    //      * (l)-[:HAT_BESUCHT]->(k) return k
    //      */

    //     try (RecommenderEngine app = new RecommenderEngine()) {
    //         // app.createFriendship("Alice", "David");
    //         // app.findeLerner("Alexander Dünne");
    //         // System.out.println(app.empfehleKurs());
    //         // app.kursAbgeschlossen("Alexander Dünne", "Steigern Sie die Teammotivation mit
    //         // OKR");
    //         // app.hatThema("E-Business Grundlagen", "Digitale Plattformen");
    //         // app.hatThema("New Growth Playbook", "Digitale Plattformen");
    //         // app.istUnterthema("Informationssicherheit", "Arbeitssicherheit &
    //         // Regulatorisches");

    //         // String verketteterString
    //         // ="Lernbereitschaft,Selbstorganisation,Zeitmanagement,Priorisierung,Zielmanagement,Eigene
    //         // Ziele,Teamziele,Strategische Ausrichtung,Selbstreflektion,Kritisches
    //         // Denken,Datenanalyse,Research,Informationsaustausch,Empathie,Kreatives
    //         // Denken,Unternehmerisches Denken,Neue Arbeitsweisen,Agile Methoden,Logisches
    //         // Denken,Analytisches
    //         // Denken,Entscheidungsfähigkeit,Flexibilität,Projektmanagement,(Klöckner-)Strategie,Wissen
    //         // über Klöckner,Wertorientierung,Kundenorientierung,Treiber des Wandels,Aktives
    //         // Zuhören,Networking-Fähigkeit,Kooperation /
    //         // Kollaboration,Teamarbeit,Stakeholder-Management,Verhandlung,Sprachkompetenzen,Konfliktfähigkeit,Befähigung,Coaching,Delegation,Gespräche
    //         // führen,Wertschätzung,Feedback,Führung,Umgang mit digitalen Tools,Digitale
    //         // Kollaboration,IT Security (kritischer Umgang mit Technologie),Digitales
    //         // Marketing,Digitaler Vertrieb,Digitale Transformation,Digitalisierung &
    //         // Ethik,Digitale Kommunikation";

    //         // String kursSkill = "Werkstudent IT,Umgang mit digitalen Tools,Digitale
    //         // Kollaboration,IT Security (kritischer Umgang mit Technologie),Digitalisierung
    //         // & Ethik,Logisches Denken,Analytisches Denken,Digitale Transformation";

    //         // StringTokenizer stk = new StringTokenizer(kursSkill,",");

    //         // String kurs = stk.nextToken();

    //         // while(stk.hasMoreTokens()){
    //         // // System.out.println("Kurs" + kurs + "wurde Skill" + stk.nextToken() +
    //         // "zugewiesen");
    //         // app.erstelleNotwendigeSkillZuweisung(kurs, stk.nextToken());
    //         // }
    //         // File csvFile = new File("D:/Alex/Studium/Bachelorarbeit/kurse.csv");
    //         // if (csvFile.isFile()) {
    //         //     System.out.println("Datei existiert");
    //         //     // create BufferedReader and read data from csv
    //         // }
          
    //         // app.erstelleKursAusCSV("D:/Alex/Studium/Bachelorarbeit/kurse.csv");+
    //         // app.gibAlleSkills();

    //     }

    //     // ArrayList<String> al1 = new ArrayList<>();
    //     // ArrayList<String> al2 = new ArrayList<>();

    //     // al1.add("1");
    //     // al1.add("2");
    //     // al1.add("3");
    //     // al1.add("4");

    //     // al2.add("3");
    //     // al2.add("4");
    //     // al2.add("5");
    //     // al2.add("6");

    //     // ArrayList<String> listen = new ArrayList<>();
    //     // listen.addAll(al1);
    //     // listen.addAll(al2);
    //     // System.out.println(listen);
    //     // listen.removeAll(al2);
    //     // System.out.println(listen);

    // }
}