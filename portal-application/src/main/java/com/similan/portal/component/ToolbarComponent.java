package com.similan.portal.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.graphicimage.GraphicImage;

@FacesComponent("mytoolbar")
public class ToolbarComponent extends UINamingContainer {

	private final String cssName = "highlightableElement";
	private final String cssBorderSpaceName = "borderSpaces";
	private final String cssNameForWrapper = "highlightableWrapper";

	@Override
	public String getFamily() {
		return "toolbar";
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", null);
		writer.writeAttribute("class", cssNameForWrapper, "styleClass");
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.endElement("div");
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		for (UIComponent child : getChildren()) {
			if (child instanceof GraphicImage) {
				changeGraphicImageStyle((GraphicImage) child);
			} else if (child instanceof UIPanel) {
				searchGraphicImagesInPanel((UIPanel) child);
			}
			
			child.encodeAll(context);
		}
	}

	private void searchGraphicImagesInPanel(UIPanel child) {
		for (UIComponent panelItem : child.getChildren()) {
			if (panelItem instanceof GraphicImage) {
				changeGraphicImageStyle((GraphicImage) panelItem);
			} else if (panelItem instanceof UIPanel) {
				searchGraphicImagesInPanel((UIPanel) panelItem);
			}
		}
	}

	private void changeGraphicImageStyle(GraphicImage graphic) {
		if (doesNotHaveHighlightStyle(graphic)) {
			addStyle(graphic);
		}
	}

	private void addStyle(GraphicImage graphic) {
		graphic.setStyleClass(graphic.getStyleClass() == null ? getCssName()
				: graphic.getStyleClass() + " " + getCssName());
	}

	private String getCssName() {
		return cssName + " " + cssBorderSpaceName;
	}

	private boolean doesNotHaveHighlightStyle(GraphicImage graphic) {
		return graphic.getStyleClass() == null
				|| !graphic.getStyleClass().contains(cssName);
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

}
