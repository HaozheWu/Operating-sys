
class Writer extends Thread{ // The ferry Class

  SyncConstructs sync;
  static int writting = 0;
  public Writer(SyncConstructs sync) { this.sync = sync; }

  private void write() {
    sync.logger.log("start write");
    sync.logger.check(writting == 0, "start write error currently writting : " + writting);
    writting++;
    try {sleep((int) (1000*Math.random()));} catch (Exception e) { }
    writting--;
    sync.logger.check(writting == 0, "end write error currently writting : " + writting);
    sync.logger.log("end write");
  }
  private void pre_write() {

    try{
      sync.wrMutex.acquire();
      sync.logger.log("Writer sync.wrMutex.acquire, number of writers: " + sync.nWriters);

      if (sync.nWriters>0) {
        sync.nWriters++; 
        sync.wrMutex.release();
        sync.logger.log("Writer sync.wrMutex.release, number of writers: " + sync.nWriters);
        sync.allowWr.acquire(); 
        sync.logger.log("Writer sync.allowWr.acquire, semaphore status: " + sync.allowWr.availablePermits() );
      }else{
        sync.nWriters++; 
        sync.wrMutex.release();
        sync.logger.log("Writer sync.wrMutex.release, number of writers: " + sync.nWriters );
      }
  
      sync.dbMutex.acquire();
      sync.logger.log("Writer sync.dbMutex.acquire, mutex status: " + sync.dbMutex.availablePermits());
    }catch(InterruptedException ie ){

      Thread.currentThread().interrupt();
      
      return ; 
    }





  }
  private void post_write() {

    try{
      sync.dbMutex.release();
      sync.logger.log("Writer sync.dbMutex.release, mutex status: " + sync.dbMutex.availablePermits() );
      sync.wrMutex.acquire(); 
      sync.logger.log("Writer sync.wrMutex.acquire, number of writers: " + sync.nWriters);
      sync.nWriters -- ; 
  
      if(sync.nWriters > 0 ){
  
        sync.allowWr.release();
        sync.logger.log("Writer sync.allowWr.release, semaphore status: " + sync.allowWr.availablePermits());
      }else{
        sync.allowRd.release();
        sync.logger.log("Writer sync.allowRd.release, semaphore status: " + sync.allowRd.availablePermits());
      }
  
      sync.wrMutex.release();
      sync.logger.log("Writer sync.wrMutex.release, number of writers: " + sync.nWriters);
    }catch(InterruptedException ie){
      Thread.currentThread().interrupt();
      
      return ; 

    }



  }
  private void write_monitor(){
    try {sleep((int) (10000*Math.random()) + 5000);} catch (Exception e) { }
    pre_write();
    write();
    post_write();
  }
  
  public void run() {
    int n = ((int) (20*Math.random()) + 10);
    for (int i=0; i < n; i++) write_monitor();
  }

}
