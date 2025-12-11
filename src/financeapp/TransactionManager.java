package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class TransactionManager {

    private final MongoCollection<Document> kolekcija;

    public TransactionManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        kolekcija = db.getCollection("transakcije");
    }

    public void dodajTransakciju(Transaction t) {
        kolekcija.insertOne(t.toDocument());
    }
    public void azurirajTransakciju(financeapp.Transaction t) {
        Document filter = new Document("_id", new org.bson.types.ObjectId(t.getId()));

        Document novi = new Document("Vrsta", t.getVrsta())
                .append("Iznos", t.getIznos())
                .append("Opis", t.getOpis());

        kolekcija.updateOne(filter, new Document("$set", novi));
    }
    public void obrisiTransakciju(String id) {
        kolekcija.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
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
                    d.getObjectId("_id").toHexString()
            ));
        }

        return lista;
    }

    public double ukupniPrihodi() {
        double ukupno = 0;
        for (Transaction t : dohvatiSveTransakcije()) {
            if ("Prihod".equals(t.getVrsta())) {
                ukupno += t.getIznos();
            }
        }
        return ukupno;
    }

    public double ukupniRashodi() {
        double ukupno = 0;
        for (Transaction t : dohvatiSveTransakcije()) {
            if ("Rashod".equals(t.getVrsta())) {
                ukupno += t.getIznos();
            }
        }
        return ukupno;
    }
}
