package org.eclipse.jgit.internal.storage.dfs;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DfsBlockCache {

    private static volatile AtomicReference<DfsBlockCache> cache = new AtomicReference<>();

    static {
        DefaultDfsBlockCache.reconfigure(new DefaultDfsBlockCacheConfig());
    }

    public static void setInstance(DfsBlockCache newCache) {
        DfsBlockCache oldCache = cache.getAndSet(newCache);

        if (oldCache != null) {
            oldCache.cleanUp();
        }
    }

    /**
     * @return the currently active DfsBlockCache.
     */
    public static DfsBlockCache getInstance() {
        return cache.get();
    }

    public abstract boolean shouldCopyThroughCache(long length);

    public abstract int getBlockSize();

    public abstract DfsPackFile getOrCreate(DfsPackDescription description, DfsPackKey key);

    public abstract DfsBlock getOrLoad(DfsPackFile pack, long position, DfsReader ctx) throws IOException;

    public abstract void put(DfsBlock v);

    public abstract <T> DfsBlockCache.Ref<T> put(DfsPackKey key, long pos, int size, T v);

    public abstract boolean contains(DfsPackKey key, long position);

    public abstract <T> T get(DfsPackKey key, long position);

    public abstract void remove(DfsPackFile pack);

    public abstract void cleanUp();

    public static abstract class Ref<T> {
        public abstract T get();

        public abstract boolean has();
    }

}
