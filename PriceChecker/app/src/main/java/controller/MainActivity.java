

//INTERNACIONALITZACI�
//BASE DADES
//BASE DADES LOCAL
//CODIS -> LECTOR DE SOFTWARE OBERT??
//MODEL VISTA CONTROLADOR
//SEPARACI� CLASSES
//WHILE TRUE

package controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import model.DataBase;
import model.DrawerItemClickListener;
import uab.tfg.pricechecker.R;
import utils.MyCamera;

public class MainActivity extends Activity {
////////ELEMENTS DELS QUE DISPOSAREM/////////////////
    private MyCamera myCamera = new MyCamera();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] options;
    private Button flashButton;
    private Button optionsButton;
    //DB
    DataBase prodDbHelper = null;
    SQLiteDatabase prodDb = null;
	 
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Associaci� del layout (vista xml)
    setContentView(R.layout.activity_main);
    //Tractament dels botons
    this.initializeButtons();
    //inicialitzem el drawer layout
    this.initializeDrawerLayout();
    //inicialitzem la c�mera i la seva vista
    myCamera.initializeCamera(this);
    myCamera.setCameraParameters();
    prodDbHelper = new DataBase(getApplicationContext());
    prodDb = prodDbHelper.getWritableDatabase();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
    @Override
    public void onResume() {
        super.onResume();  // Sempre cridem el m�tode del pare primer
        //cada cop que tornem a la app configurarem la c�mera
        if (myCamera.GetCamera() == null) {
            myCamera.initializeCamera(this);
            myCamera.setCameraParameters();
        }
    }
	
    @Override
    public void onPause(){
        super.onPause();
        myCamera.releaseCamera(); //parem la c�mera perqupe la puguin fer servir altres app
    }
	
    @Override
    public void onBackPressed(){ //pitjar el bot� "enrere" �s diferent de onPause()->(home)
        //quan pitgem bot� enrere
        super.onBackPressed(); //afegim funcions a la funci� "per defecte"
        myCamera.releaseCamera();
    }

////////////////////////////////////////////////////////
//////////////////////////BOTONS ADDICIONALS////////////////////////////
///////////////////////////////////////////////////	
public void initializeButtons(){
    //bot� flaix
    flashButton = (Button) findViewById(R.id.button_flash);
    flashButton.setOnClickListener(clickFlash);
    //bot� opcions
    optionsButton = (Button) findViewById(R.id.button_options);
    optionsButton.setOnClickListener(clickOptions);
}

    public Button.OnClickListener clickFlash = new Button.OnClickListener() {
        public void onClick(View v) {
            if(myCamera.isFlashON()==false){ //si el flaix ja est� enc�s, l'apaguem
                myCamera.flashON();
            }else{
                myCamera.flashOFF();
            }
        }
    };
	
    public Button.OnClickListener clickOptions = new Button.OnClickListener() {
        public void onClick(View v) {
            //obrim el men� lateral
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    };
	
    ////////////////////////////////////////////////////////
    //DRAWER LAYOUT////////////////////////////
    ////////////////////////////////////////////////////
    public void initializeDrawerLayout(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //no cal fer un adaptador a la mDrawer,
        //ja est� configurat en la situaci� dels elements al xml
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //per� s� en la ListView:
        //agafem les opcions de "strings"
        options = getResources().getStringArray(R.array.options_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options));
        //simple_list_itm_1, �s un layout "prefabricat" que ve amb la API,
        //consistent en un  layout amb un text simple que requereix el ArrayAdapter
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    //DB
    public boolean insertRow(int _id, String _name, int _price, String _description,
                             String _establishment){

        //insertarem una nova fila a la base de dades
        //retornar� true si s'ha inserit correctament
        ContentValues values = new ContentValues();
        values.put("ID", _id);
        values.put("Name", _name);
        values.put("Price", _price);
        values.put("Description", _description);
        values.put("Establishment", _establishment);
        return (prodDb.insert("Product", null, values) > 0);
    }
}
