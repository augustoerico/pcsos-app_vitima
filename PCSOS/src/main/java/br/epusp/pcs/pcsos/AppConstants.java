package br.epusp.pcs.pcsos;

/**
 * Created by kiko on 15/07/14.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.appspot.ericodummytestapp2.emergencycalls.Emergencycalls;
import javax.annotation.Nullable;

public class AppConstants {

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    /**
     * Class instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


    /**
     * Retrieve a Helloworld api service handle to access the API.
     */
    public static Emergencycalls getApiServiceHandle() {
        // Use a builder to help formulate the API request.
        Emergencycalls.Builder helloWorld = new Emergencycalls.Builder(AppConstants.HTTP_TRANSPORT,
                AppConstants.JSON_FACTORY,null);

        return helloWorld.build();
    }

}