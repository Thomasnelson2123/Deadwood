
import javax.xml.parsers.ParserConfigurationException;

public class Deadwood{

    static Room[] rooms;
    static Scene[] scenes;
    public static void main(String[] args) throws ParserConfigurationException, Exception {
        
        // get number of players
        if (args.length!= 1) {
            System.out.println("Program requires one argument (number of players)");
            return;
        }
        int numPlayers = 0;
        try {
            numPlayers = Integer.parseInt(args[0]);
            if (numPlayers < 2 || numPlayers > 8) {
                System.out.println("Game only supports 2-8 players");
                return;
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Argument must be an integer between 2 and 8");
            return;
        }

        // parses the XML files and initializes the rooms and scenes
        parseXML();

        Bank bank = new Bank();
        Board board = new Board(numPlayers, rooms, scenes);
        Player[] players = new Player[numPlayers];
        GameManager manager = new GameManager(board, players, bank);

        // main gameplay loop
        manager.mainLoop();
  
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
            if (!path[path.length - 1].equals("Deadwood")) {
                throw new Exception("In an unknown directory");
            }
            prefix = "XML_Files/";
        }

        System.out.println("hi");
        rooms = parser.readBoardData(parser.getDocFromFile(prefix + "board.xml"));
        scenes = parser.readCardData(parser.getDocFromFile(prefix + "cards.xml"));
    }
    
}