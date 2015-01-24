package android.arduinos.fragments;
import android.arduinos.R;
import android.arduinos.activity.MainActivity;

import android.arduinos.dao.GenericResponse;
import android.arduinos.entities.Arduino;
import android.arduinos.entities.CheckedArduino;
import android.widget.*;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.List;
@EFragment(R.layout.blink)
public class BlinkFragment extends MyFragment {

  // les éléments de l'interface visuelle
  @ViewById(R.id.buttonExecute)
  Button btnExecute;

  @ViewById(R.id.editTextClignotement)
  EditText TextClignotement;

  @ViewById(R.id.editTextdureeclignotement)
  EditText Textdureeclignotement;

  @ViewById(R.id.Spinnernumeropin)
  Spinner dropDownList;

  @ViewById(R.id.Erreurnbclignotement)
  TextView Erreurnbclignotement;
  @ViewById(R.id.Erreurtext2)
  TextView Erreurtext2;

  @ViewById(R.id.ExecuteText)
  TextView ExecuteText;

  @ViewById(R.id.ErreurPin)
  TextView ErreurPin;

  @ViewById(R.id.ListViewArduinos)
  ListView listArduinos;

  @ViewById(R.id.ListViewReponseBlink)
  ListView ListViewReponseBlink;
  ///************************************/

  // l'activité
  private MainActivity activité;
  private String Nbcligno;
  private String NbTempCligno;
  private String NbPin;
  List<String> listReponses = new ArrayList<String>();

  @AfterViews
  public void initFragment() {
// on note l'activité
    activité = (MainActivity) getActivity();

    /***********La liste deroulante ********** */
    // la liste déroulante

    List<String> list = new ArrayList<String>();
    list.add("0");list.add("1");list.add("2");list.add("3");list.add("4");list.add("5");list.add("6");list.add("7");list.add("8");

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activité, android.R.layout.simple_spinner_item, list);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    dropDownList.setAdapter(dataAdapter);

  }


  public void onRefresh() {
    if (activité != null) {
// on rafraîchit les Arduinos
      List<CheckedArduino> arduinos = activité.getCheckedArduinos();
      if (arduinos != null) {
        listArduinos.setAdapter(new ListArduinosAdapter(activité,
          R.layout.listarduinos_item, arduinos, true));
      }
    }
  }

  //On verifie si la page est valide
  private boolean pageValid() {
    //On efface les messages d'erreur
    Erreurnbclignotement.setText("");
    Erreurtext2.setText("");
    ErreurPin.setText("");
    ExecuteText.setText("");

    // on récupère les textes des input
    Nbcligno = TextClignotement.getText().toString().trim();
    NbTempCligno = Textdureeclignotement.getText().toString().trim();

    //On cherche la valeur dans la liste
    int position = dropDownList.getSelectedItemPosition();
    String selectedItem = String.valueOf(dropDownList.getSelectedItem());
    NbPin = selectedItem;


    boolean valid=true;

    // on vérifie sa validité
    try {
      if(Integer.parseInt(Nbcligno) < 1 || Integer.parseInt(Nbcligno) > 100)
      {
        Erreurnbclignotement.setText("Pas compris entre 1 et 100");
        valid = false;
      }
    } catch (Exception ex) {
      Erreurnbclignotement.setText("Pas compris entre 1 et 100");

      valid = false;
    }
    try {
      if (Integer.parseInt(NbTempCligno) < 100 || Integer.parseInt(NbTempCligno) > 2000) {
        Erreurtext2.setText("Pas compris entre 100 et 2000");
        valid = false;
      }
    } catch (Exception ex) {
      Erreurtext2.setText("Pas compris entre 100 et 2000");

      valid = false;
    }
    try {
      if (Integer.parseInt(NbPin) < 1 || Integer.parseInt(NbPin) > 13) {
        ErreurPin.setText("Pas <0");
        valid = false;
      }
    } catch (Exception ex) {
      ErreurPin.setText("Pas <0");

      valid = false;
    }

    return valid;
  }

  @Click(R.id.buttonExecute)
  protected void doExecute() {
// Si les données sont valide
    if (pageValid()) {
      //On recupere la liste d'arduino
      List<CheckedArduino> arduinos = activité.getCheckedArduinos();
      //On parcoure tous les arduinos et on selectionne les id
      for (CheckedArduino arduino : arduinos) {
        if(arduino.isChecked())
        {
          BlinkInBackground(arduino.getId(), NbPin, NbTempCligno, Nbcligno);
        }
      }
    }
  }

  //On lance la tache blink
  @Background(id = "blink", delay = MainActivity.PAUSE)
  void BlinkInBackground(String Idarduino, String pin, String duree, String nombre) {
    //On initialise bien une reponse null au depart pour eviter les erreurs
    GenericResponse response = null;

    try
    {
      //On recupere la reponse venant de activité/IDAO/DAO/RESTCLIENT
      response = activité.faireClignoterLed("1", Idarduino, Integer.parseInt(pin), Integer.parseInt(duree), Integer.parseInt(nombre));
      //On affiche les reponse
      showResponses(response);

    } catch (Exception e)
    {
      response = new GenericResponse();
      response.setErreur(2);
      response.setMessages(getMessagesFromException(e));
      showResponses(response);
    }

  }

  @UiThread
  protected void showResponses(GenericResponse reponses) {

    //Si on a pas d'erreur
    if (reponses.getErreur() == 0) {
// on ajoute l'information à la liste des reponses
      //On ajoute dans une liste de reponse des reponse
      listReponses.add(0,String.valueOf(reponses.getResponse()));
    }
    else {
// on ajoute un msg d'erreur à la liste des reponses
      StringBuffer message = new StringBuffer();
      for (String msg : reponses.getMessages()) {
        message.append(String.format("[%s]", msg));
      }
      listReponses.add(message.toString());
    }

// on affiche les reponses
    ListViewReponseBlink.setAdapter(new ArrayAdapter<String>(activité, android.R.layout.simple_list_item_1, android.R.id.text1, listReponses));

  }
}


