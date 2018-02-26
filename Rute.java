import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Rute implements Runnable{
  protected int x;
  protected int y;
  protected Rute nord;
  protected Rute sor;
  protected Rute ost;
  protected Rute vest;
  protected boolean paaVeien;
  public static String ruteInfo = "";
  private Lock laas = new ReentrantLock();
  private static OrdnetLenkeliste<String> liste = new OrdnetLenkeliste<>();
  public static int ANTALL_TRADER = 0;

  public Rute(int x, int y, boolean paaVeien){
    this.x = x;
    this.y = y;
    this.paaVeien = paaVeien;
  }

  public abstract char tilTegn();

  //Disse to kan settes samtidig.
  public void settNorVest(Rute nord, Rute vest){
    this.nord = nord;
    this.vest = vest;
  }

  public OrdnetLenkeliste<String> returnerListe(){
    OrdnetLenkeliste<String> n = new OrdnetLenkeliste<String>();
    for (String s : liste){
      n.settInn(liste.fjern());
    }
    return n;
  }

  //Disse kan settes samtidig.
  public void settSorOst(Rute sor, Rute ost){
    this.sor = sor;
    this.ost = ost;
  }

  //Setter at dette er en besokt rute
  public void harBesokt(){
      paaVeien = true;
  }

  public void resett(){
    paaVeien = false;
  }

  //Returnerer paaVei.
  public boolean giPaaVeien(){
    return paaVeien;
  }

  public Rute[] lagRuterArray(){
    Rute[] r = new Rute[4];
    r[0] = ost;
    r[1] = sor;
    r[2] = nord;
    r[3] = vest;
    return r;
  }

  public abstract boolean ruteType(String tekst);

  @Override
  public String toString(){
    int kolonne = x;
    int rad = y;
    return "(" + rad + ", "+ kolonne + ")";
  }

  public int tellHviteNaboer(Rute r){
    int antallHvite = 0;
    Rute[] liste = r.lagRuterArray();
    for (int i = 0; i < liste.length; i++){
      if (liste[i] instanceof HvitRute &&  !(liste[i].giPaaVeien())){
        antallHvite++;
      }
    }
    return antallHvite;
  }

  public Rute gaa(Rute r){
    laas.lock();
    try{
      Rute[] liste = r.lagRuterArray();
      for (int i = 0; i < liste.length; i++){
        if (liste[i] instanceof HvitRute && !(liste[i].giPaaVeien())){
          liste[i].harBesokt();
          return liste[i];
        }
      }
    } finally{
      laas.unlock();
    }
    return null;
  }

  public void finnUtvei(Rute erPaa, Rute start, String rute){

    //Stopper alt for a unnga a starte pa en sort rute
    if(erPaa instanceof SortRute){
      return;
    }

    //Endrer status slik at ikke den samme ruten skal ga pa flere ganger
    erPaa.harBesokt();
    //Dette er enden pa det rekusive kallet
    if (erPaa instanceof Aapning){
      //System.out.println(rute + erPaa.toString());
      try{
        laas.lock();
        liste.settInn(rute + erPaa.toString());
      } finally{
        laas.unlock();
      }
      //System.out.println("Fant utvei: " + erPaa.toString());
      return;
    }

    //Hvis det finnes flere veier aa velge mellom blir det husket her
    Rute forrige = tellHviteNaboer(erPaa) >= 2 ? erPaa : start;
    rute = rute + erPaa.toString();

    //Oppretter trader som kan bevege seg hver sin vei
    aktiverTrader(erPaa, rute);

    Rute neste = gaa(erPaa);

    if(neste == null){
      return;
    }

    //Rekkusive kall
    finnUtvei(neste, forrige, rute = rute + " --> ");
  }



  public void run(){
    Rute r = gaa(this);
    if (r != null){
      finnUtvei(r, this, ruteInfo);
      try{
        laas.lock();
        ANTALL_TRADER--;
        System.out.println(ANTALL_TRADER);
      } finally{
        laas.unlock();
      }
    }
  }


  //aktiverer trader og ber dem begynne
  public void aktiverTrader(Rute erPaa, String rute){
  if (tellHviteNaboer(erPaa) > 1){
    ruteInfo = rute + " -- > ";
    Thread[] traader = new Thread[tellHviteNaboer(erPaa)];

    for (int i = 0; i < traader.length; i++){
      traader[i] = new Thread(erPaa);
      traader[i].start();
      try{
        laas.lock();
        ANTALL_TRADER++;
        System.out.println(ANTALL_TRADER);
      } finally{
        laas.unlock();
      }
    }
  }
}

}
