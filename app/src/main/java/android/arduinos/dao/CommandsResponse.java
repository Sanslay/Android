package android.arduinos.dao;

import java.util.List;

/**
 * Created by usrlocal on 21/01/2015.
 */
public class CommandsResponse extends AbstractResponse {
  // réponses des Arduinos
  private List<ArduinoResponse> responses;
// getters et setters

  public List<ArduinoResponse> getResponses() {
    return responses;
  }

  public void setResponses(List<ArduinoResponse> responses) {
    this.responses = responses;
  }

}