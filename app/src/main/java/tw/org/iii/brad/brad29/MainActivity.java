package tw.org.iii.brad.brad29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uri = Settings.System.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        cr = getContentResolver();
        // content://settings 設定資料
        // content://ContactsContract 聯絡人
        // content://Calllog 通話紀錄
        // content://MediaStore
    }

    public void at1(View view) {
        // SELECT * FROM Settings
        Cursor c = cr.query(uri,null,null,null,null);

//        Log.v("brad","count : " + c.getColumnCount());
//        for(int i=0; i<c.getColumnCount();i++){
//            String field = c.getColumnName(i);
//            Log.v("brad",field);
//        }

        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex("name"));
            String value = c.getString(c.getColumnIndex("value"));
            Log.v("brad",name + " x " + value);                 //取得系統設定值
        }
        c.close();                                                //資料庫有開有關
    }

    public void at2(View view) {
//        Cursor c = cr.query(uri,null,"name = ?",
//                new String[]{Settings.System.SCREEN_BRIGHTNESS},
//                null);
//        c.moveToNext();
//        String v = c.getString(c.getColumnIndex("value"));
//        Log.v("brad","v = "+ v);
//
//        c.close();

        try {
            int v = Settings.System.getInt(cr,Settings.System.SCREEN_BRIGHTNESS);
            Log.v("brad","v = "+ v);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void at3(View view) {
        uri = ContactsContract.Contacts.CONTENT_URI;

        Cursor c = cr.query(uri, null, null, null, null);
        Log.v("brad", "count : " + c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++) {
            String field = c.getColumnName(i);
            Log.v("brad", field);
        }

//        while(c.moveToNext()){
//            String name = c.getString(c.getColumnIndex("display_name"));
//            Log.v("brad",name);
//        }

        c.close();
    }

    public void at4(View view) {
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor c = cr.query(uri, null, null, null, null);
        Log.v("brad", "count : " + c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++) {
            String field = c.getColumnName(i);
            Log.v("brad", field);
        }

        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex("display_name"));
            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.v("brad",name + ":" + phone);
        }

        Log.v("brad","count : " + c.getCount());
        c.close();
    }

    public void at5(View view) {
        // CallLog.Calls.CONTENT_URI
        uri = CallLog.Calls.CONTENT_URI;
        // CallLog.Calls.CACHED_NAME
        // CallLog.Calls.NUMBER
        // CallLog.Calls.TYPE => INCOMING, OUTGOING, MISSED
        // CallLog.Calls.DATE
        // CallLog.Calls.DURATION  以秒為單位
        Cursor c = cr.query(uri, null, null, null, null);

        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String type = "";
            if(CallLog.Calls.INCOMING_TYPE == c.getType(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "in";
            }else if(CallLog.Calls.OUTGOING_TYPE == c.getType(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "out";
            }else if(CallLog.Calls.MISSED_TYPE == c.getType(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "missed";
            }

            Log.v("brad",type + ":" + name + ":" + num);
        }


        Log.v("brad","count : " + c.getCount());
    }
}
