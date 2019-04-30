package coms486.jsward.platformracer.network;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import coms486.jsward.platformracer.LoginActivity;
import coms486.jsward.platformracer.R;
import coms486.jsward.platformracer.StartupActivity;
import jsward.platformracer.common.network.LoginPacket;

import static jsward.platformracer.common.util.Constants.LOGIN_PORT;
import static jsward.platformracer.common.util.Constants.SERVER_ADDR;

public class SecureLogin {

    private final String DEBUG_TAG = "SECURE_LOGIN";


    private SSLSocketFactory factory;
    private SSLSocket secureSocket;
    private SSLContext sslContext;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private KeyStore keyStore;

    public SecureLogin(Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException {
        //load custom keystore
        keyStore = KeyStore.getInstance("BKS");
        Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.password));
        String password = scanner.nextLine();
        scanner.close();
        keyStore.load(context.getResources().openRawResource(R.raw.clienttruststore), password.toCharArray());

        //trust the keystore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        //get context
        sslContext = SSLContext.getInstance("TLS");

        //init context
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

    }

    public void connect() throws IOException {
        //create the socket
        secureSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(SERVER_ADDR, LOGIN_PORT);
        secureSocket.setKeepAlive(true);

        for(String s:secureSocket.getSupportedCipherSuites())
            Log.d(DEBUG_TAG, s);

        // Get session after the connection is established
        SSLSession sslSession = secureSocket.getSession();

        Log.d(DEBUG_TAG,"SSLSession :");
        Log.d(DEBUG_TAG,"Protocol : "+sslSession.getProtocol());
        Log.d(DEBUG_TAG,"Cipher suite : "+sslSession.getCipherSuite());

        outputStream = new ObjectOutputStream(secureSocket.getOutputStream());
        inputStream = new ObjectInputStream(secureSocket.getInputStream());
        outputStream.flush();
    }

    public LoginPacket authenticate(LoginPacket packet) throws IOException, ClassNotFoundException {
        outputStream.writeUnshared(packet);
        return (LoginPacket) inputStream.readUnshared();
    }



    public void disconnect(){
        try{
            outputStream.close();
        } catch (IOException e) {
        }
        try{
            inputStream.close();
        } catch (IOException e) {
        }
        try{
            secureSocket.close();
        } catch (IOException e) {
        }
    }


}
