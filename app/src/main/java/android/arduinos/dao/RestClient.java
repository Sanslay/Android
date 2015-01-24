package android.arduinos.dao;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Rest(converters = {MappingJacksonHttpMessageConverter.class})
public interface RestClient extends RestClientRootUrl, RestClientSupport {
  // liste des Arduinos
  @Get("/arduinos")
  public ArduinosResponse getArduinos();

  // RestTemplate
  public void setRestTemplate(RestTemplate restTemplate);

  //Requete pour faire blink
  @Get("/arduinos/blink/{idCommande}/{idArduino}/{pin}/{duree}/{nombre}")
  public GenericResponse faireClignoterLed( String idCommande, String idArduino, int pin,  int duree, int nombre);

  //Requete pour faire faireLire
  @Get("/arduinos/pinRead/{idCommande}/{idArduino}/{pin}/{mode}")
  public GenericResponse faireLire( String idCommande, String idArduino, int pin,  String mode);

  //Requete pour faire faireEcrire
  @Get("/arduinos/pinWrite/{idCommande}/{idArduino}/{pin}/{mode}/{valeur}")
   public GenericResponse faireEcrire( String idCommande, String idArduino, int pin,String mode,  int valeur);


  // envoi de commandes jSON
  @Post("/arduinos/commands/{idArduino}")
  public CommandsResponse sendCommands(List<ArduinoCommand> commands, String idArduino);


}


