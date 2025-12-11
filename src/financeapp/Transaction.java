
package financeapp;

import org.bson.Document;

public class Transaction {
    private String id;
    private String vrsta;
    private double iznos;
    private String opis;
    private String kategorija;

    public Transaction(String vrsta, double iznos, String opis, String kategorija) {
        this.vrsta = vrsta;
        this.iznos = iznos;
        this.opis = opis;
        this.kategorija = kategorija;
    }
    public Transaction(String vrsta, double iznos, String opis, String kategorija,String id) {
        this.vrsta = vrsta;
        this.iznos = iznos;
        this.opis = opis;
        this.kategorija = kategorija;
        this.id = id;
    }

    public Document toDocument() {
        return new Document("Vrsta", vrsta)
                .append("Iznos", iznos)
                .append("Opis", opis)
                .append("Kategorija", kategorija);
    }

    public String getVrsta() { return vrsta; }
    public double getIznos() { return iznos; }
    public String getOpis() { return opis;}
    public String getId() { return id; }
    public String getKategorija(){ return kategorija; }
    }
//TRANSAKCIJE
