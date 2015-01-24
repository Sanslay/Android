package android.arduinos.fragments;


import android.arduinos.R;
import android.arduinos.activity.MainActivity;
import android.arduinos.dao.GenericResponse;
import android.arduinos.entities.CheckedArduino;
import android.widget.*;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.pinread)
public class PinReadFragment extends MyFragment {
  // l'activité
  private MainActivity activité;
  // les éléments de l'interface visuelle
  @ViewById(R.id.Spinnernumeropin)
  Spinner formulaireDropDownList;
  @ViewById(R.id.radioButtonAnalogique)
  RadioButton radioButtonAnalogique;
  @ViewById(R.id.radioBinaire)
  RadioButton radioBinaire;
  @ViewById(R.id.buttonExecute)
  Button btnExecute;
  @ViewById(R.id.nbcligno)
  TextView ErreurPin;
  @ViewById(R.id.ExcText)
  TextView ExcText;

  @ViewById(R.id.ListViewArduinos)
  ListView listArduinos;
  @ViewById(R.id.ListViewReponseRead)
  ListView ListViewReponseRead;


  private String NbPin;
  private String Mode;
  List<String> listReponses = new ArrayList<String>();

  @AfterViews
  public void initFragment() {
    ErreurPin.setText("");
    ExcText.setText("");
// on note l'activité
    activité = (MainActivity) getActivity();

    /***********La liste deroulante ********** */
    // la liste déroulante
    List<String> list = new ArrayList<String>();
    list.add("0");
    list.add("1");
    list.add("2");
    list.add("3");
    list.add("4");
    list.add("5");
    list.add("6");
    list.add("7");
    list.add("8");
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activité, android.R.layout.simple_spinner_item, list);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    formulaireDropDownList.setAdapter(dataAdapter);
  }

  @Override
  public void onRefresh() {
    if (activité != null) {
// on rafraîchit les Arduinos
      List<CheckedArduino> arduinos = activité.getCheckedArduinos();
      if (arduinos != null) {
        listArduinos.setAdapter(new ListArduinosAdapter(activité, R.layout.listarduinos_item, arduinos, true));
      }
    }
  }
  //Si on appui sur le radio bouton BINAIRE
  @Click(R.id.radioBinaire)
  protected void doRadioradioBinaire() {
    //On check le radio binaire
    radioBinaire.setChecked(true);
    //et on uncheck la radio Analogique
    radioButtonAnalogique.setChecked(false);
  }

  @Click(R.id.radioButtonAnalogique)
  protected void doRadioAnalogique() {
    //On check le radio Analogique
    radioButtonAnalogique.setChecked(true);
    //et on uncheck la radio binaire
    radioBinaire.setChecked(false);
  }

  @Click(R.id.buttonExecute)
  protected void doExecute() {
    //Si les donnée sont valide
    if (pageValid()) {
      //On recupere la liste d'arduino
      List<CheckedArduino> arduinos = activité.getCheckedArduinos();
      //On parcours la liste d'arduino et on recupere les id pour effectue les actions
      for (CheckedArduino arduino : arduinos) {
        if(arduino.isChecked())
        {
          ReadInBackground(arduino.getId(), NbPin, Mode);
        }

      }
    }

  }

  private boolean pageValid() {
    //On recupere la valeur du pin
    int position = formulaireDropDownList.getSelectedItemPosition();
    String selectedItem = String.valueOf(formulaireDropDownList.getSelectedItem());

    NbPin = selectedItem;
    //On efface les messages d'erreur
    ErreurPin.setText("");
    ExcText.setText("");

    // on vérifie sa validité
    try {

      if (Integer.parseInt(NbPin) == 0) {

        throw new Exception();
      }
      if(radioBinaire.isChecked())
      {
        Mode="b";

      }
      if (radioButtonAnalogique.isChecked())
      {
        Mode="a";
      }

    } catch (Exception ex) {
      // affichage msg d'erreur


      ErreurPin.setText("Pas <0");

      // retour à l'UI
      return false;
    }


    // c'est bon

    ErreurPin.setText("");
    //ExcText.setText("Pin:"+NbPin+",Mode:"+Mode);

    return true;
  }

  //On lance la tache read
  @Background(id = "read", delay = MainActivity.PAUSE)
    void ReadInBackground(String Idarduino, String pin, String mode) {
    //On donne bien une reponse vide au depart
    GenericResponse response = null;
    try
    {
      //On envoi bien une commande pour lire la valeur du pin donnée
      response = activité.faireLire("1",Idarduino,Integer.parseInt(pin), mode);
      showResponses(response);
    }
    catch (Exception e)
    {
      response = new GenericResponse();
      response.setErreur(2);
      response.setMessages(getMessagesFromException(e));
      showResponses(response);
    }

  }
  //On montre les reponse
  @UiThread
  protected void showResponses(GenericResponse reponses) {

    if (reponses.getErreur() == 0)
    {
// on ajoute l'information à la liste des reponses
      listReponses.add(0,String.valueOf(reponses.getResponse().toString()));
    }
    else
    {
// on ajoute un msg d'erreur à la liste des reponses
      StringBuffer message = new StringBuffer();
      for (String msg : reponses.getMessages()) {
        message.append(String.format("[%s]", msg));
      }
      listReponses.add(message.toString());
    }

// on affiche les reponses
    ListViewReponseRead.setAdapter(new ArrayAdapter<String>(activité, android.R.layout.simple_list_item_1, android.R.id.text1, listReponses));

  }
}