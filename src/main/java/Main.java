import dao.*;


public class Main {
    public static void main(String[] args) throws Exception{

        Database.setDatabase("main.db");
        Database.initDatabase();
    }
}