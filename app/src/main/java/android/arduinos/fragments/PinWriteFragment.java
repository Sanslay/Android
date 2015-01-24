package android.arduinos.fragments;

import android.arduinos.dao.GenericResponse;
import android.arduinos.entities.CheckedArduino;
import android.arduinos.R;
import android.arduinos.activity.MainActivity;
import android.view.View;
import android.widget.*;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.pinwrite)
public class PinWriteFragment extends MyFragment {
  // l'activité
  private MainActivity activité;
  // les éléments de l'interface visuelle
  @ViewById(R.id.Spinnernumeropin)
  Spinner Spinnernumeropin;
  @ViewById(R.id.radioButtonAnalogique)
  RadioButton radioButtonAnalogique;
  @ViewById(R.id.radioBinaire)
  RadioButton radioBinaire;
  @ViewById(R.id.switchonoff)
  Switch switchonoff;
  @ViewById(R.id.buttonExecuter)
  Button buttonExecuter;
  @ViewById(R.id.nbcligno)
  TextView ErreurPin;
  @ViewById(R.id.ExcText)
  TextView ExcText;
  @ViewById(R.id.textseek)
  TextView textseek;
  @ViewById(R.id.seekBar)
  SeekBar seekBar;


  private String NbPin;
  private String Mode;
  private String bit;
  private int progress;
  List<String> listReponses = new ArrayList<String>();

  // interface visuelle
  @ViewById(R.id.ListViewArduinos)
  ListView listArduinos;
  @ViewById(R.id.listReponsewrite)
  ListView listReponsewrite;

  @AfterViews
  public void initFragment() {
// on note l'activité
    activité = (MainActivity) getActivity();
    //On efface les message d'erreur
    ErreurPin.setText("");
    ExcText.setText("");
    textseek.setText("");
    radioBinaire.setChecked(true);
    switchonoff.setVisibility(View.VISIBLE);
    seekBar.setVisibility(View.INVISIBLE);
    progress=0;
    //On genere les Listerner pour la barre en analogique et donc pouvoir la liste
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
      public void onStartTrackingTouch(SeekBar seekBar) {
      }
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textseek.setText(String.valueOf(progress));
      }
    });

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
    Spinnernumeropin.setAdapter(dataAdapter);
  }

  @Click(R.id.radioBinaire)
  protected void doRadioradioBinaire() {

    radioBinaire.setChecked(true);
    //On check le radio binaire
    radioButtonAnalogique.setChecked(false);
    //et on uncheck la radio Analogique
    //On rend visible le bouton ON OFF et invisible la seekBar et le text de la seekBar
    switchonoff.setVisibility(View.VISIBLE);
    seekBar.setVisibility(View.INVISIBLE);
    textseek.setVisibility(View.INVISIBLE);
  }

  //Reprend le meme principe que pour l'autre radio mais l'inverse
  @Click(R.id.radioButtonAnalogique)
  protected void doRadioAnalogique() {
    radioButtonAnalogique.setChecked(true);
    //On check le radio binaire
    radioBinaire.setChecked(false);

    switchonoff.setVisibility(View.INVISIBLE);
    seekBar.setVisibility(View.VISIBLE);
    textseek.setVisibility(View.VISIBLE);
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

  @Click(R.id.buttonExecuter)
  protected void doExecute() {
// on execute des truc
    if (pageValid()) {
      List<CheckedArduino> arduinos = activité.getCheckedArduinos();

      for (CheckedArduino arduino : arduinos) {
        if (arduino.isChecked()) {
          if (Mode == "b") {
            writeInBackground(arduino.getId(), NbPin, Mode,Integer.parseInt( bit));
          }
          if (Mode == "a") {
            writeInBackground(arduino.getId(), NbPin, Mode, progress);
          }

        }

      }
    }
  }
  private boolean pageValid() {


    int position = Spinnernumeropin.getSelectedItemPosition();
    String selectedItem = String.valueOf(Spinnernumeropin.getSelectedItem());
    NbPin = selectedItem;
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

        progress = seekBar.getProgress();
      }
      if (switchonoff.isChecked())
      {
        bit="1";
      }
      if ( switchonoff.isChecked()==false)
      {
        bit="0";
      }


    } catch (Exception ex) {
      // affichage msg d'erreur


      ErreurPin.setText("Pas <0");

      // retour à l'UI
      return false;
    }


    // c'est bon

    ErreurPin.setText("");


    return true;
  }


  @Background(id = "write", delay = MainActivity.PAUSE)
  void writeInBackground(String Idarduino, String pin,String mode, int nombre) {
    GenericResponse response = null;
    try {

      response = activité.faireEcrire("1", Idarduino, Integer.parseInt(pin),mode,nombre);

      showResponses(response);
    } catch (Exception e) {
      response = new GenericResponse();
      response.setErreur(2);
      response.setMessages(getMessagesFromException(e));
      showResponses(response);
    }

  }
  @UiThread
  protected void showResponses(GenericResponse reponses) {


    if (reponses.getErreur() == 0) {
// on ajoute l'information à la liste des reponses
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
    listReponsewrite.setAdapter(new ArrayAdapter<String>(activité, android.R.layout.simple_list_item_1, android.R.id.text1, listReponses));

  }
}
