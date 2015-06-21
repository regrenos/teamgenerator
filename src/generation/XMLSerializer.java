package generation;

import representation.Group;
import representation.Student;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Uses the Java JAXB package to serialize objects to XML as a precursor for PDF generation.
 * Created by Steve on 6/21/2015.
 */
public class XMLSerializer {

    /**
     * Serialize an object using the JAXB marshaller to XML on disk.
     * @param object Object to serialize
     * @return the file that was saved
     */
    public static File serializeObject(Object object) {
        String outputFileName = "tmp/xml/" + object.getClass().getCanonicalName() + "_" + System.currentTimeMillis() + ".xml";
        File outputFile = new File(outputFileName);
        try {
            outputFile.createNewFile();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, outputFile);
        } catch (IOException e) {
            // TODO: communicate to front-end about failure - this should never fail, though
            e.printStackTrace();
        } catch (JAXBException e) {
            // TODO: communicate to front-end about failure
            e.printStackTrace();
        }

        return outputFile;
    }
}
