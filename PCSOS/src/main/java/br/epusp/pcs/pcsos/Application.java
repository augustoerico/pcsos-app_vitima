package br.epusp.pcs.pcsos;

import com.appspot.ericodummytestapp2.emergencycalls.model.EmergencyCall;
import com.google.api.client.util.Lists;

import java.util.ArrayList;
/**
 * Dummy Application class that can hold static data for use only in sample applications.
 *
 * TODO(developer): Implement a proper data storage technique for your application.
 */
public class Application extends android.app.Application {
    ArrayList<EmergencyCall> emergencyCalls = Lists.newArrayList();
}