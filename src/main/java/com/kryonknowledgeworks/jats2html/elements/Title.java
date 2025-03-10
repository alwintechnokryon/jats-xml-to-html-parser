package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.kryonknowledgeworks.jats2html.util.Util.htmlTagBinder;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/title.html
public class Title implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_TITLE_FULL = "<title>";
    public static String ELEMENT_TITLE = "title";

    public static String HTML_TAG = "h1";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Title(Node node,Node nestedNode, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        this.nodeList = Util.getChildNode(node);
        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                }
            } else if (!node1.getNodeName().equals("#text")){


                this.html += Util.unParsedTagBuilder(node1);
            }
        }

        if (node.getParentNode().getParentNode().getNodeName().equals("sec")){
            this.html+=Util.htmlTagBinder("h4", node.getTextContent() + (node.getTextContent().contains(".") ? "" : ".") + " " +nestedNode.getTextContent());
        }else {
            this.html+=Util.labelheadingBinder(node.getTextContent() + (node.getTextContent().contains(".") ? "" : "."), nestedNode.getTextContent());
        }
    }
    public Title(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;
            this.nodeList = Util.getChildNode(node);


            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (node1.getNodeName().equals("#text")){
                    this.html += node1.getNodeValue().replace("<","&lt;");
                }

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


            Label label = new Label(Util.getCurrentNode(Util.getChildNode(node.getParentNode()), "label"), metaDataBuilder);

            if (node.getParentNode().getNodeName().equals("sec")){
                this.html= htmlTagBinder("h4", label.element()+ " " + this.html);
            } else if(node.getParentNode().getNodeName().equals("abstract")){
                this.html= htmlTagBinder("h4", this.html);
            }else if(node.getParentNode().getNodeName().equals("kwd-group")){
                this.html= htmlTagBinder("h4", this.html);
            }else if(node.getParentNode().getNodeName().equals("ref-list")){
                this.html= htmlTagBinder("h3", this.html);
            }else if(node.getParentNode().getNodeName().equals("ack")){
                this.html= htmlTagBinder("h3", this.html);
            }else {
                this.html = htmlTagBinder(HTML_TAG, this.html);
            }

        } catch (Exception e) {
            HandleException.processException(e);

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
        return null;
    }
}
