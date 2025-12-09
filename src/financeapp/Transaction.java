package financeapp;

import org.bson.Document;

public class Transaction {
    private String vrsta;
    private double iznos;
    private String opis;

    public Transaction(String vrsta, double iznos, String opis) {
        this.vrsta = vrsta;
        this.iznos = iznos;
        this.opis = opis;
    }

    public Document toDocument() {
        return new Document("Vrsta", vrsta)
                .append("Iznos", iznos)
                .append("Opis", opis);
    }

    public String getVrsta() { return vrsta; }
    public double getIznos() { return iznos; }
    public String getOpis() { return opis; }
}
