/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.data.xml.saxadapter;
/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import com.google.common.base.CharMatcher;
import com.google.common.collect.ArrayListMultimap;
import com.sepage.franceterme.data.xml.util.Util;
import com.sepage.franceterme.data.xml.util.Values;
import com.sepage.franceterme.entities.Domain;
import com.sepage.franceterme.entities.EquivalentTerm;
import com.sepage.franceterme.entities.RelatedTerm;
import com.sepage.franceterme.entities.Subdomain;
import com.sepage.franceterme.entities.Term;
import com.sepage.franceterme.entities.VariantTerm;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathExpressionException;


public class SAXAdapter extends DefaultHandler implements Values {

    private static SAXParser sax;
    private static List<Term> terms;
    private ArrayListMultimap<String, String> xmlMap = ArrayListMultimap.create(40, 20);
    private StringBuilder HTMLbuffer = new StringBuilder();
    private Term currentTerm;
    private Domain currentDomain;
    private Subdomain currentSubdomain;
    private VariantTerm currentVariantTerm;
    private EquivalentTerm currentEquivalentTerm;
    private EquivalentTerm currentEqui_VariantTerm;
    private RelatedTerm currentAntonym, currentSynonym, currentAntonymVariant, currentSynonymVariant;
    private String currentSeeAlsoTermID, tempVariantesType;
    private List<String> openXMLTags = new ArrayList<String>();
    private int numberOfHTMLTagsOpen = 0;
    private boolean variantesTagOpen = false, equivalentTagOpen = false, expectingTermeRech = false, synonymTagOpen = false, doSetTermTitle=false;


    static {
        HTML_TAGS_STATIC.add("img");
        HTML_TAGS_STATIC.add("div");
        HTML_TAGS_STATIC.add("sup");
        HTML_TAGS_STATIC.add("br");
        HTML_TAGS_STATIC.add("b");
        HTML_TAGS_STATIC.add("font");
        HTML_TAGS_STATIC.add("a");
        HTML_TAGS_STATIC.add("i");
        HTML_TAGS_STATIC.add("strong");
        HTML_TAGS_STATIC.add("p");
        HTML_TAGS_STATIC.add("em");
        HTML_TAGS_STATIC.add("span");
        IGNORABLE_XML_TAGS.add(article_tag);
        IGNORABLE_XML_TAGS.add("CRITER");
        IGNORABLE_XML_TAGS.add("Mentions-legales");
        IGNORABLE_XML_TAGS.add(domaine_tag);
    }

    public static void main (String[] args) {
        StringBuilder builder = new StringBuilder();
        List<Term> termList = new ArrayList<Term>();
        try {
            termList = parseXMLDatabase("franceterme.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        for (Term term : termList) {
            log("\n/ " + ++ i + " ================================================================\n");
            log(term.toString());
            log("\n/ "+ " SQL Query ***************\n");
            log(term.getInsertQuery());
            builder.append(term.getInsertQuery()+"\n");
        }

        FileOutputStream outputStream = null;
        File file;
        String content = builder.toString();

        try {

            file = new File("final.sql.inserts.txt");
            outputStream = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            outputStream.write(contentInBytes);
            outputStream.flush();
            outputStream.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private SAXAdapter () throws SAXException, ParserConfigurationException {
        super();
        sax = SAXParserFactory.newInstance().newSAXParser();
    }


    public static List<Term> parseXMLDatabase (String xmlDatabaseName) throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
        terms = new ArrayList<Term>();
        SAXAdapter handler = new SAXAdapter();
        sax.parse(xmlDatabaseName, handler);
        return terms;
    }


    @Override
    public void characters (char[] buffer, int start, int length) {
        String temp = new String(buffer, start, length);

        // If html tag, then add it to a cumulative value
        if (numberOfHTMLTagsOpen > 0) {
            log("characters(): adding to htmlbuffer");
            HTMLbuffer.append(temp);
        }
        else {      // if its xml, add to xmlMap
            log("characters(): adding to XML tag " + peekXMLTagFromStack());
            putToXMLMap(peekXMLTagFromStack(), temp);      // We don't store a trimmed value here because spaces matter
        }
    }


    private boolean closeHTMLTagAndCallBuffer (String tagName) {

        //HTMLbuffer.append("</" + tagName + ">");          DONT REMOVE! ENABLE WHEN NECESSARY

        numberOfHTMLTagsOpen--;
        if (numberOfHTMLTagsOpen == 0) {      // data from the last open html tag has been read
            return true;
        }
        return false;
    }

    private void resetXMLMap () {
        xmlMap.clear();
    }

    private String flushHTMLBuffer () {
        //xmlMap.put(peekXMLTagFromStack(), HTMLbuffer.toString());
        String result = HTMLbuffer.toString();
        log("HTMLBUFFER: " + result);
        HTMLbuffer = new StringBuilder();
        return result;
    }


    private void addHTMLTagAndAttributes (String tagName, Attributes attributes) {

        numberOfHTMLTagsOpen++;
        StringBuilder attributesAndValues = new StringBuilder();
        for (int i = 0; i < attributes.getLength(); i++) {
            attributesAndValues.append(" " + attributes.getQName(i) + "=\"" + attributes.getValue(i) + "\" ");
        }
        //HTMLbuffer.append("<" + tagName + attributesAndValues.toString() + ">");          DONT REMOVE!!
        log(" (MAYBE DEPENDING ON SETTINGS) ADDING HTML TAG AND ATTRIBUTES TO HTMLBUFFER: "+"<" + tagName + attributesAndValues.toString() + ">");

    }


    // Signals reading an opening tag
    @Override
    public void startElement (String uri, String localName, String tagName, Attributes attributes) {
        log("<Opening tag: " + tagName + ">");

        if (HTML_TAGS_STATIC.contains(tagName)) {         // if its an html tag
            HTML_TAGS_STATIC.add(tagName);                // this list helps distinguish between HTML and XML tags
            addHTMLTagAndAttributes(tagName, attributes); // the openXMLTags list implements a stack-like behavior via methods in this class to help manage nested XML tags
            log("HTML tag found: " + tagName + ". HTML tags in Stack: " + numberOfHTMLTagsOpen);
        }
        else {
            pushXMLTagToStack(tagName);  //adds tag name to the XMLTag stack

            if (tagName.equals(article_tag)) { //Article
                log("\n--- NEW ARTICLE -----------------------------------------------------");
                currentTerm = new Term().setId(attributes.getValue(id_attr)); // sets id
            }
            else if (tagName.equals(terme_tag)) {
                String status = attributes.getValue(statut_attr);
                if (status.equals(privilegie_attr)) {   //Then its the term title
                    doSetTermTitle = true;
                    currentTerm.setCategory(attributes.getValue(categorie_attr)).setLangage(attributes.getValue(langage_attr));
                }
                else if (status.equals(antonyme_attr)) {    // its a relatedTerm
                    expectingTermeRech = true;
                }
                else if (status.equals(synonyme_attr)) {
                    synonymTagOpen = true;
                    currentSynonym = new RelatedTerm().setCategory(attributes.getValue(categorie_attr)).setLangage(attributes.getValue(langage_attr));
                }
            }
            else if (tagName.equals(variantes_tag)) {
                tempVariantesType = attributes.getValue(type_attr); //store for variante tag
                variantesTagOpen = true;                // for determining what variante tags will be used for
            }
            else if (tagName.equals(variante_tag)) {
                if (equivalentTagOpen) {// Then its Equivalent=>Variante
                    log("EQUIVALENT=>VARIANT detected");
                    currentEqui_VariantTerm = new EquivalentTerm().setLanguage(attributes.getValue(langage_attr))
                            .setOrigin(attributes.getValue(origine_attr)).setCategory(attributes.getValue(categorie_attr)); // sets language
                }
                else if (variantesTagOpen && !synonymTagOpen) { // its Terme=>Variantes=>Variant
                    log("VARIANTES=>VARIANT detected");
                    currentVariantTerm = new VariantTerm().setLanguage(attributes.getValue(langage_attr))
                            .setCategory(attributes.getValue(categorie_attr)).setType(tempVariantesType); // sets langage + categorie + type
                }
                else if (synonymTagOpen) {      // its Terme (synonyme) =>Variantes=>Variant
                    log("VARIANTES SYNONYM detected");
                    currentSynonymVariant = new RelatedTerm().setCategory(attributes.getValue(categorie_attr));
                }
                else {
                    log("!!!! UNKNOWN VARIANT TAG");
                }
            }
            else if (tagName.equals(termerech_tag)) {
                if (expectingTermeRech) {
                    currentAntonym = new RelatedTerm().setCategory(attributes.getValue(categorie_attr))
                            .setStatus(attributes.getValue(statut_attr)).setFranceterme_id(attributes.getValue(url_attr).replaceAll(".*=", ""));
                }
            }
            else if (tagName.equals(equivalent_tag)) {
                equivalentTagOpen = true;           // for determining what variante tags will be used for
                currentEquivalentTerm = new EquivalentTerm().setCategory(attributes.getValue(categorie_attr))
                        .setOrigin(attributes.getValue(origine_attr)).setLanguage(attributes.getValue(langue_attr));
            }
            else if (tagName.equals(a_tag)) {
                currentSeeAlsoTermID = attributes.getValue(href_attr);
                currentTerm.addSeeAlsoTerm(currentSeeAlsoTermID.replaceAll(".*=", ""));
            }
            else {
                System.out.println("No attributes for: " + tagName);
            }
        }
    }


    // signals reading the closing tag
    @Override
    public void endElement (String uri, String localName, String tagName) throws SAXException {
        log("</Closing tag: " + tagName + ">");
        boolean doFlushHTMLBuffer;
        if (HTML_TAGS_STATIC.contains(tagName)) {           // if its an HTML tag then flush the buffer onto the read characters list
            doFlushHTMLBuffer = closeHTMLTagAndCallBuffer(tagName);
            log("HTML tag: " + tagName);
            if (doFlushHTMLBuffer) {
                String buffer = flushHTMLBuffer();
                putToXMLMap(openXMLTags.get(openXMLTags.size()-1), buffer);
                log("Flushing HTMLbuffer to tag:" + tagName + ", buffer:" + buffer);
            }
        }
        else {      // if its not html

            log("\nPEEK TO XML MAP - tag:" + tagName + "::" + Util.listToStringNoBreak(xmlMap.get(tagName)) + "\n");

            if (tagName.equals(article_tag)) { //Article
                terms.add(currentTerm);
                resetXMLMap();
            }
            else if (tagName.equals(dom_tag)) { //Dom
                currentDomain = new Domain().setTitle(popXMLTagValueFromMap(dom_tag));
                currentTerm.addDomain(currentDomain);
            }
            else if (tagName.equals(sdom_tag)) { //S-Dom
                currentSubdomain = new Subdomain().setTitle(popXMLTagValueFromMap(sdom_tag));
                currentTerm.addSubDomain(currentSubdomain);
            }
            else if (tagName.equals(definition_tag)) {
                currentTerm.setDefinition(popXMLTagValueFromMap(definition_tag));
            }
            else if (tagName.equals(notes_tag)) {
                currentTerm.setNotes(popXMLTagValueFromMap(notes_tag));
            }
            else if (tagName.equals(terme_tag)) {
                if (doSetTermTitle) {
                    currentTerm.setTitle(popXMLTagValueFromMap(terme_tag));
                    doSetTermTitle = false;
                }
                if (synonymTagOpen) {
                    //popXMLTagFromStack(terme_tag);
                    synonymTagOpen = false;
                }

            }
            else if (tagName.equals(termeprop_tag)) {
                currentSynonym.setTitle(popXMLTagValueFromMap(termeprop_tag));
                currentTerm.addRelatedTerm(currentSynonym);
            }
            else if (tagName.equals(variantes_tag)) {
                variantesTagOpen = false;
                tempVariantesType = null;
            }
            else if (tagName.equals(variante_tag)) {
                if (equivalentTagOpen) {
                    currentEqui_VariantTerm.setTitle(popXMLTagValueFromMap(variante_tag));
                    currentTerm.addEquivalentTerm(currentEqui_VariantTerm);
                }
                else if (variantesTagOpen && !synonymTagOpen) {
                    currentVariantTerm.setTitle(popXMLTagValueFromMap(variante_tag));
                    currentTerm.addVariantTerm(currentVariantTerm);
                }
                else if (synonymTagOpen) {
                    log("HELLOO");
                    currentSynonymVariant.setTitle(popXMLTagValueFromMap(variante_tag));
                    currentTerm.addRelatedTerm(currentSynonymVariant);
                }
            }
            else if (tagName.equals(termerech_tag)) {
                expectingTermeRech = false;
                currentAntonym.setTitle(popXMLTagValueFromMap(termerech_tag));
                currentTerm.addRelatedTerm(currentAntonym);
            }
            else if (tagName.equals(equivalent_tag)) {
                equivalentTagOpen = false;
            }
            else if (tagName.equals(equiprop_tag)) {
                currentEquivalentTerm.setTitle(popXMLTagValueFromMap(equiprop_tag));
                currentTerm.addEquivalentTerm(currentEquivalentTerm);
            }
            else if (tagName.equals(note_tag)) {
                currentEquivalentTerm.setNote(popXMLTagValueFromMap(note_tag));
            }
            else {
                System.out.println("SKIPPING TAG: " + tagName);
            }
            popXMLTagFromStack(tagName);   // remove the last opened xml tag from stack

        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      STACK / ACCESSOR METHODS BELOW
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Provides a stack-like behavior for getting xml tags. This is necessary for situations where the xml structure contains nested (and possibly improperly formatted) xml tags.g
     */
    private void pushXMLTagToStack (String tagName) {
        log("adding tag:" + tagName + " to xmlStack");
        openXMLTags.add(tagName);
    }


    private String popXMLTagFromStack (String supposedPop) {
        String pop = (openXMLTags.remove(openXMLTags.size() - 1));
        log("Popping from XML stack, size:" + openXMLTags.size());
        log("Supposed pop:" + supposedPop + ", actual pop:" + pop);

        return pop;
    }


    private String peekXMLTagFromStack () {
        log("Peeking at XML stack, size:" + openXMLTags.size() + "// peek: " + Util.listToStringNoBreak(openXMLTags));
        if (openXMLTags.size() > 0) {
            return openXMLTags.get(openXMLTags.size() - 1);
        }
        return null;
    }


    private void putToXMLMap (String tagName, String data) {
        if (! IGNORABLE_XML_TAGS.contains(tagName)) {
            xmlMap.put(tagName, CharMatcher.WHITESPACE.trimFrom(data));
        }
    }


    private String popXMLTagValueFromMap (String tagName) {
        log("Getting XML tag:" + tagName + " from xmlmap and removing it.");
        List<String> values = xmlMap.removeAll(tagName);        // removes the key and all its entries (returns entries)
        String result = "";
        for (String temp : values) {
            result += temp+" ";
        }
        return result.trim();
    }


    @Override
    public void startDocument () {
        System.out.println("Start document");
    }


    @Override
    public void endDocument () {
        System.out.println("End document");
    }


    private static void log (String message) {
        System.out.println(message);
    }

}
