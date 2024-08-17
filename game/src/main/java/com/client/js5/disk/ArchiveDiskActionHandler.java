package com.client.js5.disk;

import com.client.collection.node.NodeDeque;
import net.runelite.rs.api.RSArchiveDiskActionHandler;

public class ArchiveDiskActionHandler implements Runnable, RSArchiveDiskActionHandler {

    public static NodeDeque requestQueue;
    public static NodeDeque responseQueue;
    public static Object lock;
    public static int numPendingActions = 0;
    static boolean flag;
    
    static {
        requestQueue = new NodeDeque();
        responseQueue = new NodeDeque();
        numPendingActions = 0;
        flag = false;
        lock = new Object();
    }

    public void run() {
        try {
            while (true) {
                ArchiveDiskAction var1;
                synchronized(requestQueue) {
                    var1 = (ArchiveDiskAction)requestQueue.last();
                }

                if (var1 != null) {
                    if (var1.type == 0) {
                        var1.archiveDisk.write((int)var1.key, var1.data, var1.data.length);
                        synchronized(requestQueue) {
                            var1.remove();
                        }
                    } else if (var1.type == 1) {
                        var1.data = var1.archiveDisk.read((int)var1.key);
                        synchronized(requestQueue) {
                            responseQueue.addFirst(var1);
                        }
                    }

                    synchronized(lock) {
                        if ((flag || numPendingActions <= 1) && requestQueue.method2028()) {
                            numPendingActions = 0;
                            lock.notifyAll();
                            return;
                        }

                        numPendingActions = 600;
                    }
                } else {
                    sleep(100L);
                    synchronized(lock) {
                        if ((flag || numPendingActions <= 1) && requestQueue.method2028()) {
                            numPendingActions = 0;
                            lock.notifyAll();
                            return;
                        }

                        --numPendingActions;
                    }
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }
    }

    public static final void sleep(long var0) {
        if (var0 > 0L) {
            if (0L == var0 % 10L) {
                long var2 = var0 - 1L;

                try {
                    Thread.sleep(var2);
                } catch (InterruptedException var8) {
                }

                try {
                    Thread.sleep(1L);
                } catch (InterruptedException var7) {
                }
            } else {
                try {
                    Thread.sleep(var0);
                } catch (InterruptedException var6) {
                }
            }

        }
    }

    public static void processPendingActions() {
        synchronized (lock) {
            if (numPendingActions == 0) {
                Thread thread = new Thread(new ArchiveDiskActionHandler());
                thread.setDaemon(true);
                thread.start();
                thread.setPriority(5);
            }

            numPendingActions = 600;
            flag = false;
        }
    }

    public static void waitForPendingArchiveDiskActions() {
        synchronized(ArchiveDiskActionHandler.lock) {
            if (ArchiveDiskActionHandler.numPendingActions != 0) {
                ArchiveDiskActionHandler.numPendingActions = 1;

                try {
                    ArchiveDiskActionHandler.lock.wait();
                } catch (InterruptedException ignored) {
                }
            }

        }
    }

    public static void processArchiveDiskActions() {
        while(true) {
            ArchiveDiskAction var1;
            synchronized(requestQueue) {
                var1 = (ArchiveDiskAction) responseQueue.removeLast();
            }

            if (null == var1) {
                return;
            }

            var1.js5Archive.processArchiveData(var1.archiveDisk, (int)var1.key, var1.data, false);
        }
    }

}