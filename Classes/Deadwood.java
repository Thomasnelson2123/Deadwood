
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.file.Paths;

public class Deadwood{
    public static void main(String[] args) throws ParserConfigurationException {
        Controller controller;
        Bank bank;
        Board board;
        GameManager manager;
        ParseXML parser = new ParseXML();

        /*
        File f = new File("../XML_Files/board.xml");


        if(!f.exists()){
            f = new File("XML_Files/board.xml");
        }

        Room[] rooms = parser.readBoardData(parser.getDocFromFile("../XML_Files/board.xml"));
        Scene[] scenes = parser.readCardData(parser.getDocFromFile("../XML_Files/cards.xml"));
        */
    }

    // initializes game manager, board, players, etc
    public void startGame(){

    }

    // resets entire game state (days, players, etc)
    public void resetGame(){

    }
    
}