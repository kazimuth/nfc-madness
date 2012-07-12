package harry.gilles.activetagnotifier;

//shell class to handle active connections

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.widget.*;
import android.nfc.*;
import android.nfc.tech.*;

public class MainActivity extends Activity {
	private NfcAdapter adapter;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = ((NfcManager) getSystemService(NFC_SERVICE)).getDefaultAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }    
    
    @Override
    public void onNewIntent(Intent intent) {
    	//do stuff
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	adapter.enableForegroundDispatch(this, null, null, null);
    }
    @Override
    public void onPause() {
    	super.onPause();
    	adapter.disableForegroundDispatch(this);
    }
    
}
