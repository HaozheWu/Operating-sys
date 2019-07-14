import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class ReadersWritersApp
{
  // Configuration
  final static SyncConstructs sync = new SyncConstructs();
  
  public static void main(String args[]) {
     int NUM_READERS = 30;
     int NUM_WRITERS = 10;
    
    System.out.println ("num args: " + args.length);
    if (args.length > 0) {
      try {
        NUM_READERS = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {}
    }
    if (args.length > 1) {
      try {
        NUM_WRITERS = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {}
    }


    Reader [] rd = new Reader[NUM_READERS];
    Writer [] wr = new Writer[NUM_WRITERS];
    for (int i=0; i<NUM_READERS; i++) rd[i] = new Reader(sync);
    for (int i=0; i<NUM_WRITERS; i++) wr[i] = new Writer(sync);
    {
      int nRd = NUM_READERS;
      int nWr = NUM_WRITERS;
      while (nRd>0 || nWr>0){
   
        if (Math.random() > 0.5) {
          if (nRd > 0) { nRd--;  rd[nRd].start(); }
        }
        else { 
          if (nWr > 0) { nWr--; wr[nWr].start(); }
        }
      }
    }
  }
}




