package com.example.tmnt.uploadphoto;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class HttpUtil {

    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String WSDL = "http://114.215.202.209/PropertyManageWebSolution/WebAPI.asmx";

    public static String RequestWebService(String method, Map<String, Object> params) {
        String result = "";
        SoapObject request = new SoapObject(NAMESPACE, method);
        if (params != null) {
            for (String key : params.keySet()) {
                request.addProperty(key, params.get(key));
            }
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(WSDL, 10000);
        try {
            ht.call(NAMESPACE + method, envelope);
            if (envelope.getResponse() != null) {
                result = envelope.getResponse().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Net_Error";
        }
        return result;
    }


    public static String ShiwuFabuService(String string, Object object) {
        // TODO Auto-generated method stub
        return null;
    }
}
