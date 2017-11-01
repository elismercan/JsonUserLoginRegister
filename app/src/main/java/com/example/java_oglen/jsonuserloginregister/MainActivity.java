package com.example.java_oglen.jsonuserloginregister;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.parser.Tag;

import static android.R.attr.name;
import static android.R.attr.tag;

public class MainActivity extends AppCompatActivity {

    EditText etAd, etSoyad, etMail, etTel,etSifre;
  String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etAd= (EditText) findViewById(R.id.etAd);
        etSoyad= (EditText) findViewById(R.id.etSoyad);
        etTel= (EditText) findViewById(R.id.etTel);
        etMail=(EditText)findViewById(R.id.etMail);
        etSifre=(EditText)findViewById(R.id.etSifre);



    }

    public void  tetikle(View v){
        String Ad= etAd.getText().toString();
        String Soyad=etSoyad.getText().toString();
        String Telefon=etTel.getText().toString();
        String Mail=etMail.getText().toString();
        String Sifre=etSifre.getText().toString();


        url="http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userName="+Ad+"&"+
                "userSurname="+Soyad+"&"+
                "userPhone="+Telefon+"&"+
                "userMail="+Mail+"&"+
                "userPass="+Sifre+"&";

        new jsonData(url,this).execute();
    }

    class jsonData extends AsyncTask<Void,Void,Void>{

        String data ="";
        String url="";
        ProgressDialog pro;
        Context cnx;

        public jsonData(String url,Context cnx){
            this.url=url;
            this.cnx=cnx;
            pro=new ProgressDialog(cnx);
            pro.setMessage("İşlem Yapılıyor Lütfen Bekleyiniz");
            pro.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{

                data= Jsoup.connect(url).ignoreContentType(true).get().body().text();

            }catch (Exception e){
                Log.e("Data Json Hatası","doInBackground",e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //grafiksel özelliği olan işlemler bu bölümde yapılır
            Log.d("Gelen Data",data);
            try {
                JSONObject obj=new JSONObject(data);
                boolean durum = obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                if(durum)
                {
                    //kullanıcı kayıt başarılı
                    Toast.makeText(cnx,mesaj,Toast.LENGTH_SHORT).show();
                    String kid=obj.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                    Log.d("kid", kid);
                }else {
                    //kullanıcı kayıt başarısız
                    Toast.makeText(cnx,mesaj,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //yükleme ekrenını tamamla
            pro.hide();

        }
    }
}
