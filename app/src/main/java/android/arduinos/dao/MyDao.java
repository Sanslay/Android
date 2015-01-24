package android.arduinos.dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usrlocal on 08/01/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class MyDao implements IDao {
  // client du service REST
  @RestService
  RestClient restClient;

  @Override
  public ArduinosResponse getArduinos() {
    return restClient.getArduinos();
  }

  @Override
  public void setUrlServiceRest(String urlServiceRest) {
// on fixe l'URL du service REST
    restClient.setRootUrl(urlServiceRest);
  }

  @Override
  public void setTimeout(int timeout) {
// on fixe le timeout des requêtes du client REST
    HttpComponentsClientHttpRequestFactory factory = new
      HttpComponentsClientHttpRequestFactory();
    factory.setReadTimeout(timeout);
    factory.setConnectTimeout(timeout);
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    restClient.setRestTemplate(restTemplate);


  }
  //Requete pour faire blink
  @Override
  public GenericResponse faireClignoterLed( String idCommande, String idArduino, int pin,  int duree, int nombre){
    // exécution service
    GenericResponse info;
    try {
      info = restClient.faireClignoterLed( idCommande,idArduino, pin,   duree, nombre);

    } catch (Exception ex) {
      // cas d'erreur
      info = new GenericResponse();
      info.setErreur(-1);
      info.setMessages(getMessagesFromException(ex));
    }
    // résultat
    return info;
  }

  //Requete pour faire faireLire
  @Override
  public GenericResponse faireLire(String idCommande, String idArduino, int pin, String mode) {
    GenericResponse info;
    try {
      info = restClient.faireLire(idCommande,idArduino,pin,mode);

    } catch (Exception ex) {
      // cas d'erreur
      info = new GenericResponse();
      info.setErreur(-1);
      info.setMessages(getMessagesFromException(ex));
    }
    // résultat
    return info;
  }

  //Requete pour faire faireEcrire
  @Override
  public GenericResponse faireEcrire(String idCommande, String idArduino, int pin,String mode, int valeur) {
    GenericResponse info;
    try {
      info = restClient.faireEcrire(idCommande, idArduino,pin,mode, valeur) ;

    } catch (Exception ex) {
      // cas d'erreur
      info = new GenericResponse();
      info.setErreur(-1);
      info.setMessages(getMessagesFromException(ex));
    }
    // résultat
    return info;
  }

  // envoi de commandes jSON
  @Override
  public CommandsResponse sendCommands(List<ArduinoCommand> commands, String idArduino) {
    return restClient.sendCommands(commands, idArduino);
  }


  protected List<String> getMessagesFromException(Exception e) {
// liste des messages d'une exception
    Throwable th = e;
    List<String> messages = new ArrayList<String>();
    while (th != null) {
      messages.add(th.getMessage());
      th = th.getCause();
    }
    return messages;
  }

}