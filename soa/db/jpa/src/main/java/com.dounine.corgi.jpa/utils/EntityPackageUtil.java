package com.dounine.corgi.jpa.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgq on 16-10-13.
 */
public class EntityPackageUtil {
    private static String base_package = "com.dounine.corgi.jpa.entity";
    private static List<String> packages = new ArrayList<>();

    public static String[] getPackages() {
        if(!packages.contains(base_package)){
            packages.add(0, base_package);
        }
        String[] array = new String[packages.size()];
        return  packages.toArray(array);
    }

    public static void setPackages(String entity_packages) {
        packages.add(entity_packages);
    }
}
