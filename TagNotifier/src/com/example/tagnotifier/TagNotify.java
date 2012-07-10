package com.example.tagnotifier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.*;
import android.nfc.*;
import android.nfc.tech.MifareClassic;
import android.util.Log;
import java.lang.Thread;

public class TagNotify extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tag_notify);
        //get information about tag that started the activity
        Intent intent = this.getIntent();
        Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mifare = MifareClassic.get(nfcTag);
        assert(mifare!=null);
        
        //display the information
        String displayMessage = "Tag ID: "+new String(nfcTag.getId())+"\nTag Tech List: ";
        String[] techs = nfcTag.getTechList();
        for(int i=0; i<techs.length; i++){
        	displayMessage += techs[i]+" ";
        }
        displayMessage += "\nSectors: " + mifare.getSectorCount();
        displayMessage += "\nBlocks: " + mifare.getBlockCount();
        displayMessage += "\nPolling card directly...";
        ((TextView)findViewById(R.id.tagInfo)).setText(displayMessage);
        
        
        //poll the card directly
        Thread talker = new Thread(new PollThread((TextView)findViewById(R.id.moreTagInfo), mifare));
        talker.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tag_notify, menu);
        return true;
    }

    private class PollThread implements Runnable{
    	TextView out;
    	String displayMessage;
    	MifareClassic mifare;
    	
    	public PollThread(TextView o, MifareClassic m){
    		displayMessage="";
    		out = o;
    		mifare = m;
    	}
    	
    	public void run(){
    		byte[] key = {0, 0, 0, 0, 0, 0};
    		try{
    		mifare.connect();
    		
    		for(int i = 0; i < mifare.getSectorCount(); i++){
    			if(mifare.authenticateSectorWithKeyA(i, key)){
    				displayMessage += "\nAuthenticated with sector "+i+" with key A=000000";
    			}else if(mifare.authenticateSectorWithKeyB(i, key)){
    				displayMessage += "\nAuthenticated with sector "+i+" with key B=000000";
    			}else{
    				displayMessage += "\nCould not authenticate with sector "+i;
    			}
    			out.setText(displayMessage);
    		}
    		mifare.close();
    		}catch(Exception e){
    			displayMessage+="\nOperation cancelled.";
    			out.setText(displayMessage);
    			return;
    		}
    	}
    }
}
