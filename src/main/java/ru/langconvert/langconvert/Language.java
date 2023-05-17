package ru.langconvert.langconvert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.langconvert.langconvert.FileUtils.FileName;

public class Language 
{
    public final String id;
    public final String name;
    public final String fileExtension;
    public final String editorUrl;
    public final List<String> prismLangs;

    private Language(String id, String name, String fileExtension, String editorUrl, String[] prismLangs)
    {
        this.id = id;
        this.name = name;
        this.editorUrl = editorUrl;
        this.prismLangs = Collections.unmodifiableList(Arrays.asList(prismLangs));
        
        if (fileExtension.isEmpty())
            throw new RuntimeException("Empty language file extension");

        this.fileExtension = fileExtension;
    }

    public Language(File xml) throws SAXException, IOException, ParserConfigurationException
    {
        FileName filename = FileUtils.splitName(xml.getName());
        if (!filename.extension.equals("xml"))
            throw new IOException("Wrong file extension");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
        
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(xml);

        doc.getDocumentElement().normalize();

        this.id = filename.path;

        this.name = contentOf(
            doc.getElementsByTagName("name"),
            "XML file error. Unknown language id."
        );

        String extension = contentOf(
            doc.getElementsByTagName("extension"),
            null
        );

        this.fileExtension = extension == null ? this.id : extension;

        this.editorUrl = contentOf(
            doc.getElementsByTagName("editor"),
            null
        );

        List<String> pl = new ArrayList<>();

        Node prism = lastOf(doc.getElementsByTagName("prism"));
        if (prism != null)
        {
            boolean useId = Boolean.parseBoolean( ((Element)prism).getAttribute("useId") );
            if (useId)
            {
                pl.add(this.id);
            }

            NodeList prismList = prism.getChildNodes();
            for (int j = 0; j < prismList.getLength(); j++)
            {
                Node module = prismList.item(j);
                if (module.getNodeName().equals("module"))
                {
                    pl.add(module.getTextContent());
                }
            }
        }
        else
        {
            pl.add(this.id);
        }

        this.prismLangs = Collections.unmodifiableList(pl);
    }

    public static Language readFromXML(File file)
    {
        if (file == null)
            return null;

        try
        {
            return new Language(file);
        }
        catch (Exception e)
        {
            MainController.LOGGER.error("Language file \"" + file.getName() + "\" read error: " + e.getMessage());
            return null;
        }
    }

    private static Node lastOf(NodeList nodes)
    {
        if (nodes.getLength() == 0)
            return null;
        return nodes.item(nodes.getLength()-1);
    }

    private static String contentOf(NodeList nodes, String exceptionMessage)
    {
        Node node = lastOf(nodes);
        if (exceptionMessage != null)
            Utils.throwIfNull(node, exceptionMessage);
        else
            if (node == null)
                return null;
        return node.getTextContent();
    }

    public String toHTMLSelectorValue()
    {
        return String.format("<custom-option value = %s>%s</custom-option>", this.id, this.name);
    }

    public String toHTMLPrismScript()
    {
        return String.format("<script src=\"https://unpkg.com/prismjs@1.29.0/components/prism-%s.js\"></script>", this.id);
    }

    public static final class Builder
    {
        private final String id;
        private String name = null;
        private String fileExtension = null;
        private String editorUrl = null;
        private String[] prismDependencies = new String[0];
        private boolean prismDisabled = false;

        public Builder(String id)
        {
            this.id = id;
            this.fileExtension = id;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder extension(String extension)
        {
            this.fileExtension = extension;
            return this;
        }

        public Builder editorUrl(String url)
        {
            this.editorUrl = url;
            return this;
        }

        public Builder prismDependencies(String... dependencies)
        {
            this.prismDependencies = dependencies;
            return this;
        }

        public Builder disablePrism()
        {
            this.prismDisabled = true;
            return this;
        }

        public Language build()
        {
            List<String> prismLangs = new ArrayList<>();
            if (!this.prismDisabled)
            {
                prismLangs.add(this.id);
                for (String lang : this.prismDependencies)
                {
                    prismLangs.add(lang);
                }
            }

            return new Language(this.id, 
                        Utils.throwIfNull(name, "Language name is null"), 
                        Utils.throwIfNull(fileExtension, "Language file name is null"), 
                        this.editorUrl,
                        prismLangs.toArray(new String[0])
                    );
        }
    }
}
