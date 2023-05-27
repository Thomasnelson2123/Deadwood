
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

public class Deadwood{

    static Room[] rooms;
    static Scene[] scenes;

    // main function for deadwood, initializes controller/view, and then tells controller to initialize the model.
    public static void main(String[] args) throws ParserConfigurationException, Exception {
        
        Random rand = new Random(3);
        // get number of players
        

        Gooey gui = new Gooey();
        gui.setVisible(true);
        int numPlayers = 0;
        try {
            numPlayers = Integer.parseInt(JOptionPane.showInputDialog(gui, "How many players?")); 

        } catch (Exception e) {
            System.out.println("Well well well, have you got a liscence for that unparsable integer? Sounds like you not compiling, init? Off you go");
            System.exit(0);
        }
        // parses the XML files and initializes the rooms and scenes
        parseXML();

        Bank bank = new Bank(rand);
        Board board = new Board(numPlayers, rooms, scenes, rand);
        Player[] players = new Player[numPlayers];
        GameManager manager = new GameManager(board, players, bank, rand);

        gui.setManager(manager);

        // main gameplay loop

        
        // Take input from the user about number of players
  
    }

    // initializes the parseXML functions in the parseXML class with the correct filepath
    private static void parseXML() throws Exception{
        ParseXML parser = new ParseXML();
        String cwd = System.getProperty("user.dir");
        String[] path = cwd.split("\\\\");
        //String prefix = "../XML_Files/";
        String prefix = "XML_Files/";
        System.out.println(prefix);
        // because for somer reason when Deadwood.java is run, it doesn't recognize
        // it is in the classes directory... but sometimes it does?
        rooms = parser.readBoardData(parser.getDocFromFile(prefix + "board.xml"));
        scenes = parser.readCardData(parser.getDocFromFile(prefix + "cards.xml"));
    }
    
}