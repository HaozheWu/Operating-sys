import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class SyncConstructs { 
  public final Semaphore wrMutex = new Semaphore(1);
  public final Semaphore rdMutex = new Semaphore(1);
  public final Semaphore allowWr = new Semaphore(0);
  public final Semaphore allowRd = new Semaphore(0);
  public final Semaphore dbMutex = new Semaphore(1);
  // public final Lock lock = new ReentrantLock();
  // public final Condition allowWr = lock.newCondition();
  // public final Condition allowRd = lock.newCondition();
  // boolean allowRd_event; allowWr_event;
  public int nReaders, nWriters, nWaitingReaders;

  public final Logger logger = new assertLooger();

  public SyncConstructs() {
    nReaders = 0;
    nWriters = 0;
    nWaitingReaders = 0;
  }
}
