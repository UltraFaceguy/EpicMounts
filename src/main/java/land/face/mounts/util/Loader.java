package land.face.mounts.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Loader extends URLClassLoader {

    private final ClassLoader loader;

    public Loader(ClassLoader loader) {
        super(((URLClassLoader) loader).getURLs(), null);
        this.loader = loader;
    }

    @Override
    public URL[] getURLs() {
        if (!(loader instanceof URLClassLoader)) return new URL[0];
        return ((URLClassLoader) loader).getURLs();
    }


    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        try {
            return loader.loadClass(name);
        }
        catch (final ClassNotFoundException | NoClassDefFoundError ignored) { }
        throw new ClassNotFoundException(name);
    }

    @NotNull
    public <T> Collection<Class<? extends T>> find(@NotNull final Class<T> superClazz) {
        final ImmutableSet<ClassPath.ClassInfo> path;
        try {
            path = ClassPath.from(this).getTopLevelClassesRecursive("land.face.mounts");
        }
        catch (final IOException ex) {
            return Collections.emptyList();
        }

        if (path == null || path.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<Class<? extends T>> classes = new ArrayList<>();

        for (final ClassPath.ClassInfo info : path) {
            try {
                final Class<?> clazz = info.load();
                if (!superClazz.isAssignableFrom(clazz) || clazz == superClazz) {
                    continue;
                }
                classes.add(clazz.asSubclass(superClazz));
            }
            catch (final Exception ignored) { }
        }

        return classes;
    }
}