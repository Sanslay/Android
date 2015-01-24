package android.arduinos.dao;

import android.arduinos.*;
public class GenericResponse extends AbstractResponse {
  // réponse de l'Arduino
  private ArduinoResponse response;


// getters et setters

  public ArduinoResponse getResponse() {
    return response;
  }

  public void setResponse(ArduinoResponse response) {
    this.response = response;
  }
}