package android.arduinos.fragments;

import android.arduinos.dao.ArduinoCommand;
import android.arduinos.dao.ArduinoResponse;
import android.arduinos.dao.CommandsResponse;
import android.arduinos.dao.GenericResponse;
import android.arduinos.entities.CheckedArduino;
import android.support.v4.app.Fragment;
import android.arduinos.R;
import android.arduinos.activity.MainActivity;
import android.view.View;
import android.widget.*;
import org.androidannotations.annotations.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EFragment(R.layout.commands)
public class CommandsFragment extends MyFragment {
  // l'activité
  private MainActivity activité;

  // les éléments de l'interface visuelle
  @ViewById(R.id.buttonExc)
  Button buttonExc;
  @ViewById(R.id.editJson)
  EditText editJson;
  @ViewById(R.id.textcommand)
  TextView textcommand;
  @ViewById(R.id.textErreur)
  TextView textErreur;

  @ViewById(R.id.ListViewArduinos)
  ListView listArduinos;
  @ViewById(R.id.listReponseCommand)
  ListView listReponseCommand;


  private List<ArduinoCommand> commands;
  private ObjectMapper mapper = new ObjectMapper();
  List<String> listReponses = new ArrayList<String>();

  @AfterViews
  public void initFragment() {
// on note l'activité
    activité = (MainActivity) getActivity();
    //On vide les messages d'erreur
    textErreur.setText("");
    textcommand.setText("");
    commands = new ArrayList<ArduinoCommand>();
    //On pres ecrit la commande JSON pour se facilite la vie
    editJson.setText("{\"id\":\"1\",\"ac\":\"cl\",\"pa\":{\"pin\":\"8\",\"dur\":\"100\",\"nb\":\"20\"}}");
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

  private boolean pageValid() {
    try {
      //On vide la liste
      commands.clear();
      //On efface les messages d'erreurs
      textErreur.setText("");
      //On defini des objets pour la requete
      ArduinoCommand command;
      String JSON;

      //On recupere la commande JSON
      JSON = editJson.getText().toString().trim();
      //Transformation de la commande JSON
      command = mapper.readValue(JSON, ArduinoCommand.class);
      //On l'ajoute a une liste de command
      commands.add(command);

    }
    catch (IOException e) {
      //En cas de probleme nous affichons une erreur
      e.printStackTrace();
      textErreur.setText("ERREUR COMMANDE");
      return false;
    }

    return true;
  }
  //Losque l'on appui sur le bouton
  @Click(R.id.buttonExc)
  protected void doExecute() {
  //Si la commande est valide
    if (pageValid()) {
      //On envoi la commande
      commandInBackGround(activité.getCheckedArduinos());

    }
  }

  //Tache commande
  @Background(id = "commande", delay = MainActivity.PAUSE)
  void commandInBackGround(List<CheckedArduino> arduinos) {
    //On crée une liste de responses
    List<CommandsResponse> responses = new ArrayList<CommandsResponse>();
    CommandsResponse response;

    int idCommand = 0;
    for (CheckedArduino arduino : arduinos)
    {
      if(arduino.isChecked()) {
        try {
          response = activité.sendCommands(commands, arduino.getId());
          idCommand++;
        } catch (Exception e) {
          response = new CommandsResponse();
          response.setErreur(2);
          response.setMessages(getMessagesFromException(e));
        }
        responses.add(response);
      }
    }
    showResponses(responses);
  }
  @UiThread
  void showResponses(List<CommandsResponse> responses) {
    // on teste la nature de l'information reçue
    List<String> listResponses;
    listResponses = new ArrayList<String>();
    for (CommandsResponse response: responses) {
      int erreur = response.getErreur();
      if (erreur == 0) {
        for (ArduinoResponse r : response.getResponses())
          listResponses.add(r.toString());
      }
      // erreur ?
      if (erreur != 0) {
        // on affiche les msg d'erreur
        showMessages(activité, response.getMessages());
        // on n'affiche que l'onglet [Config]
        activité.showTabs(new Boolean[]{true, false, false, false, false});
      }
    }
    listReponseCommand.setAdapter(new ArrayAdapter<String>(activité, android.R.layout.simple_list_item_1, android.R.id.text1, listResponses));
  }

  }
