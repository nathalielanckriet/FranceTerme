package com.sepage.franceterme.xml.util;

        import java.util.HashSet;

public interface  Values {

    final static String article_tag = "Article", domaine_tag = "Domaine", dom_tag = "Dom", sdom_tag = "S-dom",
            definition_tag = "Definition", notes_tag = "Notes", terme_tag = "Terme", variantes_tag = "variantes",
            variante_tag = "variante", termerech_tag = "TermeRech", equivalent_tag = "Equivalent", equiprop_tag = "Equi_prop",
            note_tag = "Note", voir_tag = "Voir", a_tag = "A", termeprop_tag="Terme_prop";
    final static String id_attr = "id", statut_attr = "statut", categorie_attr = "categorie", origine_attr = "origine",
            langage_attr = "langage", langue_attr = "langue", href_attr = "href", type_attr = "type", privilegie_attr = "privilegie", url_attr = "url", antonyme_attr="antonyme", synonyme_attr="synonyme";
    final static HashSet<String> HTML_TAGS_STATIC = new HashSet<String>(), IGNORABLE_XML_TAGS= new HashSet<String>();

}
