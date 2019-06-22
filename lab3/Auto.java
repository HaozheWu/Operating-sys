import java.util.concurrent.Semaphore;

class Auto extends Thread { // Class for the auto threads.

  private int id_auto;
  private int port;
  private Ferry fry;
  Logger logger;


  public Auto(int id, int prt, Ferry ferry, Logger logger)
  {
    this.id_auto = id;
    this.port = prt;
    this.fry = ferry;
    this.logger = logger;
  }

  public void run() {

    while (true) {
      // Delay
      try {sleep((int) (300*Math.random()));} catch (Exception e) {  break;}
      System.out.println("Auto " + id_auto + " arrives at port " + port);
    

      
      try{
        fry.loadingSemaphore[port].acquire();

        while(fry.getPort() != this.port || fry.loadingDone.availablePermits() == 0){
          try {sleep(100); } catch (InterruptedException e) { return;}
        }
  
  
        
        fry.addLoad();  // increment the ferry load
        System.out.println("Auto " + id_auto + " boards on the ferry at port " + port);
        logger.check (fry.getPort() == port, "error loading at wrong port");
        fry.loadingSemaphore[port].release();

        port = 1 - port ;   
        while(fry.getPort() != this.port ){
          try {sleep(100); } catch (Exception e) { break;}
        }

      }catch(InterruptedException ie){
        Thread.currentThread().interrupt();
        //System.out.println("Thread terminated by interruption2 ");  
        // handle the interrupt
        break; 
        
      }


      
      // Board

      //loadingSemaphore[port].release();
      
      // Arrive at the next port


      
      try {
      fry.unloadingSemaphore[port].acquire(); 

      fry.reduceLoad(); 
      // disembark    
      System.out.println("Auto " + id_auto + " disembarks from ferry at port " + port);
      logger.check (fry.getPort() == port, "error unloading at wrong port");
        // Reduce load
      fry.unloadingSemaphore[port].release(); 
      } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      //System.out.println("Thread terminated by interruption1");
      // handle the interrupt
      break; 
      
      }



      // Terminate
      if(isInterrupted()) break;
    }
    System.out.println("Auto "+id_auto+" terminated");
    //System.exit(0);  
  
  }
}
