package com.kryonknowledgeworks.jats2html.front.element;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contrib {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map =  new HashMap<>();

    public Contrib(Node node){
        try{
            this.node = node;


            nodeList = Util.getChildNode(node);



            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            String textContent = "";
            List childMap =  new ArrayList();
            List<String> aff = new ArrayList<>();

            for (Node node1 : nodeList) {
                if (tagNames.contains(node1.getNodeName()) && !node1.getNodeName().equals("#text")) {
                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text")) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                        Element element = (Element) node1.getParentNode();
                        parentKeyName = (element.getAttribute("corresp").equals("yes"))?"Corresp":"Author";
                        if (node1.getNodeName().equals("xref")){
                            Element e = (Element) node1;
                            if(e.getAttribute("ref-type").equals("aff") && e.hasAttribute("rid")){
                                aff.add(e.getAttribute("rid"));
                            }
                        }else{
                            childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                        }
                    }
                }else{
                    if (!node1.getTextContent().isBlank()){
                        Element element = (Element) node1.getParentNode();
                        parentKeyName = element.getAttribute("pub-type");
                        textContent = node1.getTextContent();
                    }
                }
            }

//            map.put(parentKeyName,childMap);


            if(!aff.isEmpty()){
                Map<String,List<String>> affList = new HashMap<>();
                affList.put("aff",aff);
                childMap.add(affList);
            }

            if (childMap.size() > 0 && textContent == ""){
                map.put(parentKeyName,childMap);
            }else if(textContent.length() > 0){
                map.put(parentKeyName,textContent);
            }

        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
