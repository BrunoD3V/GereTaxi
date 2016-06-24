package p4.geretaxi;

public class SoapHandler {

    private static String NAMESPACE;
    private static String URL;
    private static String METHOD_NAME;
    private static String SOAP_ACTION;
    private static String IP = "192.168.1.13";

    public SoapHandler(String methodName){
        NAMESPACE = "http://GereTaxiPackage/";
        URL = "http://"+IP+":8080/GereTaxi/WSGereTaxi";
        METHOD_NAME = methodName;
        SOAP_ACTION = NAMESPACE+METHOD_NAME;
    }

    public String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        SoapHandler.IP = IP;
    }

    public String getMethodName() {
        return METHOD_NAME;
    }

    public static void setMethodName(String methodName) {
        METHOD_NAME = methodName;
    }

    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public static void setNAMESPACE(String NAMESPACE) {
        SoapHandler.NAMESPACE = NAMESPACE;
    }

    public String getSoapAction() {
        return SOAP_ACTION;
    }

    public static void setSoapAction(String soapAction) {
        SOAP_ACTION = soapAction;
    }

    public String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        SoapHandler.URL = URL;
    }
}
