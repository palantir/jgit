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

    abstract boolean shouldCopyThroughCache(long length);

    abstract int getBlockSize();

    abstract DfsPackFile getOrCreate(DfsPackDescription description, DfsPackKey key);

    abstract DfsBlock getOrLoad(DfsPackFile pack, long position, DfsReader ctx) throws IOException;

    abstract void put(DfsBlock v);

    abstract <T> DfsBlockCache.Ref<T> put(DfsPackKey key, long pos, int size, T v);

    abstract boolean contains(DfsPackKey key, long position);

    abstract <T> T get(DfsPackKey key, long position);

    abstract void remove(DfsPackFile pack);

    abstract void cleanUp();

    static abstract class Ref<T> {
        abstract T get();

        abstract boolean has();
    }

}
