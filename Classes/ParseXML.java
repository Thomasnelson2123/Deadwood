import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseXML {
    public Document getDocFromFile(String filename) throws ParserConfigurationException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;
        try {
            doc = db.parse(filename);
        } 
        catch (Exception ex) {
            System.out.println("XML parse failure");
            System.out.println(filename);
            ex.printStackTrace();
        }
        return doc;
    }

    public Room[] readBoardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList sets = root.getElementsByTagName("set");
        String setName = "";
        int[] dims = new int[4];
        
        for (int i = 0; i < sets.getLength(); i++) {

        }
        return null;
    }

    // reads all the card data for the game and returns an array of Scene objects
    public Scene[] readCardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        ArrayList<Scene> scenes = new ArrayList<Scene>();
        for (int i = 0; i < cards.getLength(); i++) {
            int number = 0;
            Node card = cards.item(i);
            String caption = "";
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
            NodeList children = card.getChildNodes();
            ArrayList<Role> roles = new ArrayList<Role>();
            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if("scene".equals(sub.getNodeName())){
                    number = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
                    caption = sub.getTextContent();
                }
                if("part".equals(sub.getNodeName())){
                    Role role = readRoleData(sub, true);
                    roles.add(role);
                }
            }      
            Role[] roleArray = roles.toArray(new Role[roles.size()]);
            Scene scene = new Scene(cardName, number, caption, budget, roleArray);
            scenes.add(scene);     
        }
        return scenes.toArray(new Scene[scenes.size()]);
    }

    // reads a Role ("part") from the XML and returns a new Role class
    private Role readRoleData(Node node, boolean onCard) {
        String partName = node.getAttributes().getNamedItem("name").getNodeValue();
        int level = Integer.parseInt(node.getAttributes().getNamedItem("level").getNodeValue());
        String line = "";
        int[] dims = new int[4];
        NodeList partChildren = node.getChildNodes();
        for (int k = 0; k < partChildren.getLength(); k++) {
            Node sub = partChildren.item(k);
            if("area".equals(sub.getNodeName())){
                dims[0] = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
                dims[1] = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
                dims[2] = Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue());
                dims[3] = Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue());
            }
            if("line".equals(sub.getNodeName())) {
                line = sub.getTextContent();
            }
        }
        return new Role(partName, line, level, onCard, dims);
    }
    
}
