public class Aapning extends HvitRute{
  public Aapning(int x, int y, boolean paaVeien){
      super(x, y, paaVeien);
  }

  private String type = "aapning";


  @Override
  public String type(){
    return type;
  }

  @Override
  public char tilTegn(){
    return '.';
  }
}
