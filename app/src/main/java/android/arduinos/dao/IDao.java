package android.arduinos.dao;

import java.util.List;
public interface IDao {
  // liste des arduinos
  public ArduinosResponse getArduinos();
  // URL du service web
  public void setUrlServiceRest(String url);
  // timeout du service web
  public void setTimeout(int timeout);

  public GenericResponse faireClignoterLed( String idCommande, String idArduino, int pin,  int duree, int nombre);

  public GenericResponse faireLire( String idCommande, String idArduino, int pin,  String mode);

  public GenericResponse faireEcrire( String idCommande, String idArduino, int pin,String mode,  int valeur);

  public CommandsResponse sendCommands(List<ArduinoCommand> commands, String idArduino);

}
