package p4.geretaxi;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;

public class MarshalDouble implements Marshal {

    @Override
    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, "double", Double.class, this);
    }

    @Override
    public Object readInstance(XmlPullParser parser, String s, String s1, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        return Double.parseDouble(parser.nextText());
    }

    @Override
    public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
        writer.text(obj.toString());
    }
}
