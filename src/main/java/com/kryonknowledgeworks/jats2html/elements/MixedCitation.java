package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MixedCitation implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_MIXED_CITATION_FULL = "<mixed-citation>";
    public static String ELEMENT_MIXED_CITATION = "mixed-citation";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public MixedCitation(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        Node paragraph = node.getFirstChild();

        if (paragraph.getNodeValue() != null){
            this.html += paragraph.getNodeValue();
        } else {

            if (tagNames.contains(paragraph.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(paragraph.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, paragraph);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!paragraph.getNodeName().equals("#text")){

                this.html += "<pre style='color:red'>'''" + Util.convertToString(paragraph).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
            }

        }
        Node sibling = paragraph.getNextSibling();

        while (sibling != null){

            if (sibling.getNodeName().equals("#text")){
                this.html += sibling.getNodeValue();
            }

            if (tagNames.contains(sibling.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(sibling.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, sibling);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!sibling.getNodeName().equals("#text")){

                this.html += "<pre style='color:red'>'''" + Util.convertToString(sibling).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
            }

            sibling = sibling.getNextSibling();

        }

        this.html += "</p>";
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
