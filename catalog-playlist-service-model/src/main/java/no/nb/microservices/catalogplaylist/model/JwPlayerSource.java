package no.nb.microservices.catalogplaylist.model;

import javax.xml.bind.annotation.XmlAttribute;

public class JwPlayerSource {
    private String file;

    @XmlAttribute
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
