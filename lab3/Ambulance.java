import java.util.concurrent.Semaphore;
class Ambulance extends Thread { // the Class for the Ambulance thread

  private int id;
  private int port;
  private Ferry fry;
  Logger logger;


  public Ambulance(int id, int prt, Ferry ferry, Logger logger)
  {
    this.port = prt;
    this.fry = ferry;
    this.id = id;
    this.logger = logger;
  }

  public void run() {
     while (true) {
      // Attente
      try {sleep((int) (1000*Math.random()));} catch (Exception e) { break;}
      System.out.println("Ambulance " + id + " arrives at port " + port);
  
      // try{
      //   loadingSemaphore[port].acquire();
      // }catch(InterruptedException ie){
      //   Thread.currentThread().interrupt();
      //   System.out.println("Thread terminated by interruption");
      //   // handle the interrupt
      //   return;
      // }  
      try{

        
        fry.loadingSemaphore[port].acquire();
        while(fry.getPort() != this.port || fry.loadingDone.availablePermits() == 0  ){
          try {sleep(100);  } catch (InterruptedException e) { return;}
        }
        //System.out.println("kkkkkkkkkkkkk2"+fry.loadingDone.availablePermits() );
        fry.loadAmbulance();  // increment the ferry load
        System.out.println("Ambulance " + id + " boards the ferry at port " + port);
        logger.check (fry.getPort() == port, "error loading at wrong port");
        fry.loadingSemaphore[port].release();
        //System.out.println("kkkkkkkkkkkkk3");
  
        
  
        // Board
  
         // increment the load  
        
        // Arrive at the next port
        port = 1 - port ;   
  
        while(fry.getPort() != this.port ){
          try {sleep(100);   } catch (Exception e) { break;}
        }
        
      }catch(InterruptedException ie){
        Thread.currentThread().interrupt();
        //System.out.println("Thread terminated by interruption");
        // handle the interrupt
        break; 
      }
      //System.out.println("kkkkkkkkkkkkk1");

      //System.out.println("kkkkkkkkkkkkk4");
      try {
      fry.unloadingSemaphore[port].acquire(); 
      fry.unloadAmbulance();
      //Disembarkment    
      System.out.println("Ambulance " + id + " disembarks the ferry at port " + port);
      logger.check(fry.getPort() == port, "error unloading at wrong port");
         // Reduce load
      fry.unloadingSemaphore[port].release(); 
      } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      //System.out.println("Thread terminated by interruption ");
      // handle the interrupt
      break;
      }
      //System.out.println("kkkkkkkkkkkkk5");

      // Terminate
      if(isInterrupted()) break;
      //System.out.println("kkkkkkkkkkkkk6");
    }
    System.out.println("Ambulance " + id + " terminated.");
    //System.exit(0);  
  }
}
