import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Labyrint{

  private static Rute[][] brett;
  private static int rad;
  private static int kolonne;
  private final int storrelse;
  private static boolean skriv = true;


  //Siden en Labyrint kun kan lages av fra fil saa er den private.
  private Labyrint(int rad, int kolonne){
    this.rad = rad;
    this.kolonne = kolonne;
    storrelse = rad * kolonne;
    brett = new Rute[rad][kolonne];
  }

  //Setter opp Labyrinten og definerer alle ruter
  public static Labyrint lesFraFil(File filnavn) throws FileNotFoundException{

    try{
      Scanner fil = new Scanner(filnavn);

      //Bestemmer dimensjonene paa Labyrinten.
      String beggeTall = fil.nextLine();
      String[] sArray = beggeTall.split(" ");
      int antallRader = Integer.parseInt(sArray[0]);
      int antallKolonner = Integer.parseInt(sArray[1]);
      Labyrint lab = new Labyrint(antallRader, antallKolonner);

      //Leser inn alle linjene som utgjor laberinten.
      int x = 0;
      while (fil.hasNext()){
        String linje = fil.nextLine();
        int k = x +1;
        if (skriv){
          System.out.println(linje);
        }
        String[] linjeStykke = linje.split("");
        int y = 0;
        for(String s : linjeStykke){
          sortHvitEllerAapning(s, x, y, antallRader, antallKolonner);
          y++;
        }
        x++;
      }
      settSorOst(antallRader, antallKolonner);
      return lab;
    }catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  //Test for a finne ut om det er en aapning, hvit eller sort rute
  private static void sortHvitEllerAapning(String symbol, int x, int y, int maxX, int maxY){
    Rute nord = (x - 1 < 0) ? null : brett[x-1][y];
    Rute vest = (y - 1 < 0) ? null : brett[x][y-1];
    if (symbol.equals(".")){
      if (erAapen(x, y, maxX, maxY)){
        brett[x][y] = new Aapning(x, y, false);
      } else {
        brett[x][y] = new HvitRute(x, y, false);
      }
    } else if (symbol.equals("#")){
      brett[x][y] = new SortRute(x, y, false);
    } else {
      System.out.println("Noe er galt: " + symbol);
    }
    brett[x][y].settNorVest(nord, vest);
  }

  public static void settMinimalUtskrift(){
    skriv = false;
  }

  private static void settSorOst(int maxX, int maxY){
    for (int x = 0; x < maxX; x++){
      for (int y = 0; y < maxY; y++){
        Rute sor = (x + 1 >= maxX) ? null : brett[x+1][y];
        Rute ost = (y + 1 >= maxY) ? null : brett[x][y+1];
        brett[x][y].settSorOst(sor, ost);
      }
    }
  }


  public static OrdnetLenkeliste<String> finnUtveiFra(int k, int r){
    brett[r][k].finnUtvei(brett[r][k], brett[r][k], "");
    try{
      Thread.sleep(100);
    } catch(InterruptedException e){}

      for (int x = 0; x < rad; x++){
        for (int y = 0; y < kolonne; y++){
          if (brett[x][y] instanceof HvitRute){
          brett[x][y].resett();
          }
        }
      }

      return brett[r][k].returnerListe();
    }

    private static boolean erAapen(int x, int y, int maxX, int maxY){
      if (x == 0||y == 0||x == maxX-1||y == maxY-1){
        return true;
      }
      return false;
    }
  }
