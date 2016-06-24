package p4.geretaxi;

public class SoapHandler {

    private static String NAMESPACE;
    private static String URL;
    private static String METHOD_NAME;
    private static String SOAP_ACTION;
    private static String SERVER_IP = "192.168.1.5";

    public SoapHandler(String methodName){
        NAMESPACE = "http://GereTaxiPackage/";
        URL = "http://"+SERVER_IP+":8080/GereTaxi/WSGereTaxi";
        METHOD_NAME = methodName;
        SOAP_ACTION = "http://GereTaxiPackage/inserirServico";
    }

    public String getIP() {
        return SERVER_IP;
    }

    public static void setIP(String IP) {
        SoapHandler.SERVER_IP = IP;
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

    public String getSoapAction() {
        return SOAP_ACTION;
    }

    public String getURL() {
        return URL;
    }

}
