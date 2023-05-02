
import javax.xml.parsers.ParserConfigurationException;

public class Deadwood{

    static Room[] rooms;
    static Scene[] scenes;
    public static void main(String[] args) throws ParserConfigurationException, Exception {
        

        Controller controller;
        Bank bank;
        Board board;
        GameManager manager;

        // parses the XML files and initializes the rooms and scenes
        parseXML();


        
    }

    // initializes game manager, board, players, etc
    private static void startGame(){

    }

    // resets entire game state (days, players, etc)
    private static void resetGame(){

    }

    private static void parseXML() throws Exception{
        ParseXML parser = new ParseXML();
        String cwd = System.getProperty("user.dir");
        String[] path = cwd.split("\\\\");
        String prefix = "";
        // because for somer reason when Deadwood.java is run, it doesn't recognize
        // it is in the classes directory... but sometimes it does?
        if (path[path.length - 1].equals("Classes")) {
            prefix = "../XML_Files/";
        }
        else {
            if (path[path.length - 1].equals("Deadwood")) {
                throw new Exception("In an unknown directory");
            }
            prefix = "XML_Files/";
        }

        rooms = parser.readBoardData(parser.getDocFromFile(prefix + "board.xml"));
        scenes = parser.readCardData(parser.getDocFromFile(prefix + "cards.xml"));
    }
    
}