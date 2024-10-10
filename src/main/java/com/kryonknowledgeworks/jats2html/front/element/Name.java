package com.kryonknowledgeworks.jats2html.front.element;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Name {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();

    public Name(Node node){
        try{
            this.node = node;

            nodeList = Util.getChildNode(node);
            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            List<Object> childMap = new ArrayList<>();
            String textContent = "";
            Boolean hasElementCitation = node.getParentNode().getNodeName().equals("element-citation");

            for (Node nodeParent : nodeList){
                if (hasElementCitation){

                        if (tagNames.contains(nodeParent.getNodeName()) && !nodeParent.getNodeName().equals("#text")) {
                            String className = ClassNameSingleTon.tagToClassName(nodeParent.getNodeName());
                            if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !nodeParent.getNodeName().equals("#text")) {
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, nodeParent);
                                childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                            }
                        }else{
                            if (nodeParent.getNodeName().equals("#text")  && !nodeParent.getTextContent().isBlank()){
                                textContent = nodeParent.getTextContent();
                            }
                        }

                }
                else{
                    List<Node> childNodes = Util.getChildNode(nodeParent);
                    for (Node node1 : childNodes){


                        if (tagNames.contains(node1.getNodeName()) && node1.getNodeType() == Node.ELEMENT_NODE) {

                            String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());

                            if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text")) {
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                                childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                            }

                        }else{

                            if (node1.getNodeType() == Node.TEXT_NODE  && !node1.getTextContent().isBlank()){

                                textContent = node1.getTextContent();
                                map.put(node1.getParentNode().getNodeName(),textContent);
                            }
                        }
                    }
                }
            }


           if (hasElementCitation){
               if (childMap.size() > 0 && textContent == ""){
               map.put(parentKeyName,childMap);
               }else if(textContent.length() > 0){
                map.put(parentKeyName,textContent);
               }
           }

        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
