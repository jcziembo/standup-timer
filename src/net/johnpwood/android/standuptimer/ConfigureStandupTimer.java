package net.johnpwood.android.standuptimer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigureStandupTimer extends Activity implements OnClickListener {
    private static final String MEETING_LENGTH_POS = "meetingLengthPos";
    private static final String NUMBER_OF_PARTICIPANTS = "numberOfParticipants";

    private int meetingLengthPos = 0;
    private int numParticipants = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeGUIElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            startActivity(new Intent(this, About.class));
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        Intent i = new Intent(this, StandupTimer.class);

        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        meetingLengthPos = s.getSelectedItemPosition();
        i.putExtra("meetingLengthPos", meetingLengthPos);

        TextView t = (TextView) findViewById(R.id.num_participants);
        int numParticipants = Integer.parseInt(t.getText().toString());
        i.putExtra("numParticipants", numParticipants);

        if (numParticipants > 0) {
            saveState();
            startActivity(i);
        }
    }

    private void initializeGUIElements() {
        loadState();
        initializeNumberOfParticipants();
        initializeMeetingLengthSpinner();
        initializeStartButton();
    }

    private void initializeNumberOfParticipants() {
        TextView t = (TextView) findViewById(R.id.num_participants);
        t.setText("" + numParticipants);
    }

    private void initializeStartButton() {
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    private void initializeMeetingLengthSpinner() {
        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.meeting_lengths,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(meetingLengthPos);
    }

    private void saveState() {
        Logger.i("Saving state.  mettingLengthPos = " + meetingLengthPos + ", numParticipants = " + numParticipants);
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(MEETING_LENGTH_POS, meetingLengthPos);
        preferences.putInt(NUMBER_OF_PARTICIPANTS, numParticipants);
        preferences.commit();
    }

    private void loadState() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        meetingLengthPos = preferences.getInt(MEETING_LENGTH_POS, 0);
        numParticipants = preferences.getInt(NUMBER_OF_PARTICIPANTS, 2);
        Logger.i("Retrieved state.  mettingLengthPos = " + meetingLengthPos + ", numParticipants = " + numParticipants);
    }
}
