public class SortRute extends Rute{
  public SortRute(int x, int y, boolean paaVeien){
      super(x, y, paaVeien);
  }

  private String type = "sort";

  public String type(){
    return type;
  }

  @Override
  public char tilTegn(){
    return '#';
  }

  @Override
  public boolean ruteType(String tekst){
    return tekst.equals("sort");
  }

}
