package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Graphic implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<graphic>";
    public static String ELEMENT = "graphic";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Graphic(Node node,String id, MetaDataBuilder metaDataBuilder) {
        this.node = node;
        NamedNodeMap map=node.getAttributes();
        Node xlinkNode=  map.getNamedItem("xmlns:xlink");
        String xlink="";
        if(xlinkNode!=null) {
            xlink = node.getAttributes().getNamedItem("xmlns:xlink").getNodeValue();
        }
//            String filePath= KryonXMLParser.filePath;
//            File file=new File(filePath);
//            String fileImagePath=file.getParentFile().toString();
        String href = "";
        if (node.getAttributes().getNamedItem("xlink:href") != null){
            href = node.getAttributes().getNamedItem("xlink:href").getNodeValue();
        } else if(node.getAttributes().getNamedItem("xlink-href") != null){
            href = node.getAttributes().getNamedItem("xlink-href").getNodeValue();
        }

        this.html+= Util.htmlImageBinder(xlink,"/assets/articles/" + Util.getPublisherId(node) + "/" + href ,id);

    }

    public Graphic(Node node, MetaDataBuilder metaDataBuilder) {

        this.node = node;
        NamedNodeMap map=node.getAttributes();
        Node xlinkNode=  map.getNamedItem("xmlns:xlink");
        String xlink="";
        if(xlinkNode!=null) {
            xlink = node.getAttributes().getNamedItem("xmlns:xlink").getNodeValue();
        }
//            String filePath= KryonXMLParser.filePath;
//            File file=new File(filePath);
//            String fileImagePath=file.getParentFile().toString();
        String href = "";
        if (node.getAttributes().getNamedItem("xlink:href") != null){
            href = node.getAttributes().getNamedItem("xlink:href").getNodeValue();
        } else if(node.getAttributes().getNamedItem("xlink-href") != null){
            href = node.getAttributes().getNamedItem("xlink-href").getNodeValue();
        }

        this.html+= Util.htmlImageBinder(xlink,"/assets/articles/" + Util.getPublisherId(node) + "/" + href);

    }

    @Override
    public String element() {
        return html;
    }

    @Override
    public List<String> elements() {
        return null;
    }

    @Override
    public Boolean isChildAvailable() {
        return null;
    }
}
