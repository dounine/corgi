package com.dounine.corgi.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by huanghuanlai on 2016/11/18.
 */
public final class ClassUtils {
    private ClassUtils() {
    }


    public static List<Class<?>> getAllAssignedClass(Class<?> cls) throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    public static final Set<Class<?>> queryInterfaceByImpls(Collection<Class<?>> list, Class<?> face) {
        Set<Class<?>> implClass = new HashSet<>();
        if (null != list && null != face) {
            for (Class<?> impl : list) {
                if (face.isAssignableFrom(impl)) {
                    implClass.add(impl);
                }
            }
        }
        return implClass;
    }

//    public static void main(String[] args) {
//        Set<Class<?>> list = new HashSet<>();
//        list.add(InterImpl1.class);
//        list.add(InterImpl2.class);
//        list.add(JsonpUtils.class);
//        for(Class impl : queryInterfaceByImpls(list,Inter.class)){
//            System.out.println(impl);
//        }
//    }
}
