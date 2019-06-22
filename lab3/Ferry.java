import java.util.concurrent.Semaphore;

class Ferry extends Thread{ // The ferry Class
  final static int MAXLOAD = 5;
  private int port=0;  // Start at port 0
  private int load=0;  // Load is zero
  private int numCrossings;  // number of crossings to execute
  private boolean ambulance_loaded = false;
  Logger logger;
  // Semaphores
  Semaphore loadingDone = new Semaphore(0) ; 
  Semaphore unloadingDone  = new Semaphore(1); 
  Semaphore[] loadingSemaphore = 
                                  new Semaphore[]{new Semaphore(1),new Semaphore(1)}  ; 
  Semaphore[] unloadingSemaphore = 
                                  new Semaphore[]{new Semaphore(1),new Semaphore(1)}  ; 

  public Ferry(int prt, int nbtours, Logger logger)
  {
    this.port = prt;
    numCrossings = nbtours;
    this.logger = logger;

  }

  public void run() {
    try {sleep(2000);  } catch (Exception e) { System.out.println(e);}
    System.out.println("Start at port " + port + " with a load of " + load + " vehicles");
    //loadingDone.release(MAXLOAD); 
    // numCrossings crossings in our day
    for(int i=0 ; i < numCrossings ; i++) {
      

      while(load != 0){
        try {sleep(100); } catch (Exception e) { break;}
      }

      loadingDone.release(MAXLOAD);

      try {
        unloadingDone.acquire();
        //unloadingDone.release(load);
        
        } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        System.out.println("Thread terminated by interruption");
        // handle the interrupt
        return;
        }

      while(loadingDone.availablePermits()!=0){
        try {sleep(100); } catch (Exception e) { break;}
      } 


      
      // The crossing
      System.out.println("Departure from port " + port + " with a load of " + load + " vehicles");
      System.out.println("Crossing " + i + " with a load of " + load + " vehicles");
      if (ambulance_loaded) {
        logger.check(load > 0 && load <= MAXLOAD, "error ferry leaving with less load! ");
      } 
      else {
        logger.check(load == MAXLOAD, "error ferry leaving with less load!, load is " + load + " ");
      }
      port = 1 - port;
      //loadingSemaphore[1-port].release();
      try {sleep((int) (100*Math.random()));} catch (Exception e) { }
      // Arrive at port
      System.out.println("Arrive at port " + port + " with a load of " + load + " vehicles");
      // Disembarkment et loading
      
      
      try {
        unloadingDone.acquire();
        } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        System.out.println("Thread terminated by interruption");
        // handle the interrupt
        return;
        }
      
      unloadingDone.release();
      

     // System.out.println("Done here");
      
    }

    
     
  }

  // methodes to manipulate the load of the ferry
  public int getLoad() { return(load); }
  public int getPort() { return(port); }
  public synchronized void addLoad() {
    
    try{

     
      
      loadingDone.acquire();
      logger.check(load < MAXLOAD, "error loadig in a full Ferry!");
      load = load + 1; 
      System.out.println ("added load, now " + load);
      //System.out.println(loadingDone.availablePermits());
      
    }catch(InterruptedException  e){
      Thread.currentThread().interrupt();
      return ; 
      //System.out.print(e);
    }
    //System.out.println("llllll");


    // if( ambulance_loaded == true ||load == 5 ){
      
    // }else{
    //   loadingSemaphore[port].release();
    // }
    
  }
  public   void reduceLoad()  { 
    

    logger.check(load > 0, "error unloading an empty Ferry!");
    load = load - 1 ; 
    System.out.println ("removed load, now " + load);

    if(load == 0 ){
      unloadingDone.release();
    }
  

  }
  public synchronized  void loadAmbulance() {
    
    
    ambulance_loaded = true;

    addLoad();
    
    loadingDone.drainPermits();
  }
  public   void unloadAmbulance(){    
    ambulance_loaded = false;
    reduceLoad();
    
  }

  // public Semaphore getLoadSem(int port ){
  //   return loadingSemaphore[port]; 
  // }
  // public Semaphore getUnloadSem(int  port){
  //   return unloadingSemaphore[port]; 
  // }


}
