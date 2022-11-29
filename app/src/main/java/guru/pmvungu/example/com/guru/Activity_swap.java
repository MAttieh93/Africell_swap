package guru.pmvungu.example.com.guru;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import guru.pmvungu.example.com.includes.base64;
import guru.pmvungu.example.com.includes.clsfunction;

import static guru.pmvungu.example.com.includes.apiUrl.baseUrl;
import static guru.pmvungu.example.com.includes.apiUrl.baseUrlswap;
import static guru.pmvungu.example.com.includes.apiUrl.getAntenne;
import static guru.pmvungu.example.com.includes.apiUrl.getToken;
import static guru.pmvungu.example.com.includes.apiUrl.getUserId;

public class Activity_swap extends AppCompatActivity {

    final DBAdapter db = new DBAdapter(Activity_swap.this);
    private String strFinalXML;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


   // public static final String EXTRA_MESSAGE = "sl.Africell.afr_sl_registration.MESSAGE";
    private static final int CAMERA_PIC_REQUEST_PersonalPhoto = 1111;
    private static final int CAMERA_PIC_REQUEST_IdCardSide1 = 1112;
    private static final int CAMERA_PIC_REQUEST_IdCardSide2 = 1113;
    private static final int CAMERA_PIC_REQUEST_FingerPrint = 1114;
    private static final int SCAN_SIM_BAR_CODE = 49374;

    String strURLInsertSwapSIM="";

    private ImageView mIdCardSide1;
    private String strIdCardSide1="";
    private Uri Side1Uri;
    private String strSide1URL;

    libClass libclass;

    String pathUrl = "https://localhost:81/" ;
    String txtURLIP,txtPort;
    String  _ipserver, _port;
    String _msisdn,_iccid,_firstname,_lastname,_nickname,_gender,dob,_street,_email,_idtype,_idnumber,
    _msisdn1,_msisdn2,_msisdn3,_msisdn4,_msisdn5,_userId,_apitoken;
    Button validateButton;
    ImageButton btnBirthDatePick;

    TextView lblBirthDateValue;

     clsfunction clsfunction = new clsfunction(this);

   // final DBAdapter db = new DBAdapter(Activity_request.this);
  //String[] strmotif = ";Depot espece;Retrait espece;Releve de transaction".split(";");///{"", "India", "USA", "China", "Japan", "Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);

        //Spinner spinnerNDC =  (Spinner) findViewById(R.id.spinnerNDC);
        _ipserver ="";   // default IP server
        _port=""; //default port 80 or 81
        strIdCardSide1="";
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);
        _userId=getUserId();
        getIpServer();
        clsfunction.sFillCBONDC((Spinner) findViewById(R.id.spinnerNDC));
        clsfunction.sFillCBOGender((Spinner) findViewById(R.id.spinnerGender));
        clsfunction.sFillCBOIdType((Spinner) findViewById(R.id.stypeId));
       //setupActionBar();

        //Calendar CurrentDateTime = Calendar.getInstance();
        //TextView lblBirthDateValue = (TextView) findViewById(R.id.lblBirthDateValue);

        isEnabledSIM(true);
       // isEnabledPersonal(false);
        isEnabledID(false);

       /* val button: Button = findViewById(R.id.button)
        val img: Drawable = button.context.resources.getDrawable(R.drawable.ic_baseline_alarm_24)
        button.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
       */
       final  EditText txtMSISDN3 = (EditText) findViewById(R.id.txtMSISDN3);
       // String _msisdn3=txtMSISDN3.getText().toString();
        txtMSISDN3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (txtMSISDN3.getText().toString().trim().length() > 0) {
                        isValidatedID();

                    }
                }
            }
        });


    }
    public void onNextClick(View view){
        if (isValidatedSIM()){
            isEnabledSIM(false);
            isEnabledID(true);
        }
        return;

    }
    public Boolean isValidatedSIM(){
       TextView lblError = (TextView) findViewById(R.id.lblError);
       lblError.setText("");

        EditText txtMSISDN = (EditText) findViewById(R.id.txtMSISDN);
        _msisdn = txtMSISDN.getText().toString();

    if (_msisdn.length() != 7){
        Toast.makeText(this, "The number you are trying to register doesnt exist.", Toast.LENGTH_LONG).show();
        lblError.setText("The number you are trying to register doesnt exist.");
        txtMSISDN.findFocus();
        return false;
    }

    EditText txtICC = (EditText) findViewById(R.id.txtICC);
    _iccid = txtICC.getText().toString();
        if (_iccid.trim().equals("")){
            Toast.makeText(this, "The ICCID doesn't exist.", Toast.LENGTH_LONG).show();
            lblError.setText("The ICCID doesn't exist.");
            txtICC.findFocus();
            return false;
        }

    String _IMSI = "";
    if (!_iccid.trim().equals("")){

			switch (_iccid.length()){
            case 10 :
                _IMSI = "630900"  +_iccid.substring(0, 1)+ _iccid.substring(2,10);
                break;
            case 15:
                if (!_iccid.substring(0, 6).equals("630900")){
                    Toast.makeText(this, "IMSI Format must be like 630900XXXXXXXXXX", Toast.LENGTH_LONG).show();
                    lblError.setText("IMSI Format must be like 630900XXXXXXXXXX.");
                    return false;
                }
                _IMSI=_iccid;
                break;
            default:
                Toast.makeText(this, "IMSI doesn't exist.", Toast.LENGTH_LONG).show();
                lblError.setText("IMSI doesn't exist.");
                return false;
        }
    }
  _iccid= _IMSI;
   if (!_iccid.substring(0, 6).equals("630900")){
            Toast.makeText(this, "IMSI Format must be like 630900XXXXXXXXXX", Toast.LENGTH_LONG).show();
            lblError.setText("IMSI Format must be like 630900XXXXXXXXXX.");
            return false;
   }


    Spinner spinnerNDC = (Spinner) findViewById(R.id.spinnerNDC);
   // EditText txtMSISDN = (EditText) findViewById(R.id.txtMSISDN);

    String _SN = txtMSISDN.getText().toString().trim();
    String _NDC = spinnerNDC.getSelectedItem().toString().trim();

    if (!_SN.trim().equals("")){
        if (_SN.length() != 7){
            Toast.makeText(this, "MSISDN doesnt exist.", Toast.LENGTH_LONG).show();
            lblError.setText("MSISDN doesnt exist.");
            return false;
        }
        _msisdn = "243" + _NDC.substring(1, 3) + _SN;
    }

   //get and check first Name
        EditText txtFirstName = (EditText) findViewById(R.id.txtFirstName);
          _firstname = txtFirstName.getText().toString();

        if (_firstname.equals("")){
            Toast.makeText(this, "veuillez saisir le  nom.", Toast.LENGTH_LONG).show();
            lblError.setText("veuillez saisir le  nom.");
            txtFirstName.findFocus();
            return false;
        }
        else
        {
            if (_firstname.length() < 2){
                Toast.makeText(this, "veuillez saisir le  nom.", Toast.LENGTH_LONG).show();
                lblError.setText("veuillez saisir le  nom.");
                return false;
            }
        }
        //get and check the last name
        EditText txtLastName = (EditText) findViewById(R.id.txtLastName);
          _lastname = txtLastName.getText().toString();

        if (_lastname.equals("")){
            Toast.makeText(this, "Veuillez saisir le Postnom.", Toast.LENGTH_LONG).show();
            lblError.setText("Veuillez saisir le Postnom.");
            txtLastName.findFocus();
            return false;
        }
        else
        {
            if (_lastname.length() < 2){
                Toast.makeText(this, "Veuillez saisir le Postnom valide.", Toast.LENGTH_LONG).show();
                lblError.setText("Veuillez saisir le Postnom valide.");
                txtLastName.findFocus();
                return false;
            }
        }

        EditText txtFatherName = (EditText) findViewById(R.id.txtFatherName);
          _nickname = txtFatherName.getText().toString();

        //get gender
        Spinner spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        _gender = spinnerGender.getSelectedItem().toString();

        if (_gender.equals("")){
            Toast.makeText(this, "Veuillez choisir le sexe.", Toast.LENGTH_LONG).show();
            lblError.setText("Veuillez choisir le sexe.");
            spinnerGender.findFocus();
            return false;
        }

        //get birth Date
        TextView lblBirthDateValue = (TextView) findViewById(R.id.lblBirthDateValue);
          dob = lblBirthDateValue.getText().toString();

        String[]tBirthday=dob.split("/");
        int iYear= Calendar.getInstance().get(Calendar.YEAR);
 
        if (dob.equals("")){
            Toast.makeText(this, "veuillez entrer la date de Naissance valide.", Toast.LENGTH_LONG).show();
            lblError.setText("veuillez entrer la date de Naissance valide.");
            lblBirthDateValue.findFocus();
            return false;
        }else
        if (tBirthday.length > 0 && (iYear-Integer.parseInt(tBirthday[2]) < 17)){
            Toast.makeText(this, "La date de Naissance entrée est invalide.", Toast.LENGTH_LONG).show();
            lblError.setText("La date de Naissance entrée est invalide.");
            lblBirthDateValue.findFocus();
            return false;
        }

        EditText txtAdress = (EditText) findViewById(R.id.txtStreet);
        _street = txtAdress.getText().toString();
        //get email
        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
          _email = txtEmail.getText().toString();

        //get education
        Spinner spIdtype = (Spinner) findViewById(R.id.stypeId);
          _idtype = spIdtype.getSelectedItem().toString();

        if (_idtype.equals("")){
            Toast.makeText(this, "Selectionner le type de Doc.", Toast.LENGTH_LONG).show();
            lblError.setText("Selectionner le type de Doc.");
            spIdtype.findFocus();
            return false;
        }

        EditText txtnumberId = (EditText) findViewById(R.id.txtIdnumber);
          _idnumber = txtnumberId.getText().toString();

        if (_idnumber.equals("")){
            Toast.makeText(this, "Veuillez entrer la reference Doc.", Toast.LENGTH_LONG).show();
            lblError.setText("Veuillez entrer la reference Doc.");
            txtnumberId.findFocus();
            return false;
        }
        if (strIdCardSide1.equals("")){
            Toast.makeText(this, "Veuillez prendre la photo de card ID", Toast.LENGTH_LONG).show();
            lblError.setText("Veuillez prendre la photo de card ID");
            return false;
        }

    return true ;
}
    public void onValidateClick(View view){

    if (isValidatedSIM()){
        // check refernce info
        if (isValidatedID()){
         //call uploadTask
         //   Toast.makeText(this, "call uploadTask.", Toast.LENGTH_LONG).show();
              onUploaded(); //https
            //onUploadClick(); //http ip static
        }
        return;

    }
   else{
        //back to sim info form
        isEnabledSIM(true);
        isEnabledID(false);

    }

}
    private boolean isNetworkAvailable() {
        try{
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }catch (Exception ex){
            TextView lblError = (TextView) findViewById(R.id.lblError);
            lblError.setText(ex.getMessage());
            return false;
        }

    }
    public  void onUploaded(){
//try {
    TextView lblError = (TextView) findViewById(R.id.lblError);
    boolean isConnected = isNetworkAvailable();
    if (isConnected) {
        lblError.setText("");
    } else {
        lblError.setText("You are not connected to the internet");
        clsfunction.popMessage("No Internet Connection", "You are not connected to the internet.");
        return;
    }

/*
    _apitoken=getToken();

    strURLInsertSwapSIM = baseUrlswap + "onInsertdata";
    new ImageUploadTaskAsync().execute(_msisdn, _iccid, _firstname,
            _lastname, _nickname, dob, _gender, _street, _email, _idtype, _idnumber, strIdCardSide1,
            _msisdn1, _msisdn2, _msisdn3, _msisdn4, _msisdn5, _userId,_apitoken);
}catch (Exception ee){
    Log.e("LOG",ee.getMessage().toString());
    libclass.popMessage("error exception", ee.getMessage());
   return;
}
*/
        _apitoken=getToken();
        strURLInsertSwapSIM=baseUrlswap + "onInsertdata";
        //"http://10.100.21.95:8280/AfricellRegMobileMoney.asmx/onInsertdata";
        new  AsyncInserthttps().execute(_msisdn,_iccid, _firstname,
                _lastname,_nickname,dob,_gender,_street,_email,_idtype,_idnumber,strIdCardSide1,
                _msisdn1,_msisdn2,_msisdn3,_msisdn4,_msisdn5,_userId,_apitoken);

}
    public boolean isValidatedID(){

      TextView lblError = (TextView) findViewById(R.id.lblError);
      lblError.setText("");

      EditText txtMSISDN1 = (EditText) findViewById(R.id.txtMSISDN1);
        _msisdn1 = txtMSISDN1.getText().toString();
        try{
            if (_msisdn1.equals("") || _msisdn1.length() < 9){
                Toast.makeText(this, "Veuillez entrer le Numero reference 1.", Toast.LENGTH_LONG).show();
                lblError.setText("Veuillez entrer le Numero reference 1.");
                txtMSISDN1.findFocus();
                return false;
            }

            EditText txtMSISDN2 = (EditText) findViewById(R.id.txtMSISDN2);
            _msisdn2 = txtMSISDN2.getText().toString();

            if (_msisdn2.equals("") || _msisdn2.length() < 9){
                Toast.makeText(this, "Veuillez entrer le Numero reference 2.", Toast.LENGTH_LONG).show();
                lblError.setText("Veuillez entrer le Numero reference 2.");
                txtMSISDN2.findFocus();
                return false;
            }

            EditText txtMSISDN3 = (EditText) findViewById(R.id.txtMSISDN3);
            _msisdn3 = txtMSISDN3.getText().toString();

            if (_msisdn3.equals("") || _msisdn3.length() < 9 ){
                Toast.makeText(this, "Veuillez entrer le Numero reference 3.", Toast.LENGTH_LONG).show();
                lblError.setText("Veuillez entrer le Numero reference 3.");
                txtMSISDN3.findFocus();
                return false;
            }

            EditText txtMSISDN4 = (EditText) findViewById(R.id.txtMSISDN4);
            _msisdn4 = txtMSISDN4.getText().toString();

            EditText txtMSISDN5 = (EditText) findViewById(R.id.txtMSISDN5);
            _msisdn5 = txtMSISDN5.getText().toString();


            if (_msisdn1.length() >= 9 && _msisdn2.length() >= 9 && _msisdn3.length() >= 9){

                ((Button) findViewById(R.id.validateButton)).setEnabled(true);
            }else{
                ((Button) findViewById(R.id.validateButton)).setEnabled(false);
                return false;
            }

            _msisdn1 = (_msisdn1.length() > 9 || !_msisdn1.equals("")) ?  _msisdn1.substring(_msisdn1.length() - 9):_msisdn1 ;
            _msisdn2 = (_msisdn2.length() > 9 || !_msisdn2.equals("")) ?  _msisdn2.substring(_msisdn2.length() - 9):_msisdn2 ;
            _msisdn3 = (_msisdn3.length() > 9 || !_msisdn3.equals("")) ?  _msisdn3.substring(_msisdn3.length() - 9):_msisdn3 ;

            _msisdn4 = (_msisdn4.length() > 9 || !_msisdn4.equals("")) ?  _msisdn4.substring(_msisdn4.length() - 9):_msisdn4 ;
            _msisdn5 = (_msisdn5.length() > 9 || !_msisdn5.equals("")) ?  _msisdn5.substring(_msisdn5.length() - 9):_msisdn5 ;

            if (_msisdn1.trim().equalsIgnoreCase(_msisdn2.trim())){
                Toast.makeText(this, "le Numero reference 1 et 2 ne peut etre identique", Toast.LENGTH_LONG).show();
                lblError.setText("le Numero reference 1 et 2 ne peut etre identique");
                txtMSISDN1.findFocus();
                return false;
            }  if (_msisdn1.trim().equalsIgnoreCase(_msisdn3.trim())){
                Toast.makeText(this, "le Numero reference 1 et 3 ne peut etre identique", Toast.LENGTH_LONG).show();
                lblError.setText("le Numero reference 1 et 3 ne peut etre identique");
                txtMSISDN1.findFocus();
                return false;
            } if (_msisdn2.trim().equalsIgnoreCase(_msisdn3.trim())){
                Toast.makeText(this, "le Numero reference 3 et 2 ne peut etre identique", Toast.LENGTH_LONG).show();
                lblError.setText("le Numero reference 3 et 2 ne peut etre identique");
                txtMSISDN2.findFocus();
                return false;
            }

        }catch (Exception d){
            Log.e("",d.toString());
            return false;

        }

        return true;
  }
    public void onPrecedentClick(View view){
        if (isValidatedSIM()){
            isEnabledSIM(true);
            isEnabledID(false);
        }
        else{
            isEnabledSIM(false);
            isEnabledID(true);
        }
    }
    public void isEnabledSIM(Boolean blvalue){
      //  setTitle("AFRICELL-SWAP");
        ((Button)findViewById(R.id.validateButton)).setVisibility(View.GONE);
        isEnabledPersonal(blvalue);

        if (blvalue){
            ((View)findViewById(R.id.LineSIMInfo0)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LineSIMInfo01)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblSIMInfoInfo)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LineSIMInfo)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LineSIMInfo1)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblNDC)).setVisibility(View.VISIBLE);
            ((Spinner)findViewById(R.id.spinnerNDC)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblMSISDN)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblICC)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtICC)).setVisibility(View.VISIBLE);
           // ((Button)findViewById(R.id.btnSim)).setVisibility(View.VISIBLE);

        }else{
            ((View)findViewById(R.id.LineSIMInfo0)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LineSIMInfo01)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblSIMInfoInfo)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LineSIMInfo)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LineSIMInfo1)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblNDC)).setVisibility(View.GONE);
            ((Spinner)findViewById(R.id.spinnerNDC)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblMSISDN)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblICC)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtICC)).setVisibility(View.GONE);
            //((Button)findViewById(R.id.btnSim)).setVisibility(View.GONE);

        }

    }
    public void isEnabledPersonal(Boolean blvalue){
        ((Button)findViewById(R.id.validateButton)).setVisibility(View.GONE);
        if (blvalue){
            ((TextView)findViewById(R.id.lblEmptySeparator1)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo0)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo01)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblPersonalInfo)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo1)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblFirstName)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtFirstName)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblFatherName)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtFatherName)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblLastName)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtLastName)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblGender)).setVisibility(View.VISIBLE);
            ((Spinner)findViewById(R.id.spinnerGender)).setVisibility(View.VISIBLE);

            ((EditText)findViewById(R.id.lblBirthDateValue)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblStreet)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtStreet)).setVisibility(View.VISIBLE);

            ((TextView)findViewById(R.id.lblEmail)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtEmail)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lbltypeId)).setVisibility(View.VISIBLE);
            ((Spinner)findViewById(R.id.stypeId)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblIdnumber)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtIdnumber)).setVisibility(View.VISIBLE);

            ((Button)findViewById(R.id.btnPersonal)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.btnCapture2)).setVisibility(View.VISIBLE);
            ((ImageButton)findViewById(R.id.btnBirthDatePick)).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.camera_idcard1_image)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lbltypeId)).setVisibility(View.VISIBLE);
            ((Spinner)findViewById(R.id.stypeId)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblIdnumber)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblBOD)).setVisibility(View.VISIBLE);


        }else{
            ((TextView)findViewById(R.id.lblEmptySeparator1)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo0)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo01)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblPersonalInfo)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo1)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblFirstName)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtFirstName)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblFatherName)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtFatherName)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblLastName)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtLastName)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblGender)).setVisibility(View.GONE);
            ((Spinner)findViewById(R.id.spinnerGender)).setVisibility(View.GONE);

            ((Button)findViewById(R.id.btnPersonal)).setVisibility(View.GONE);
            ((ImageButton)findViewById(R.id.btnBirthDatePick)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.lblBirthDateValue)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblStreet)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtStreet)).setVisibility(View.GONE);

            ((TextView)findViewById(R.id.lblEmail)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtEmail)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lbltypeId)).setVisibility(View.GONE);
            ((Spinner)findViewById(R.id.stypeId)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblIdnumber)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtIdnumber)).setVisibility(View.GONE);

            ((Button)findViewById(R.id.btnCapture2)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.camera_idcard1_image)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lbltypeId)).setVisibility(View.GONE);
            ((Spinner)findViewById(R.id.stypeId)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblIdnumber)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblBOD)).setVisibility(View.GONE);


        }


    }
    public void isEnabledID(Boolean blvalue){
       // setTitle("AFRICELL-SWAP");
        if (blvalue){
            ((View)findViewById(R.id.LinePersonalInfo4)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo03)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblPersonalInfo3)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo3)).setVisibility(View.VISIBLE);
            ((View)findViewById(R.id.LinePersonalInfo5)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblMSISDN1)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN1)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblMSISDN2)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN2)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblMSISDN3)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN3)).setVisibility(View.VISIBLE);

            ((TextView)findViewById(R.id.lblMSISDN4)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN4)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.lblMSISDN5)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.txtMSISDN5)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.btnBackSim)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.validateButton)).setVisibility(View.VISIBLE);

        }else{
            ((View)findViewById(R.id.LinePersonalInfo4)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo03)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblPersonalInfo3)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo3)).setVisibility(View.GONE);
            ((View)findViewById(R.id.LinePersonalInfo5)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblMSISDN1)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN1)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblMSISDN2)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN2)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblMSISDN3)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN3)).setVisibility(View.GONE);

            ((TextView)findViewById(R.id.lblMSISDN4)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN4)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lblMSISDN5)).setVisibility(View.GONE);
            ((EditText)findViewById(R.id.txtMSISDN5)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.btnBackSim)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.validateButton)).setVisibility(View.GONE);

        }

    }
    public void  getIpServer(){
        _ipserver ="";_port="";
        String _name="", _apn="";
        db.open();
        Cursor c = db.getWAP();
        if (c.moveToFirst()) {
            _name=c.getString(1).toString();
            _apn=c.getString(2).toString();
            _ipserver = c.getString(3).toString();
            _port = c.getString(5).toString();
            //Toast.makeText(getBaseContext(), " server  "+_ipserver, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getBaseContext(), "Server no found. configure your APN!!!", Toast.LENGTH_LONG).show();
            new MainActivity().callForm(this, Activity_history.class);
            finish();
        }

       db.close();

      //  http://localhost:81/apn/name.php

        if (!_port.equals("")){
            _port=":"+_port;
        }

        if (!_apn.equals("")){
            _apn="/"+_apn;
        }
        /*if (!_name.equals("")){
            _name="/"+_name;
        }*/

        pathUrl = baseUrlswap +_apn +_name ;
        txtURLIP = pathUrl;

}
     /*
     public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(),strmotif[position] ,Toast.LENGTH_LONG).show();
    }*/

    public void onUploadClick() {

        _apitoken=getToken();
        strURLInsertSwapSIM="http://10.100.21.95:8280/AfricellRegMobileMoney.asmx/onInsertdata";
        new  AsyncInsert().execute(_msisdn,_iccid, _firstname,
                _lastname,_nickname,dob,_gender,_street,_email,_idtype,_idnumber,strIdCardSide1,
                _msisdn1,_msisdn2,_msisdn3,_msisdn4,_msisdn5,_userId,_apitoken);

       // TextView lblError = (TextView) findViewById(R.id.lblError);
       // lblError.setTextColor(Color.RED);


    }
    private class AsyncInsert extends AsyncTask<String, String, String>{
        ProgressDialog pdLoading;
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              pdLoading = new ProgressDialog(Activity_swap.this);
            //this method will be running on UI thread
            pdLoading.setMessage("\tVeuillez patientez SVP!!!");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL(strURLInsertSwapSIM);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception doInbackground";
            }
////try post
            try {
                     // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Headers","API-TOKEN:" + getToken());
                   // conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    //conn.setDoOutput(true);

                    // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("_msisdn", params[0])
                        .appendQueryParameter("_iccid", params[1])
                        .appendQueryParameter("_firstname", params[2])
                        .appendQueryParameter("_lastname", params[3])
                        .appendQueryParameter("_nickname", params[4])
                        .appendQueryParameter("dob", params[5])
                        .appendQueryParameter("_gender", params[6])
                        .appendQueryParameter("_street", params[7])
                        .appendQueryParameter("_email", params[8])
                        .appendQueryParameter("_idtype", params[9])
                        .appendQueryParameter("_idnumber", params[10])
                        .appendQueryParameter("strIdCardSide1", params[11])
                        .appendQueryParameter("_msisdn1", params[12])
                        .appendQueryParameter("_msisdn2", params[13])
                        .appendQueryParameter("_msisdn3", params[14])
                        .appendQueryParameter("_msisdn4", params[15])
                        .appendQueryParameter("_msisdn5", params[16])
                        .appendQueryParameter("_userId", params[17])
                        .appendQueryParameter("apitoken", params[18]
                        );

                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception try post"+e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                     Log.e("connexion","connexion :");
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{
                     Log.e("connexion","not connexion....");
                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception response";
            } finally {
                conn.disconnect();
            }

        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
                try{

                    int position =result.indexOf("exception");
                    int falied=result.toLowerCase().indexOf("failed");

                    JSONObject obj = new JSONObject(result);

                    if ((position>0)|| result.equalsIgnoreCase("unsuccessful")){
                        //system return error
                        Toast.makeText(Activity_swap.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                        Log.e("SmsReceiver","OOPs! Something went wrong. Connection Problem.");

                        TextView lblError = (TextView) findViewById(R.id.lblError);
                        lblError.setText("OOPs! Something went wrong. Connection Problem.");

                        return;
                    }else if (falied > 0){
                        TextView lblError = (TextView) findViewById(R.id.lblError);
                        lblError.setText(obj.getString("Response").toString());
                        return;

                    }if(!result.equalsIgnoreCase("")){
                        if (result!=null && result.toLowerCase().indexOf("successful")>=0){

                            TextView lblError = (TextView) findViewById(R.id.lblError);
                            lblError.setText(obj.getString("Response").toString());
                            //delete de pictures on the phone
                            //clean PersonalPhoto
                             if (!strIdCardSide1.equals("")) {
                                File file1 = new File(strSide1URL);
                                file1.delete();
                                getContentResolver().delete(Side1Uri, null, null);
                             }

                              popAlert("SIM Swap",obj.getString("Response").toString(),Activity_swap.class,MainActivity.class,Activity_swap.this);
                            //finish();
                            return;

                        }


                    }else if (result.equalsIgnoreCase("")){
                        // Log.e("SmsReceiver","OOPs! sorry Invalid reguest!!!!");
                        TextView lblError = (TextView) findViewById(R.id.lblError);
                        lblError.setText("OOPs! sorry Invalid reguest!!!!");
                    }
                }catch (Exception e){
                    TextView lblError = (TextView) findViewById(R.id.lblError);
                    lblError.setText(e.getMessage());

                }

        }

    }
    private  void getValuesFromDB(String xvalue){
        //select xvalue=motif from dbb

    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showDatePickerDialog(View v) {
        try {

            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }catch (Exception  ex){
            ///Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public  void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String strDay = "";
            String strMonth = "";
            if (day < 10) {
                strDay = "0" + day;
            } else {
                strDay = "" + day;
            }
            month = month + 1;
            if (month < 10) {
                strMonth = "0" + month;
            } else {
                strMonth = "" + month;
            }
            Activity_swap activity=(Activity_swap)getActivity();
            activity.sSetBirthDate( strMonth+"/"+strDay+"/"+year);
        }
    }

    private void sSetBirthDate(String strDate){
        final TextView textViewToChange = (TextView) findViewById(R.id.lblBirthDateValue);
        try {

            if (!strDate.equals("")) {
                //	final TextView textViewToChange = (TextView) findViewById(R.id.lblBirthDateValue);
                textViewToChange.setText(strDate);
            }
        }catch (Exception ex){
            textViewToChange.setText("01/01/1900");
        }
    }
    public void CaptureCaptureIdCardSide1(View V) {
        try {
            mIdCardSide1 = (ImageView) findViewById(R.id.camera_idcard1_image);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Side1 Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            Side1Uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Side1Uri);
            startActivityForResult(intent, CAMERA_PIC_REQUEST_IdCardSide1);
        } catch (Exception e) {
            Log.e("capture ID Card", e.getMessage());
            clsfunction.popMessage("Error occur", "capture ID Card");
            return;
        }

    }
    private static final int PICK_IMAGE = 1;
    private Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            android.database.Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            decodeFile(picturePath, 512);

        }


       /* if (requestCode == CAMERA_PIC_REQUEST_PersonalPhoto) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), PersonalPhotoUri);
                    strPersonalPhotoURL = getRealPathFromURI(PersonalPhotoUri);
                    decodeFile(strPersonalPhotoURL, 512);

                    //if (intNbrOfFaces == 1) {
                    intNbrRequestedFace = 1;
                    mPersonalPhoto.setImageBitmap(bitmap);
                    thumbnail = bitmap;
                    //3
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                    //3.5 prepare the image to be uploaded
                    byte[] bytedata = bytes.toByteArray();
                    strPersonalPhoto = base64.encodeBytes(bytedata);
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                TextView lblError = (TextView) findViewById(R.id.lblError);
                lblError.setText("CAMERA_PIC_REQUEST : " +requestCode);
            }
        }
    if (requestCode == SCAN_SIM_BAR_CODE) {
            EditText txtICC = (EditText) findViewById(R.id.txtICC);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                String contents = scanningResult.getContents();
                //String format = scanningResult.getFormatName();
                txtICC.setText(contents);
                getNDCMSISDN(contents);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                txtICC.setText("");
            }
        }*/
        if (requestCode == CAMERA_PIC_REQUEST_IdCardSide1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), Side1Uri);
                    strSide1URL =  getRealPathFromURI(Side1Uri);
                    decodeFile(strSide1URL, 1024);
                    mIdCardSide1.setImageBitmap(bitmap);
                    thumbnail = bitmap;
                    //3
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    //3.5 prepare the image to be uploaded
                    byte[] bytedata = bytes.toByteArray();
                    strIdCardSide1 = base64.encodeBytes(bytedata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }




    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        android.database.Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void decodeFile(String filePath, int pictsize) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        //final int REQUIRED_SIZE = 512;
        //final int REQUIRED_SIZE = 1024;
        //final int REQUIRED_SIZE = 2048;
        final int REQUIRED_SIZE = pictsize;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        //image.setImageBitmap(bitmap);
    }

    private class AsyncInserthttps extends AsyncTask<String, String, String>{
          ProgressDialog dialog;

        HttpsURLConnection conn;
        URL url = null;
        SSLContext sslcontext;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Activity_swap.this);
            dialog.setMessage("\tVeuillez patientez SVP!!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... params) {

            try {
                url = new URL(strURLInsertSwapSIM);

                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, null);
                //SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
                //HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpsURLConnection) url.openConnection();
                conn.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                conn.setSSLSocketFactory(sslcontext.getSocketFactory());
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Headers","API-TOKEN:" + getToken());

                // setDoInput and setDoOutput method depict handling of both send and receive
                // conn.setDoInput(true);
                //conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("_msisdn", params[0])
                        .appendQueryParameter("_iccid", params[1])
                        .appendQueryParameter("_firstname", params[2])
                        .appendQueryParameter("_lastname", params[3])
                        .appendQueryParameter("_nickname", params[4])
                        .appendQueryParameter("dob", params[5])
                        .appendQueryParameter("_gender", params[6])
                        .appendQueryParameter("_street", params[7])
                        .appendQueryParameter("_email", params[8])
                        .appendQueryParameter("_idtype", params[9])
                        .appendQueryParameter("_idnumber", params[10])
                        .appendQueryParameter("strIdCardSide1", params[11])
                        .appendQueryParameter("_msisdn1", params[12])
                        .appendQueryParameter("_msisdn2", params[13])
                        .appendQueryParameter("_msisdn3", params[14])
                        .appendQueryParameter("_msisdn4", params[15])
                        .appendQueryParameter("_msisdn5", params[16])
                        .appendQueryParameter("_userId", params[17])
                        .appendQueryParameter("apitoken", params[18]
                        );
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception try post"+e1.toString();
            }
            finally {

            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpsURLConnection.HTTP_OK) {
                    Log.e("connexion","connexion :");
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{
                    Log.e("connexion","not connexion....");
                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "error occur exception: "+e.getMessage();
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try{

                int position =result.indexOf("exception");
                int falied=result.toLowerCase().indexOf("failed");
                JSONObject obj = new JSONObject(result);

                if ((position>0)|| result.equalsIgnoreCase("unsuccessful")){
                    //system return error
                    Toast.makeText(Activity_swap.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    Log.e("SmsReceiver","OOPs! Something went wrong. Connection Problem.");

                    TextView lblError = (TextView) findViewById(R.id.lblError);
                    lblError.setText("OOPs! Something went wrong. Connection Problem.");

                    return;
                }else if (falied > 0){
                    TextView lblError = (TextView) findViewById(R.id.lblError);
                    lblError.setText(obj.getString("Response").toString());
                    return;

                }if(!result.equalsIgnoreCase("")){
                    if (result!=null && result.toLowerCase().indexOf("successful")>=0){

                        TextView lblError = (TextView) findViewById(R.id.lblError);
                        lblError.setText(obj.getString("Response").toString());
                        //delete de pictures on the phone
                        //clean PersonalPhoto
                        if (!strIdCardSide1.equals("")) {
                            File file1 = new File(strSide1URL);
                            file1.delete();
                            getContentResolver().delete(Side1Uri, null, null);
                        }

                        popAlert("SIM Swap",obj.getString("Response").toString(),Activity_swap.class,MainActivity.class,Activity_swap.this);
                        //finish();
                        return;

                    }


                }else if (result.equalsIgnoreCase("")){
                    // Log.e("SmsReceiver","OOPs! sorry Invalid reguest!!!!");
                    TextView lblError = (TextView) findViewById(R.id.lblError);
                    lblError.setText("OOPs! sorry Invalid reguest!!!!");
                }
            }catch (Exception e){
                TextView lblError = (TextView) findViewById(R.id.lblError);
                lblError.setText(e.getMessage());

            }

        }
    }
    public void popAlert(String strTitle,String strMessage,final Class classBtnYes,final Class classBtnNon,final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
      //  builder.setTitle(Html.fromHtml("<font color='#FF7F27'>This is a test</font>"));
        builder.setMessage(strMessage);
        builder.setTitle(strTitle);
        builder.setIcon(R.drawable.afr_inc);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                new MainActivity().callForm(context,classBtnYes.getClass());
                finish();
            }
        });

       /* builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new MainActivity().callForm(getApplicationContext(),classBtnNon.getClass());
                finish();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();
        return;

    }

}
