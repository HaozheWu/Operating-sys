
class Reader extends Thread{ // The ferry Class

  SyncConstructs sync;
  public Reader(SyncConstructs sync) { this.sync = sync; }

  private void read() {
    sync.logger.log("start read");
    sync.logger.check(Writer.writting ==0, "start read error currently writting : " + Writer.writting + " ");
    try {sleep((int) (20*Math.random()));} catch (Exception e) { }
    sync.logger.log("end read");
  }
  private void pre_read() {

    try{

      sync.wrMutex.acquire();
      sync.logger.log("Writer sync.wrMutex.acquire, number of writers: " + sync.nWriters );

      if(sync.nWriters > 0 ){
        sync.wrMutex.release();
        sync.logger.log("Writer sync.wrMutex.release, number of writers: " + sync.nWriters);
        sync.allowRd.acquire();
        sync.logger.log("Writer sync.allowRd.acquire, semaphore status: " + sync.allowRd.availablePermits());
  
  
      }else {
        sync.wrMutex.release();
        sync.logger.log("Writer sync.wrMutex.release, number of writers: " + sync.nWriters);
      }
  
      sync.rdMutex.acquire();
      sync.logger.log("Writer sync.rdMutex.acquire, number of readers: " + sync.nReaders);
      if(sync.nReaders==0){
        sync.dbMutex.acquire();
        sync.logger.log("Writer sync.dbMutex.acquire, mutex status: " + sync.dbMutex.availablePermits());
      }
  
      sync.nReaders++; 
      sync.rdMutex.release();
      sync.logger.log("Writer sync.rdMutex.release, number of readers: " + sync.nReaders);
  
    }catch(InterruptedException ie ){
      Thread.currentThread().interrupt();
      
      return ; 
    }



  }
  private void post_read() {

    try{
      sync.logger.log("Writer sync.rdMutex.acquire, number of readers: " + sync.nReaders );
      sync.rdMutex.acquire();
      sync.nReaders-- ; 
      if ( sync.nReaders == 0 ){
        sync.dbMutex.release();
        sync.logger.log("Writer sync.dbMutex.release, mutex status: " + sync.dbMutex.availablePermits() );
      }
  
      sync.rdMutex.release();
      sync.logger.log("Writer sync.rdMutex.release, number of readers: " + sync.nReaders );
    }catch(InterruptedException ie ){
      Thread.currentThread().interrupt();
      
      return ; 
    }

  

  }
  private void read_monitor(){
    try {sleep((int) (5000*Math.random()) + 2000);} catch (Exception e) { }
    pre_read();
    read();
    post_read();
  }

  public void run() {
    int n = ((int) (80*Math.random()) + 100);
    for (int i=0; i < n; i++) read_monitor();
  }

}
