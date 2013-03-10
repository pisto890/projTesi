package it.unimi.dico.islab.proj739058;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class StaffSax extends DefaultHandler
{
	
	public List<String> results = new ArrayList<String>();
    boolean search = false;


    public void startElement(String uri, String localName,
         String gName, Attributes attributes) throws SAXException
    {
      if (localName.equals("p")) {
         results.add(attributes.getValue("","title").replace(" ", "_"));
      }
    }
}