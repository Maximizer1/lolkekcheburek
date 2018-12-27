package com.company;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.objects.Chat;
import java.util.ArrayList;

public class UserManager {
    private static UserManager sInstance;
    private static final String DB_HOST = "ds139614.mlab.com";
    private static final int DB_PORT = 39614;
    private static final String DB_NAME = "heroku_lsx8lb9l";
    private static final String DB_USER = "Db_Maks";
    private static final String DB_PASSWORD = "dbmaks123";

    private static final String DB_URL = "mongodb://" + DB_USER + ":" + DB_PASSWORD + "@" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private MongoCollection<Document> mUserCollection1;
    private MongoCollection<Document> mUserCollection2;
    private MongoCollection impwords;
    private MongoCollection notimpwords;
    private MongoCollection gettingimpwords;
    private MongoCollection gettingnotimpwords;

    public MongoClientURI clientURL = new MongoClientURI(DB_URL);
    public MongoClient client1 = new MongoClient(clientURL);
    public MongoDatabase db = client1.getDatabase(DB_NAME);



    public UserManager() {
        mUserCollection1 = db.getCollection("ImportantWords");
        impwords = db.getCollection("ImportantWords");

        mUserCollection2 = db.getCollection("NotImportantWords");
        notimpwords = db.getCollection("NotImportantWords");

    }

    public void addImportantAnswer(int _id, String word, String status) {
        Document document = new Document();
        document.put("word", word);
        document.put("status", status);
        impwords.insertOne(document);
    }

    public void addNotImportantAnswer(int _id, String word, String status) {
        Document document = new Document();
        document.put("_id", _id);
        document.put("word", word);
        document.put("status", status);
        notimpwords.insertOne(document);
    }

    public boolean isIdDuplicateInImportant(int id) {
        boolean isDuplicate = true;
        Document query = new Document("_id", id);
        Document user = mUserCollection1.find(query).first();

        if (user == null) {
            isDuplicate = false;
        }

        return isDuplicate;
    }

    public boolean isIdDuplicateInNotImportant(int id) {
        boolean isDuplicate = true;
        Document query = new Document("_id", id);
        Document user = mUserCollection2.find(query).first();

        if (user == null) {
            isDuplicate = false;
        }

        return isDuplicate;
    }

    public void showAllList()
    {
        MongoCollection<Document> coll = db.getCollection("ImportantWords");
        long count = coll.count();
        System.out.println(count);
    }

    public ArrayList<String> showAllList(String status)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        if (status.equals("Important")){
            MongoCollection<Document> coll = db.getCollection("ImportantWords");
            for (Document currentDoc : coll.find()){
                arrayList.add(currentDoc.getString("word"));
            }
        }else {
            MongoCollection<Document> coll = db.getCollection("NotImportantWords");
            for (Document currentDoc : coll.find()){
                arrayList.add(currentDoc.getString("word"));
            }
        }
        return arrayList;
    }
//    public void ImportantWord(String text){
//        Document document = new Document();
//        document.put("Important", text);
//        words.insertOne(document);
//    }
//
//    public void LessImportantWord(String text){
//        Document document = new Document();
//        document.put("LessImportant", text);
//        words.insertOne(document);
//    }
//
//    public static UserManager getInstance() {
//        if (sInstance == null) {
//            sInstance = new UserManager();
//        }
//        return sInstance;
//    }
//
//    public String getWord(int id) {
//        Document query = new Document("_id", _id); ++_id;
//        Document user = mUserCollection.find(query).first();
//
//        String word = user.getString("word");
//
//        return word;
//    }
//
//    public String getNameByUpdate(Update update) {
//        Document query = new Document("id", update.getMessage().getChatId());
//        Document user = mUserCollection.find(query).first();
//        if (user == null){
//            return addNewUser(update);
//        } else {
//            String firstName = user.getString("firstName");
//            String lastName = user.getString("lastName");
//            return firstName + " " + lastName;
//        }
//    }
//
//    public void showImportant(int id)
//    {
//
//    }
//
//    public String addNewUser(Update update) {
//        Document document = new Document("id", update.getMessage().getChatId())
//                .append("firstName", update.getMessage().getFrom().getFirstName())
//                .append("lastName", update.getMessage().getFrom().getLastName());
//        mUserCollection.insertOne(document);
//        return update.getMessage().getFrom().getFirstName();
//    }
//    public String getNameByChat(Update update) {
//        Document query = new Document("id", update.getMessage().getChatId().toString());
//
//        Document user = mUserCollection.find(query).first();
//
//        if (user == null) {
//            Document newUser = new Document("id", update.getMessage().getChat().getId().toString())
//                    .append("firstName", update.getMessage().getChat().getFirstName())
//                    .append("lastName", update.getMessage().getChat().getLastName())
//                    .append("important", update.getMessage().getText())
//                    .append("lesimp", update.getMessage().getChat().getUserName())
//                    .append("username", update.getMessage().getChat().getUserName());
//
//            mUserCollection.insertOne(newUser);
//            return "Вас не було в базі користувачів, але ми вас додали ;-)\n";
//        }
//        String firstName = user.getString("firstName");
//        String lastName = user.getString("lastName");
//        String username = user.getString("username");
//
//        // Більше інформації по стилізації тексту в Телеграмі: https://core.telegram.org/bots/api#markdown-style
//        return "*" + firstName + " " + lastName + "*, нікнейм: *" + username + "*\n";
//    }
//    public String write(Chat chat ){
//        Document query = new Document("id", chat.getId().toString());
//        Document user = mUserCollection.find(query).first();
//        return  user.getString("important");
//    }
}