package android.arduinos.entities;
import java.io.Serializable;
public class Arduino implements Serializable {
  // données
  private String id;
  private String description;
  private String mac;
  private String ip;
  private int port;

  public Arduino() {
  }

  public Arduino(String id, String description, String mac, String ip, int port) {
    this.id = id;
    this.description = description;
    this.mac = mac;
    this.ip = ip;
    this.port = port;
  }

  @Override
  public String toString() {
    return "Arduino{" +
      "id='" + id + '\'' +
      ", description='" + description + '\'' +
      ", mac='" + mac + '\'' +
      ", ip='" + ip + '\'' +
      ", port=" + port +
      '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}