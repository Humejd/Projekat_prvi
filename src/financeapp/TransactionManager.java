
package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Map;
import java.util.HashMap;


import java.util.ArrayList;

public class TransactionManager {

    private final MongoCollection<Document> kolekcija;

    public TransactionManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        kolekcija = db.getCollection("transakcije"
                );
    }

    public void dodajTransakciju(Transaction t) {
        kolekcija.insertOne(t.toDocument());
    }
    public void azurirajTransakciju(financeapp.Transaction t) {
        Document filter = new Document("_id", new org.bson.types.ObjectId(t.getId()));

        Document novi = new Document("Vrsta", t.getVrsta())
                .append("Iznos", t.getIznos())
                .append("Opis", t.getOpis())
                .append("Kategorija", t.getKategorija());

        kolekcija.updateOne(filter, new Document("$set", novi));
    }
    public void obrisiTransakciju(String id) {
        kolekcija.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
    }
    public Map<String, Double> pripremaZaExport() {

        Map<String, Double> podaci = new HashMap<>();


        podaci.put("Prihodi", 0.0);
        podaci.put("Rashodi", 0.0);
        podaci.put("Hrana", 0.0);
        podaci.put("Prijevoz", 0.0);
        podaci.put("Zabava", 0.0);
        podaci.put("Plata", 0.0);
        podaci.put("Racuni", 0.0);
        podaci.put("Ostalo", 0.0);

        for (Transaction t : dohvatiSveTransakcije()) {

            if ("Prihod".equals(t.getVrsta())) {
                podaci.put("Prihodi", podaci.get("Prihodi") + t.getIznos());
            } else if ("Rashod".equals(t.getVrsta())) {
                podaci.put("Rashodi", podaci.get("Rashodi") + t.getIznos());
            }

            switch (t.getKategorija()) {
                case "Plata":
                case "Hrana":
                case "Prijevoz":
                case "Zabava":
                case "Racuni":
                    podaci.put(
                            t.getKategorija(),
                            podaci.get(t.getKategorija()) + t.getIznos()
                    );
                    break;
                default:
                    podaci.put(
                            "Ostalo",
                            podaci.get("Ostalo") + t.getIznos()
                    );
            }
        }

        return podaci;
    }


    public ArrayList<Transaction> dohvatiSveTransakcije() {
        ArrayList<Transaction> lista = new ArrayList<>();
        MongoCursor<Document> kursor = kolekcija.find().iterator();

        while (kursor.hasNext()) {
            Document d = kursor.next();
            lista.add(new Transaction(
                    d.getString("Vrsta"),
                    d.getDouble("Iznos"),
                    d.getString("Opis"),
                    d.getString("Kategorija"),
                    d.getObjectId("_id").toHexString()
            ));
        }

        return lista;
    }




}
//TRANSAKKCIJSKI MENADZER