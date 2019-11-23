package org.dementia.chichi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SET_ALARM;
import static android.Manifest.permission.WAKE_LOCK;

public class AllowCallPermission {
    public String callPerson;
    public String callTime;
    public String callDate;
    public boolean didCall;
    public int AddressCount;
    public ArrayList<String> AddressNames = new ArrayList<String>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_CONTENT_CODE = 100;
    private static final int PERMISSION_RECORD_AUDIO = 300;
    private static final int PERMISSION_STORAGE_CODE = 400;
    public Activity activity;

    // permission 확인
    public boolean checkPermissionCall() {
        int result = ContextCompat.checkSelfPermission(activity, READ_CALL_LOG);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkPermissionContent() {
        int result = ContextCompat.checkSelfPermission(activity, READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkPermissionRecordAudio() {
        int result = ContextCompat.checkSelfPermission(activity, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkPermissionStorage() {
        int result = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    // permission 요청
    public void requestPermissionCall() {
        ActivityCompat.requestPermissions(activity,
                new String[]{READ_CALL_LOG},
                PERMISSION_REQUEST_CODE);
    }
    public void requestPermissionContent() {
        ActivityCompat.requestPermissions(activity,
                new String[]{READ_CONTACTS},
                PERMISSION_CONTENT_CODE);
    }
    public void requestPermissionsRecordAudio() {
        ActivityCompat.requestPermissions(activity,
                new String[]{RECORD_AUDIO},
                PERMISSION_RECORD_AUDIO);
    }
    public void requestPermissionStorage() {
        ActivityCompat.requestPermissions(activity,
                new String[]{READ_EXTERNAL_STORAGE},
                PERMISSION_STORAGE_CODE);
    }

    // CallLog를 반환합니다.
    public void setCallLog() {
        Cursor managedCursor = activity.getBaseContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        managedCursor.moveToNext();
        callPerson = managedCursor.getString(name);
        String callType = managedCursor.getString(type);
        String date_ = managedCursor.getString(date);
        Date callDayTime = new Date(Long.valueOf(date_));
        splitCallDate(callDayTime);
        int dircode = Integer.parseInt(callType);
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                didCall = true;
                break;

            case CallLog.Calls.INCOMING_TYPE:
                didCall = true;
                break;

            case CallLog.Calls.MISSED_TYPE:
                didCall = false;
                break;
        }
        managedCursor.close();
    }

    public void getCallAddress() {
        Cursor phoneCursor = null;
        Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        phoneCursor = activity.getBaseContext().getContentResolver().query(uContactsUri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        AddressCount = phoneCursor.getCount();
        int nameColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        phoneCursor.moveToFirst();
        AddressNames.add(phoneCursor.getString(nameColumn));
        while (phoneCursor.moveToNext()) {
            AddressNames.add(phoneCursor.getString(nameColumn));
        }
    }
    public void splitCallDate(Date date) {
        String[] newdate = date.toString().split(" ");
        callDate = newdate[5] + "-" + changeMonth(newdate[1]) + "-" + newdate[2];
        callTime = newdate[3];
    }

    public String changeMonth(String s) {
        switch (s) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
        }
        return null;
    }
}
