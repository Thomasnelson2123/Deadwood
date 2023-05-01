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
        ArrayList<Room> rooms = new ArrayList<Room>();
        Element root = d.getDocumentElement();
        NodeList sets = root.getElementsByTagName("set");
        // iterate over all the rooms on the board
        for (int i = 0; i < sets.getLength(); i++) {
            ArrayList<ShotCounter> shots = new ArrayList<ShotCounter>();
            ArrayList<String> neighbors = new ArrayList<String>();
            ArrayList<Role> roles = new ArrayList<Role>();
            int[] roomDims = new int[4];
            Node set = sets.item(i);       
            String roomName = set.getAttributes().getNamedItem("name").getNodeValue();
            NodeList children = set.getChildNodes();
            // iterate over all children of a set
            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                // neighbors of set
                if("neighbors".equals(sub.getNodeName())){
                    NodeList neighborsList = sub.getChildNodes();
                    // iterate over all neighbors of a set
                    for (int k = 0; k < neighborsList.getLength(); k++) {
                        Node n = neighborsList.item(k);
                        if ("neighbor".equals(n.getNodeName())) {
                            // found a neighbor, add it to list 
                            neighbors.add(n.getAttributes().getNamedItem("name").getNodeValue());
                        }  
                    }
                }

                // area of room
                if ("area".equals(sub.getNodeName())) {
                    roomDims = getArea(sub);
                }

                // shotcounters of room
                if("takes".equals(sub.getNodeName())) {
                    NodeList takesList = sub.getChildNodes();
                    // iterate over all takes of a set
                    for (int k = 0; k < takesList.getLength(); k++) {
                        int takeNum = 0;
                        int[] takeDims = new int[4];
                        Node n = takesList.item(k);
                        if ("take".equals(n.getNodeName())) {
                            takeNum = Integer.parseInt(n.getAttributes().getNamedItem("number").getNodeValue());
                            NodeList areas = n.getChildNodes();
                            for (int l = 0; l < areas.getLength(); l++) {
                                Node a = areas.item(l);
                                if("area".equals(a.getNodeName())) {
                                    takeDims = getArea(a);
                                }
                            }
                            ShotCounter take = new ShotCounter(takeNum, takeDims, false);
                            shots.add(take); 
                        } 
                    }
                }

                // get offcard roles
                if("parts".equals(sub.getNodeName())) {
                    NodeList partsList = sub.getChildNodes();
                    for(int k = 0; k < partsList.getLength(); k++) {
                        Node part = partsList.item(k);
                        if("part".equals(part.getNodeName())) {
                            Role role = readRoleData(part, false);
                            roles.add(role);
                        }
                    }
                }
  
            }
            // create the room with gathered data and add to rooms list
            Room room = new Room(roles.toArray(new Role[roles.size()]), roomName, 
            neighbors.toArray(new String[neighbors.size()]), roomDims, 
            shots.toArray(new ShotCounter[shots.size()]));
            rooms.add(room);


        }
        return rooms.toArray(new Room[rooms.size()]);
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
                dims = getArea(sub);
            }
            if("line".equals(sub.getNodeName())) {
                line = sub.getTextContent();
            }
        }
        return new Role(partName, line, level, onCard, dims);
    }

    private int[] getArea(Node sub) {
        int[] dims = new int[4];
        dims[0] = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
        dims[1] = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
        dims[2] = Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue());
        dims[3] = Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue());
        return dims;
    }
    
}
