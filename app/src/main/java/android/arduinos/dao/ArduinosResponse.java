package android.arduinos.dao;

import android.arduinos.entities.Arduino;

import java.util.List;

public class ArduinosResponse extends AbstractResponse {
  // liste des Arduinos
  private List<Arduino> arduinos;
// getters et setters

  public List<Arduino> getArduinos() {
    return arduinos;
  }

  public void setArduinos(List<Arduino> arduinos) {
    this.arduinos = arduinos;
  }
}