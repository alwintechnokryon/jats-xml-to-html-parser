package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/article-id.html
public class ArticleId implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<article-id>";
    public static String ELEMENT = "article-id";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";


    public ArticleId(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        elementFilter();

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!node1.getNodeName().equals("#text")){


                this.html += Util.unParsedTagBuilder(node1);
            }

        }

        String pubIdType = node.getAttributes().getNamedItem("pub-id-type").getNodeValue();

        if (pubIdType.equals("doi")){
            this.html += "<div class='mb-3' id='doi-content'><h4>DOI</h4><a class='blank' href='https://doi.org/"+node.getFirstChild().getNodeValue()+"'>" + node.getFirstChild().getNodeValue() +"</a></div>";
        }





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
        return this.node.hasChildNodes();
    }


    public List<Node> elementFilter() {

        this.childNodes = this.node.getChildNodes();

        for (int i = 0; i < this.childNodes.getLength(); i++) {

            this.childNodes.item(i);

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }
        }
        return nodeList;
    }
}
