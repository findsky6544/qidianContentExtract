package main;


import java.io.FileNotFoundException;

public class Index
{
  public static void main(String[] args)
    throws FileNotFoundException
  {
    DeleteFrame frame = new DeleteFrame();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(3);
    frame.setVisible(true);
  }
}
