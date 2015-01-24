package android.arduinos.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.arduinos.R;
import android.arduinos.dao.*;
import android.arduinos.entities.CheckedArduino;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;
import android.arduinos.fragments.*;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;
import java.util.Locale;

@EActivity
public class MainActivity extends FragmentActivity implements ActionBar.TabListener, IDao {
  // couche [DAO]
  @Bean(MyDao.class)
  IDao dao;
  // les constantes
  // -----------------------------------------------------
// pause avant exécution d'une tâche asynchrone
  public final static int PAUSE = 0;
  // délai en ms d'attente maximale de la réponse du serveur
  private final static int TIMEOUT = 1000;

  @AfterInject
  public void initActivity() {
// configuration de la couche [DAO]
    dao.setTimeout(TIMEOUT);
  }

  @Override
  public ArduinosResponse getArduinos() {
    return dao.getArduinos();
  }

  @Override
  public void setUrlServiceRest(String urlServiceRest) {
    dao.setUrlServiceRest(urlServiceRest);
  }


  @Override
  public void setTimeout(int timeout) {
    dao.setTimeout(timeout);
  }

  //Pour faireClignoter la led
  @Override
  public GenericResponse faireClignoterLed(String idCommande, String idArduino, int pin, int duree, int nombre) {

    return dao.faireClignoterLed( idCommande,  idArduino,  pin,   duree,  nombre);
  }
  //Pour faireLire
  @Override
  public GenericResponse faireLire(String idCommande, String idArduino, int pin, String mode) {
    return dao.faireLire(idCommande,idArduino,pin,mode);
  }

  //Pour faireEcrire
  @Override
  public GenericResponse faireEcrire(String idCommande, String idArduino, int pin,String mode, int valeur) {
    return dao.faireEcrire(idCommande,idArduino,pin,mode,valeur);
  }
  //Pour envoyer une commande
  @Override
  public CommandsResponse sendCommands(List<ArduinoCommand> commands, String idArduino) {
    return dao.sendCommands(commands, idArduino);
  }

  // le gestionnaire de fragments ou sections
  SectionsPagerAdapter mSectionsPagerAdapter;
  // le conteneur des fragments
  MyPager mViewPager;
  // les onglets
  private Tab[] tabs;
  // les constantes
  // la liste des Arduinos
  private List<CheckedArduino> checkedArduinos;

  // -----------------------------------------------------


  @Override
  protected void onCreate(Bundle savedInstanceState) {
// classique
    super.onCreate(savedInstanceState);
// il faut installer le sablier avant de créer la vue
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
// la vue
    setContentView(R.layout.main);
// la barre d'onglets
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
// instanciation de notre gestionnaire de fragments
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
// on récupère la référence du conteneur de fragments
    mViewPager = (MyPager) findViewById(R.id.pager);
// il est associé à notre gestionnaire de fragments
    mViewPager.setAdapter(mSectionsPagerAdapter);
// on inhibe le swipe
    mViewPager.setSwipeEnabled(false);
// on crée autant d'onglets qu'il y a de fragments affichés par le conteneur
    tabs = new Tab[mSectionsPagerAdapter.getCount()];
    for (int i = 0; i < tabs.length; i++) {
      tabs[i] = actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this);
      actionBar.addTab(tabs[i]);
    }
// on n'affiche que le 1er onglet
    showTabs(new Boolean[]{true, false, false, false, false});
  }

  // gestion des onglets
  public void showTabs(Boolean[] show) {
// si show[i] est vrai, affiche l'onglet n° i
    final ActionBar actionBar = getActionBar();
// on passe tous les onglets en revue
// en commençant par la fin
    for (int i = 0; i < show.length; i++) {
// onglet n° i
      Tab tab = tabs[i];
      int position = tab.getPosition();
      if (show[i]) {
// la vue doit être affichée si elle ne l'est pas déjà
        if (position == Tab.INVALID_POSITION) {
          actionBar.addTab(tab);
        }
      } else {
// la vue doit être enlevée si elle ne l'est pas déjà
        if (position != Tab.INVALID_POSITION) {
          actionBar.removeTab(tab);
        }
      }
    }
  }

  public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
// un onglet a été sélectionné - on change le fragment affiché par le conteneur de fragments
    int position = tab.getPosition();
// on l'affiche
    mViewPager.setCurrentItem(position);
// on rafraîchit le fragment affiché
    mSectionsPagerAdapter.getItem(position).onRefresh();
  }

  public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
  }

  public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
  }

  // notre gestionnaire de fragments
// à redéfinir pour chaque application
// doit définir les méthodes suivantes
// getItem, getCount, getPageTitle
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    // les fragments
    MyFragment[] fragments = {new ConfigFragment_(), new BlinkFragment_(), new PinReadFragment_(), new PinWriteFragment_(), new CommandsFragment_()};

    // constructeur
    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    // doit rendre le fragment n° i avec ses éventuels arguments
    @Override
    public MyFragment getItem(int position) {
// on rend le fragment
      return fragments[position];
    }

    // rend le nombre de fragments à gérer
    @Override
    public int getCount() {
      return fragments.length;
    }



    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      switch (position) {
        case 0:
          return getString(R.string.config_titre).toUpperCase(l);
        case 1:
          return getString(R.string.blink_titre).toUpperCase(l);
        case 2:
          return getString(R.string.pinread_titre).toUpperCase(l);
        case 3:
          return getString(R.string.pinwrite_titre).toUpperCase(l);
        case 4:
          return getString(R.string.commands_titre).toUpperCase(l);
      }
      return null;
    }





  }

  // getters et setters
  public List<CheckedArduino> getCheckedArduinos() {
    return checkedArduinos;
  }

  public void setCheckedArduinos(List<CheckedArduino> checkedArduinos) {
    this.checkedArduinos = checkedArduinos;
  }
}
