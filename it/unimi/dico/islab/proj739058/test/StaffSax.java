package it.unimi.dico.islab.proj739058.test;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class StaffSax extends DefaultHandler
{
    boolean search = false;


    public void startElement(String uri, String localName,
         String gName, Attributes attributes) throws SAXException
    {
      if (localName.equals("p"))
         System.out.println(attributes.getValue("","title"));
    }
}