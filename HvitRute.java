public class HvitRute extends Rute{
  public HvitRute(int x, int y, boolean paaVeien){
      super(x, y, paaVeien);
  }

  private String type = "hvit";

  public String type(){
    return type;
  }

  @Override
  public char tilTegn(){
    return '.';
  }

  @Override
  public boolean ruteType(String tekst){
    return tekst.equals("hvit");
  }

}
