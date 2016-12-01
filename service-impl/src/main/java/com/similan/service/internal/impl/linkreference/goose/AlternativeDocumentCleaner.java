package com.similan.service.internal.impl.linkreference.goose;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.TagsEvaluator;

import scala.collection.JavaConversions;
import scala.collection.mutable.ListBuffer;
import scala.collection.mutable.StringBuilder;

import com.google.common.collect.Sets;
import com.gravity.goose.Article;
import com.gravity.goose.cleaners.StandardDocumentCleaner;
import com.gravity.goose.text.ReplaceSequence;

@Slf4j
public class AlternativeDocumentCleaner extends StandardDocumentCleaner {
  @Override
  public Document clean(Article article) {
    log.trace("Starting cleaning phase with DefaultDocumentCleaner");
    Document docToClean = article.doc();
    docToClean = cleanEmTags(docToClean);
    docToClean = removeDropCaps(docToClean);
    docToClean = removeScriptsAndStyles(docToClean);
    docToClean = cleanBadTags(docToClean);
    docToClean = removeNodesViaRegEx(docToClean, captionPattern);
    docToClean = removeNodesViaRegEx(docToClean, googlePattern);
    docToClean = removeNodesViaRegEx(docToClean, entriesPattern);
    docToClean = removeNodesViaRegEx(docToClean, facebookPattern);
    docToClean = removeNodesViaRegEx(docToClean, twitterPattern);
    docToClean = cleanUpSpanTagsInParagraphs(docToClean);
    docToClean = convertWantedTagsToParagraphs(docToClean, articleRootTags);
    // docToClean = convertDivsToParagraphs(docToClean, "div");
    // docToClean = convertDivsToParagraphs(docToClean, "span");

    // docToClean = convertDivsToParagraphs(docToClean, "span");
    return docToClean;
  }

  /**
   * replaces <em> tags with textnodes
   */
  private Document cleanEmTags(Document doc) {
    Elements ems = doc.getElementsByTag("em");

    for (Element node : ems) {
      Elements images = node.getElementsByTag("img");
      if (images.size() == 0) {
        TextNode tn = new TextNode(node.text(), doc.baseUri());
        node.replaceWith(tn);
      }
    }
    log.trace(ems.size() + " EM tags modified");
    return doc;
  }

  /**
   * takes care of the situation where you have a span tag nested in a paragraph
   * tag e.g. businessweek2.txt
   */
  private Document cleanUpSpanTagsInParagraphs(Document doc) {
    Elements spans = doc.getElementsByTag("span");
    for (Element item : spans) {
      if (item.parent().nodeName().equals("p")) {
        TextNode tn = new TextNode(item.text(), doc.baseUri());
        item.replaceWith(tn);
        log.trace("Replacing nested span with TextNode: " + item.text());
      }
    }
    return doc;
  }

  /**
   * remove those css drop caps where they put the first letter in big text in
   * the 1st paragraph
   */
  private Document removeDropCaps(Document doc) {
    Elements items = doc.select("span[class~=(dropcap|drop_cap)]");
    log.trace(items.size() + " dropcap tags removed");
    for (Element item : items) {
      TextNode tn = new TextNode(item.text(), doc.baseUri());
      item.replaceWith(tn);
    }
    return doc;
  }

  private Document removeScriptsAndStyles(Document doc) {

    Elements scripts = doc.getElementsByTag("script");
    for (Element item : scripts) {
      removeNode(item);
    }
    log.trace(scripts.size() + " script tags removed");

    Elements styles = doc.getElementsByTag("style");

    for (Element style : styles) {
      removeNode(style);
    }
    log.trace(styles.size() + " style tags removed");
    return doc;
  }

  private Document cleanBadTags(Document doc) {
    Elements children = doc.body().children();
    Elements naughtyList = children.select(queryNaughtyIDs);
    log.trace(naughtyList.size() + " naughty ID elements found");

    for (Element node : naughtyList) {
      log.trace("Removing node with id: " + node.id());
      removeNode(node);
    }

    Elements naughtyList2 = children.select(queryNaughtyIDs);
    log.trace(naughtyList2.size() + " naughty ID elements found after removal");

    Elements naughtyClasses = children.select(queryNaughtyClasses);

    log.trace(naughtyClasses.size() + " naughty CLASS elements found");

    for (Element node : naughtyClasses) {
      log.trace("Removing node with class: " + node.className());
      removeNode(node);
    }

    Elements naughtyClasses2 = children.select(queryNaughtyClasses);
    log.trace(naughtyClasses2.size()
        + " naughty CLASS elements found after removal");

    Elements naughtyList5 = children.select(queryNaughtyNames);

    log.trace(naughtyList5.size() + " naughty Name elements found");

    for (Element node : naughtyList5) {

      log.trace("Removing node with class: " + node.attr("class") + " id: "
          + node.id() + " name: " + node.attr("name"));

      removeNode(node);
    }
    return doc;
  }

  /**
   * removes nodes that may have a certain pattern that matches against a class
   * or id tag
   * 
   * @param pattern
   */
  private Document removeNodesViaRegEx(Document doc, Pattern pattern) {
    try {
      Elements naughtyList = doc.getElementsByAttributeValueMatching("id",
          pattern);

      log.trace(naughtyList.size() + " ID elements found against pattern: "
          + pattern);

      for (Element node : naughtyList) {
        removeNode(node);
      }
      Elements naughtyList3 = doc.getElementsByAttributeValueMatching("class",
          pattern);
      log.trace(naughtyList3.size() + " CLASS elements found against pattern: "
          + pattern);

      for (Element node : naughtyList3) {
        removeNode(node);
      }
    } catch (IllegalArgumentException e) {
      log.warn(e.toString(), e);
    }
    return doc;
  }

  /**
   * Apparently jsoup expects the node's parent to not be null and throws if it
   * is. Let's be safe.
   * 
   * @param node
   *          the node to remove from the doc
   */
  private void removeNode(Node node) {
    if (node == null || node.parent() == null)
      return;
    node.remove();
  }

  public void replaceElementsWithPara(Document doc, Element div) {
    Document newDoc = new Document(doc.baseUri());
    Element newNode = newDoc.createElement("p");
    newNode.append(div.html());
    div.replaceWith(newNode);
  }

  private Document convertWantedTagsToParagraphs(Document doc,
      TagsEvaluator wantedTags) {

    Elements selected = Collector.collect(wantedTags, doc);

    for (Element elem : selected) {
      if (Collector.collect(blockElemementTags, elem).isEmpty()) {
        replaceElementsWithPara(doc, elem);
      } else {
        ListBuffer<Node> replacements = getReplacementNodes(doc, elem);
        for (Element child : elem.children()) {
          removeNode(child);
        }
        for (Node n : JavaConversions.asJavaIterable(replacements)) {
          try {
            elem.appendChild(n);
          } catch (Exception ex) {
            log.info("Failed to append cleaned child!", ex);
          }
        }
      }
    }
    return doc;
  }

  @SuppressWarnings("unused")
  private Document convertDivsToParagraphs(Document doc, String domType) {
    log.trace("Starting to replace bad divs...");
    int badDivs = 0;
    int convertedTextNodes = 0;
    Elements divs = doc.getElementsByTag(domType);
    int divIndex = 0;

    for (Element div : divs) {
      try {
        Matcher divToPElementsMatcher = divToPElementsPattern.matcher(div
            .html().toLowerCase());
        if (divToPElementsMatcher.find() == false) {
          replaceElementsWithPara(doc, div);
          badDivs += 1;
        } else {
          ListBuffer<Node> replaceNodes = getReplacementNodes(doc, div);
          for (Element child : div.children()) {
            removeNode(child);
          }
          for (Node node : JavaConversions.asJavaIterable(replaceNodes)) {
            try {
              div.appendChild(node);
            } catch (Exception e) {
              log.info(e.toString(), e);
            }
          }
        }
      } catch (NullPointerException e) {
        log.error(e.toString());
      }
      divIndex += 1;
    }

    log.trace(String
        .format(
            "Found %d total %s with %d bad ones replaced and %d textnodes converted inside %s",
            divs.size(), domType, badDivs, convertedTextNodes, domType));

    return doc;
  }

  /**
   * go through all the div's nodes and clean up dangling text nodes and get rid
   * of obvious jank
   */
  public Element getFlushedBuffer(StringBuilder replacementText, Document doc) {
    val bufferedText = replacementText.toString();
    log.trace("Flushing TextNode Buffer: " + bufferedText.trim());
    Document newDoc = new Document(doc.baseUri());
    Element newPara = newDoc.createElement("p");
    newPara.html(replacementText.toString());
    return newPara;
  }

  public ListBuffer<Node> getReplacementNodes(Document doc, Element div) {

    StringBuilder replacementText = new StringBuilder();
    ListBuffer<Node> nodesToReturn = new ListBuffer<Node>();

    ListBuffer<Node> nodesToRemove = new ListBuffer<Node>();

    for (Node kid : div.childNodes()) {
      if (kid.nodeName().equals("p") && replacementText.size() > 0) {

        // flush the buffer of text
        val newNode = getFlushedBuffer(replacementText, doc);
        nodesToReturn = nodesToReturn.$plus$eq(newNode);
        replacementText.clear();

        if (kid instanceof Element) {
          val kidElem = (Element) kid;
          nodesToReturn = nodesToReturn.$plus$eq(kidElem);
        }

      } else if (kid.nodeName().equals("#text")) {
        
        val kidTextNode = (TextNode) kid;
        val kidText = kidTextNode.attr("text");
        val replaceText = tabsAndNewLinesReplacements.replaceAll(kidText);
        if (replaceText.trim().length() > 1) {

          Node prevSibNode = kidTextNode.previousSibling();
          while (prevSibNode != null && prevSibNode.nodeName().equals("a")
              && !prevSibNode.attr("grv-usedalready").equals("yes")) {
            replacementText.append(" " + prevSibNode.outerHtml() + " ");
            nodesToRemove = nodesToRemove.$plus$eq(prevSibNode);
            prevSibNode.attr("grv-usedalready", "yes");
            prevSibNode = (prevSibNode.previousSibling() == null) ? null
                : prevSibNode.previousSibling();
          }
          // add the text of the node
          replacementText.append(replaceText);

          // check the next set of links that might be after text (see
          // businessinsider2.txt)
          Node nextSibNode = kidTextNode.nextSibling();
          while (nextSibNode != null && nextSibNode.nodeName().equals("a")
              && !nextSibNode.attr("grv-usedalready").equals("yes")) {
            replacementText.append(" " + nextSibNode.outerHtml() + " ");
            nodesToRemove = nodesToRemove.$plus$eq(nextSibNode);
            nextSibNode.attr("grv-usedalready", "yes");
            nextSibNode = (nextSibNode.nextSibling() == null) ? null
                : nextSibNode.nextSibling();
          }
        }
        nodesToRemove = nodesToRemove.$plus$eq(kid);

      } else {
        nodesToReturn = nodesToReturn.$plus$eq(kid);
      }
    }
    // flush out anything still remaining
    if (replacementText.size() > 0) {
      val newNode = getFlushedBuffer(replacementText, doc);
      nodesToReturn = nodesToReturn.$plus$eq(newNode);
      replacementText.clear();
    }
    for (Node node : JavaConversions.asJavaIterable(nodesToRemove)) {
      removeNode(node);
    }
    return nodesToReturn;

  }

  private static StringBuilder sb = new StringBuilder();
  static {
    // create negative elements
    sb.append("^side$|combx|retweet|mediaarticlerelated|menucontainer|navbar|comment|PopularQuestions");
    sb.append("|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta$|scroll$|shoutbox|sponsor");
    sb.append("|tags|socialnetworking|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools");
    sb.append("|post-attributes|welcome_form|contentTools2|the_answers|remember-tool-tip");
    sb.append("|communitypromo|runaroundLeft|^subscribe");
    sb.append("|vcard|articleheadings|date|^print$|popup");
    sb.append("|author-dropdown|tools|socialtools|byline|konafilter|KonaFilter|breadcrumbs|^fn$|wp-caption-text");
  }
  /**
   * this regex is used to remove undesirable nodes from our doc indicate that
   * something maybe isn't content but more of a comment, footer or some other
   * undesirable node
   */
  private static String regExRemoveNodes = sb.toString();
  private static String queryNaughtyIDs = "[id~=(" + regExRemoveNodes + ")]";
  private static String queryNaughtyClasses = "[class~=(" + regExRemoveNodes
      + ")]";
  private static String queryNaughtyNames = "[name~=(" + regExRemoveNodes
      + ")]";
  private static ReplaceSequence tabsAndNewLinesReplacements = ReplaceSequence
      .create("\n", "\n\n").append("\t").append("^\\s+$");
  /**
   * regex to detect if there are block level elements inside of a div element
   */
  private static Pattern divToPElementsPattern = Pattern
      .compile("<(a|blockquote|dl|div|img|ol|p|pre|table|ul)");

  private static TagsEvaluator blockElemementTags = new TagsEvaluator(
      JavaConversions.asScalaSet(Sets.newHashSet("a", "blockquote", "dl",
          "div", "img", "ol", "p", "pre", "table", "ul")));
  private static TagsEvaluator articleRootTags = new TagsEvaluator(
      JavaConversions.asScalaSet(Sets.newHashSet("div", "span", "article", "pre")));

  private static Pattern captionPattern = Pattern.compile("^caption$");
  private static Pattern googlePattern = Pattern.compile(" google ");
  private static Pattern entriesPattern = Pattern.compile("^[^entry-]more.*$");
  private static Pattern facebookPattern = Pattern.compile("[^-]facebook");
  private static Pattern twitterPattern = Pattern.compile("[^-]twitter");
}
