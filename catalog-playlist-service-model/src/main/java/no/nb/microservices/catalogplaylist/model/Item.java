package no.nb.microservices.catalogplaylist.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"guid", "title", "description", "altTitle", "jwPlayerSource", "image"})
public class Item {
    private String guid;
    private String title;
    private String altTitle;
    private String description;
    private JwPlayerSource jwPlayerSource;
    private String image;

    @XmlElement
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(namespace = "http://www.nb.no/playlist/rss")
    public String getAltTitle() {
        return altTitle;
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "source", namespace="http://rss.jwpcdn.com/")
    public JwPlayerSource getJwPlayerSource() {
        return jwPlayerSource;
    }

    public void setJwPlayerSource(JwPlayerSource jwPlayerSource) {
        this.jwPlayerSource = jwPlayerSource;
    }

    @XmlElement(namespace="http://rss.jwpcdn.com/")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
