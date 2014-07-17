package br.epusp.pcs.pcsos;

// import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
// import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.ericodummytestapp2.emergencycalls.Emergencycalls;
import com.appspot.ericodummytestapp2.emergencycalls.Emergencycalls.Emergencycall.Insert;
// import com.appspot.ericodummytestapp2.emergencycalls.Emergencycalls.Emergencycall.List;
import com.appspot.ericodummytestapp2.emergencycalls.model.EmergencyCall;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.Arrays;
// import java.util.List;
import java.util.Set;

public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = "MainActivity";
    private MessagesDataAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prevent the keyboard from being visible upon startup.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ListView listView = (ListView) findViewById(R.id.messages_list_view);
        mListAdapter = new MessagesDataAdapter((Application) getApplication());
        listView.setAdapter(mListAdapter);
    }

    /**
     * Simple use of an ArrayAdapter but we're using a static class to ensure no references to the
     * Activity exists.
     */
    static class MessagesDataAdapter extends ArrayAdapter {
        MessagesDataAdapter(Application application) {
            super(application.getApplicationContext(), android.R.layout.simple_list_item_1,
                    application.emergencyCalls);
        }

        void replaceData(EmergencyCall[] messages) {
            clear();
            for (EmergencyCall message : messages) {
                add(message);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);

            EmergencyCall message = (EmergencyCall)this.getItem(position);

            StringBuilder sb = new StringBuilder();

            Set<String> fields = message.keySet();
            boolean firstLoop = true;
            for (String fieldName : fields) {
                // Append next line chars to 2.. loop runs.
                if (firstLoop) {
                    firstLoop = false;
                } else {
                    sb.append("\n");
                }

                sb.append(fieldName)
                        .append(": ")
                        .append(message.get(fieldName));
            }

            view.setText(sb.toString());
            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayEmergencyCall(EmergencyCall... emergencyCalls) {
        String msg;
        if (emergencyCalls==null || emergencyCalls.length < 1) {
            msg = "Emergency call was not present";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Log.d(LOG_TAG, "Displaying " + emergencyCalls.length + " emergency calls.");

            java.util.List<EmergencyCall> emergencyCallList = Arrays.asList(emergencyCalls);
            mListAdapter.replaceData(emergencyCalls);
        }
    }

    public void onClickSendEmergencyCall(View view) {
        View rootView = view.getRootView();

        TextView victimTextInputTV = (TextView)rootView.findViewById(R.id.victim_text_edit_text);
        if (victimTextInputTV.getText()==null ||
                Strings.isNullOrEmpty(victimTextInputTV.getText().toString())) {
            Toast.makeText(this, "Input victim name", Toast.LENGTH_SHORT).show();
            return;
        };

        final String victimTextString = victimTextInputTV.getText().toString();
        // final int greetingCount = Integer.parseInt(greetingCountString);

        TextView messageTextInputTV = (TextView)rootView.findViewById(R.id.message_text_edit_text);
        if (messageTextInputTV.getText()==null ||
                Strings.isNullOrEmpty(messageTextInputTV.getText().toString())) {
            Toast.makeText(this, "Input a help message", Toast.LENGTH_SHORT).show();
            return;
        };

        final String messageMessageString = messageTextInputTV.getText().toString();

        AsyncTask<Void, Void, EmergencyCall> sendGreetings = new AsyncTask<Void, Void, EmergencyCall> () {
            @Override
            protected EmergencyCall doInBackground(Void... unused) {
                // Retrieve service handle.
                Emergencycalls apiServiceHandle = AppConstants.getApiServiceHandle();

                try {
                    EmergencyCall emergencyCall = new EmergencyCall();
                    emergencyCall.setMessage(messageMessageString);
                    emergencyCall.setVictim(victimTextString);

                    Insert insertEmergencyCallCommand = apiServiceHandle.emergencycall().insert(
                            emergencyCall);
                    emergencyCall = insertEmergencyCallCommand.execute();
                    return emergencyCall;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Exception during API call", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(EmergencyCall emergencyCall) {
                if (emergencyCall!=null) {
                    displayEmergencyCall(emergencyCall);
                } else {
                    Log.e(LOG_TAG, "No emergency calls were returned by the API.");
                }
            }
        };

        sendGreetings.execute((Void)null);
    }
}
