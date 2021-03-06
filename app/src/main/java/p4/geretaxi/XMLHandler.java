package p4.geretaxi;

import android.location.Location;
import android.os.Environment;
import android.util.Xml;

import com.google.maps.model.LatLng;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XMLHandler {
    private String text;

    public boolean writeGPSCoordinates(Location location, String Processo) {
        boolean result = false;
        File file = new File(Environment.getExternalStorageDirectory(), Processo+".xml");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            if (file.length() == 0) {
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                xmlSerializer.startTag(null, "wpt");
                xmlSerializer.attribute(null, "lat", String.valueOf(location.getLatitude()));
                xmlSerializer.attribute(null, "lon", String.valueOf(location.getLongitude()));
                xmlSerializer.endTag(null, "wpt");
                xmlSerializer.endDocument();
                xmlSerializer.flush();



            } else {
                xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                xmlSerializer.startTag(null, "wpt");
                xmlSerializer.attribute(null, "lat", String.valueOf(location.getLatitude()));
                xmlSerializer.attribute(null, "lon", String.valueOf(location.getLongitude()));
                xmlSerializer.endTag(null, "wpt");
                xmlSerializer.endDocument();
            }
            String dataWriter = writer.toString();
            fileOutputStream.write(dataWriter.getBytes());
            result = true;
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return result;
    }



    public boolean writeServico(Servico servico) {
        boolean result = false;
        File file = new File(Environment.getExternalStorageDirectory(), "servicos.xml");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            if (file.length() == 0) {
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                xmlSerializer.startTag(null, "servico");
                xmlSerializer.startTag(null , "processo");
                xmlSerializer.text(servico.getProcesso());
                xmlSerializer.endTag(null, "processo");
                xmlSerializer.startTag(null, "data");
                xmlSerializer.text(servico.getData());
                xmlSerializer.endTag(null, "data");
                xmlSerializer.startTag(null, "hora-de-inicio");
                xmlSerializer.text(servico.getHoraDeInicio());
                xmlSerializer.endTag(null, "hora-de-inicio");
                xmlSerializer.startTag(null, "nome-cliente");
                xmlSerializer.text(servico.getNomeCliente());
                xmlSerializer.endTag(null, "nome-cliente");
                xmlSerializer.startTag(null, "numero-passageiros");
                xmlSerializer.text(String.valueOf(servico.getNumPassageiros()));
                xmlSerializer.endTag(null, "numero-passageiros");
                xmlSerializer.startTag(null, "origem");
                xmlSerializer.text(servico.getOrigem());
                xmlSerializer.endTag(null,"origem");
                xmlSerializer.startTag(null, "destino");
                xmlSerializer.text(servico.getDestino());
                xmlSerializer.endTag(null, "destino");
                xmlSerializer.endTag(null, "servico");
                xmlSerializer.endDocument();

            } else {
                if (verificaProcesso(Xml.newPullParser(), servico.getProcesso()))
                    return false;
                xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                xmlSerializer.startTag(null, "servico");
                xmlSerializer.startTag(null , "processo");
                xmlSerializer.text(servico.getProcesso());
                xmlSerializer.endTag(null, "processo");
                xmlSerializer.startTag(null, "data");
                xmlSerializer.text(servico.getData());
                xmlSerializer.endTag(null, "data");
                xmlSerializer.startTag(null, "hora-de-inicio");
                xmlSerializer.text(servico.getHoraDeInicio());
                xmlSerializer.endTag(null, "hora-de-inicio");
                xmlSerializer.startTag(null, "nome-cliente");
                xmlSerializer.text(servico.getNomeCliente());
                xmlSerializer.endTag(null, "nome-cliente");
                xmlSerializer.startTag(null, "numero-passageiros");
                xmlSerializer.text(String.valueOf(servico.getNumPassageiros()));
                xmlSerializer.endTag(null, "numero-passageiros");
                xmlSerializer.startTag(null, "origem");
                xmlSerializer.text(servico.getOrigem());
                xmlSerializer.endTag(null,"origem");
                xmlSerializer.startTag(null, "destino");
                xmlSerializer.text(servico.getDestino());
                xmlSerializer.endTag(null, "destino");
                xmlSerializer.endTag(null, "servico");
                xmlSerializer.endDocument();
            }
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean writeTrajecto (List<LatLng> locations, String processo) {

        File file = new File(Environment.getExternalStorageDirectory(), "Trajecto" + processo + ".xml");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            for (LatLng p: locations) {
                xmlSerializer.startTag(null, Constants.WPT);
                xmlSerializer.attribute(null,Constants.LAT, String.valueOf(p.lat));
                xmlSerializer.attribute(null, Constants.LON, String.valueOf(p.lng));
                xmlSerializer.endTag(null, Constants.WPT);

            }
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
       return false;
    }

    public boolean verificaProcesso(XmlPullParser parser, String processo) {
        boolean result = false;
        File file = new File(Environment.getExternalStorageDirectory(), "servicos.xml");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            parser.setInput(new InputStreamReader(fileInputStream));
            parser.nextTag();
            while(parser.next() != XmlPullParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("processo")) {
                        parser.next();
                        if (parser.getText().equals(processo))
                            return true;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean eraser(String s) {

        boolean result = false;
        File file =new File(Environment.getExternalStorageDirectory(),s+".xml");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write("<?xml version='1.0' standalone='yes' ?>".getBytes());
            result = true;
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<LatLng> loadGpxData(XmlPullParser parser, String processo)
    {


        List<LatLng> latLngs = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory(), processo+".xml");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            parser.setInput(new InputStreamReader(fileInputStream));
            parser.nextTag();

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals("wpt")) {
                    // Save the discovered lat/lon attributes in each <wpt>
                    latLngs.add(new LatLng(
                            Double.valueOf(parser.getAttributeValue(null, "lat")),
                            Double.valueOf(parser.getAttributeValue(null, "lon"))));
                }
                // Otherwise, skip irrelevant data
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return latLngs;
    }

    public double parseDistance(String data) throws IOException, XPathExpressionException {
        Double distance = 0.0;
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        InputStream inputStream = IOUtils.toInputStream(data, "UTF-8");
        InputSource inputXML = new InputSource(inputStream);

        NodeList nodes = (NodeList) xPath.evaluate("/DirectionsResponse/route/leg/distance/value", inputXML, XPathConstants.NODESET);
        distance=Double.parseDouble(nodes.item(0).getTextContent());



        return distance;
    }
    public boolean getPortagem(XmlPullParser parser, String data) throws IOException, XmlPullParserException {

        String text;
        InputStream inputStream = IOUtils.toInputStream(data, "UTF-8");
        parser.setInput(new InputStreamReader(inputStream));
        parser.nextTag();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals("html_instructions")) {
                    parser.next();
                    text = parser.getText();
                    if (text.contains("portagem") || text.contains("toll")){
                        return true;
                    }
                }
            }


        }
        return false;
    }

    public List<LatLng> parseDirections(XmlPullParser parser, String data) throws XmlPullParserException, IOException {
        List<LatLng> latLngs = new ArrayList<>();   // List<> as we need subList for paging later
        String points;


        InputStream inputStream = IOUtils.toInputStream(data, "UTF-8");
        parser.setInput(new InputStreamReader(inputStream));
        parser.nextTag();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if(parser.getEventType() == XmlPullParser.START_TAG){
                if(parser.getName().equals("overview_polyline")){
                    parser.nextTag();
                    if(parser.getName().equals("points")){
                        parser.next();
                        points = parser.getText();

                        latLngs = decodePolyLine(points);
                    }
                }
            }

        }

        return latLngs;
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
