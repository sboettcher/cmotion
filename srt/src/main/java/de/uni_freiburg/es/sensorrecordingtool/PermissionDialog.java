package de.uni_freiburg.es.sensorrecordingtool;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/** Just ask for the permission and restart the Recorder, now with hopefully
 * enabled permissions.
 *
 * Created by phil on 2/28/16.
 */
public class PermissionDialog extends Activity{

    private static final int PERMISSION_REQUEST = 1234;

    @Override
    protected void onResume() {
        super.onResume();
        Intent start = getIntent();

        if (!externalStorage(this) || !location(this))  {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                 Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST);
        }
    }

    public static boolean externalStorage(Context c) {
        return ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean location(Context c) {
        return ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);

        for (int res : grantResults)
            if (res != PackageManager.PERMISSION_GRANTED)
                return;

        Intent i = new Intent(this, Recorder.class);
        if (getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());
        startService(i);
        finish();
    }
}
