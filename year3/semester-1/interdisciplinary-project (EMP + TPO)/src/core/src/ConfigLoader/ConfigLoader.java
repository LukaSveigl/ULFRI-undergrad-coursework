package ConfigLoader;

import Utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A config file loader.
 */
public class ConfigLoader {
    /** The database configuration. */
    private DBData data;
    /** The configuration path. */
    private final String configPath = "config.xml";
    /** The logger. */
    private final Logger logger;

    private boolean loggingEnabled;

    /**
     * Constructs a ConfigLoader instance.
     */
    public ConfigLoader(boolean log) {
        this.data = null;
        this.logger = new Logger(log);
        this.loggingEnabled = log;
    }

    /**
     * Loads the data from the config xml file.
     */
    public void load() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(this.configPath));
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("schema");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element)node;
                    this.data = new DBData(
                            element.getAttribute("id"),
                            element.getElementsByTagName("ip").item(0).getTextContent(),
                            element.getElementsByTagName("port").item(0).getTextContent()
                    );

                    // The config should always contain only 1 database connection data.
                    break;
                }
            }
        } catch (Exception e) {
            if (!this.isWindows() || !this.loggingEnabled) {
                this.data = new DBData(
                        "TPO",
                        "db-mysql-fra1-30802-do-user-12793213-0.b.db.ondigitalocean.com",
                        "25060"
                );
            }
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Fetches the config data.
     * @return DBData - The config data stored internally.
     */
    public DBData fetchData() {
        return this.data;
    }

    /**
     * Checks if the current OS is Windows. This is a workaround needed for Android development.
     * @return Boolean - True if the OS is Windows, false otherwise.
     */
    private boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win" ));
    }
}
