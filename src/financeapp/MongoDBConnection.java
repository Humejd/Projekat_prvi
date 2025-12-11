package financeapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "finansijeDB";

    private static MongoClient klijent = null;

    public static MongoDatabase getDatabase() {
        if (klijent == null) {
            klijent = MongoClients.create(URI);
        }
        return klijent.getDatabase(DB_NAME);
    }
}
//MONGO KONEKCIJA