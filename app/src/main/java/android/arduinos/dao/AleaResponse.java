package android.arduinos.dao;

import java.util.List;

public class AleaResponse {

  // data
  private int erreur;
  private List<String> messages;
  private int alea;

  // getters et setters

  public int getErreur() {
    return erreur;
  }

  public void setErreur(int erreur) {
    this.erreur = erreur;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public int getAlea() {
    return alea;
  }

  public void setAlea(int alea) {
    this.alea = alea;
  }
}
