package generation;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.BasicConfigurator;
import representation.Group;
import representation.SectionGrouping;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Makes use of the Apache FOP library to generate XSL-FO trees from internal objects and then generate PDF output files.
 * <p>
 * Created by Steve on 6/21/2015.
 */
public class PDFGenerator {

    public static final String TEMP_PDF_LOCATION = "tmp/pdf/";
    public static final String PDF = ".pdf";

    public static void generateGroupPDF(Group group) {
        generatePDF(group, new File("src/generation/templates/group.xsl"));
    }

    public static void generateSectionGroupingPDF(SectionGrouping sectionGrouping) {
        generatePDF(sectionGrouping, new File("src/generation/templates/sectiongrouping.xsl"));
    }

    private static void generatePDF(Object object, File xslFile) {
        try {
            String outputFilePath = TEMP_PDF_LOCATION + object.getClass().getCanonicalName() + "_" + System.currentTimeMillis() + PDF;
            File outputFile = new File(outputFilePath);
            outputFile.createNewFile();
            OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));

            BasicConfigurator.configure();
            FopFactory fopFactory = FopFactory.newInstance(outputFile.getParentFile().toURI());
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, output);

            File xmlRepresentation = XMLSerializer.serializeObject(object);
            xmlRepresentation.deleteOnExit();
            StreamSource source = new StreamSource(xmlRepresentation);

            StreamSource transform = new StreamSource(xslFile);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(transform);

            Result finalResult = new SAXResult(fop.getDefaultHandler());
            transformer.transform(source, finalResult);
            output.close();
        } catch (TransformerException e) {
// TODO: sane error handling
            e.printStackTrace();
        } catch (IOException e) {
// TODO: sane error handling
            e.printStackTrace();
        } catch (FOPException e) {
// TODO: sane error handling
            e.printStackTrace();
        }
    }
}
