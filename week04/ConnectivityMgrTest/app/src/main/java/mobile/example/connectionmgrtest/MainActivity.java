package mobile.example.connectionmgrtest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "NetworkStatusExample";

	TextView tvOutput;
	ConnectivityManager connMgr;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvOutput = findViewById(R.id.tvOutput);
        connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);


		// 강의교재 내용을 여기에 작성 후 로그를 확인해볼 것

    }
    
    
    public void onClick(View v) {
    		
    	StringBuilder result = new StringBuilder();
    		
    	switch(v.getId()) {
    	case R.id.btnAllInfo:
    		NetworkInfo[] netInfo = connMgr.getAllNetworkInfo();
    		for (NetworkInfo n : netInfo) {
    			result.append(n.toString() + "\n\n");
    		}
    		break;
    	case R.id.btnActiveInfo:
    		NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();
    		result.append(activeNetInfo);
    		
    		break;
    	}
    	
    	tvOutput.setText(result);
    	
    }
    
    
}
