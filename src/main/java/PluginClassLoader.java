import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * In order to impose tight security restrictions on untrusted classes but
 * not on trusted system classes, we have to be able to distinguish between
 * those types of classes. This is done by keeping track of how the classes
 * are loaded into the system. By definition, any class that the interpreter
 * loads directly from the CLASSPATH is trusted. This means that we can't
 * load untrusted code in that way--we can't load it with Class.forName().
 * Instead, we create a ClassLoader subclass to load the untrusted code.
 * This one loads classes from a specified directory (which should not
 * be part of the CLASSPATH).
 */

public class PluginClassLoader extends ClassLoader {
    /** This is the directory from which the classes will be loaded */
    File directory;

    /** The constructor. Just initialize the directory */
    public PluginClassLoader (File dir) {
        directory = dir;
    }

    /** A convenience method that calls the 2-argument form of this method */
    public Class loadClass (String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    /**
     * This is one abstract method of ClassLoader that all subclasses must
     * define. Its job is to load an array of bytes from somewhere and to
     * pass them to defineClass(). If the resolve argument is true, it must
     * also call resolveClass(), which will do things like verify the presence
     * of the superclass. Because of this second step, this method may be called to
     * load superclasses that are system classes, and it must take this into account.
     */
    public Class loadClass (String classname, boolean resolve) throws ClassNotFoundException {
        try {
            // Our ClassLoader superclass has a built-in cache of classes it has
            // already loaded. So, first check the cache.
            Class c = findLoadedClass(classname);

            // After this method loads a class, it will be called again to
            // load the superclasses. Since these may be system classes, we've
            // got to be able to load those too. So try to load the class as
            // a system class (i.e. from the CLASSPATH) and ignore any errors
            if (c == null) {
                try { c = findSystemClass(classname); }
                catch (Exception ex) {}
            }

            // If the class wasn't found by either of the above attempts, then
            // try to load it from a file in (or beneath) the directory
            // specified when this ClassLoader object was created. Form the
            // filename by replacing all dots in the class name with
            // (platform-independent) file separators and by adding the ".class" extension.
            if (c == null) {
                // Figure out the filename
                String filename = classname.replace('.',File.separatorChar)+".class";

                // Create a File object. Interpret the filename relative to the
                // directory specified for this ClassLoader.
                File f = new File(directory, filename);

                // Get the length of the class file, allocate an array of bytes for
                // it, and read it in all at once.
                int length = (int) f.length();
                byte[] classbytes = new byte[length];
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                in.readFully(classbytes);
                in.close();

                // Now call an inherited method to convert those bytes into a Class
                c = defineClass(classname, classbytes, 0, length);
            }

            // If the resolve argument is true, call the inherited resolveClass method.
            if (resolve) resolveClass(c);

            // And we're done. Return the Class object we've loaded.
            return c;
        }
        // If anything goes wrong, throw a ClassNotFoundException error
        catch (Exception ex) { throw new ClassNotFoundException(ex.toString()); }
    }

static List<PlugTest> loadAllJarClasses(String jarpath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    URL[] urls = { new URL("jar:file:" + jarpath+"!/") };
    URLClassLoader cl = URLClassLoader.newInstance(urls);
        List<PlugTest>plugins=new ArrayList<>();
    JarFile jarFile = new JarFile(jarpath);
    Enumeration<JarEntry> e = jarFile.entries();

    while (e.hasMoreElements()) {
        JarEntry je = e.nextElement();
        if(je.isDirectory() || !je.getName().endsWith(".class")){
            continue;
        }
        // -6 because of .class
        String className = je.getName().substring(0,je.getName().length()-6);
        className = className.replace('/', '.');
        Class c = cl.loadClass(className);
        Class[] intf = c.getInterfaces();
        for (int j=0; j<intf.length; j++) {
            if (intf[j].getName().equals("PlugTest")) {
                // the following line assumes that PluginFunction has a no-argument constructor
                PlugTest pf = (PlugTest) c.newInstance();
                plugins.add(pf);
                 System.out.println("loaded "+pf.getClass()+" from "+jarFile.getName());
                continue;
            }
        }

    }
    return plugins;
}

    static List<PlugTest> getPlugins(String pluginsDir) {
        List<PlugTest>plugins=new ArrayList<>();
        File dir = new File(System.getProperty("user.dir") + File.separator + pluginsDir);
        ClassLoader cl = new PluginClassLoader(dir);
        if (dir.exists() && dir.isDirectory()) {
            // we'll only load classes directly in this directory -
            // no subdirectories, and no classes in packages are recognized
            String[] files = dir.list();
            for (int i=0; i<files.length; i++) {
                try {
                    // only consider files ending in ".class"
                    if (files[i].endsWith(".jar")){

                    plugins.addAll(loadAllJarClasses(dir+File.separator+files[i]));

                    }


                    if (! files[i].endsWith(".class"))
                        continue;

                    Class c = cl.loadClass(files[i].substring(0, files[i].indexOf(".")));
                    Class[] intf = c.getInterfaces();
                    for (int j=0; j<intf.length; j++) {
                        if (intf[j].getName().equals("PlugTest")) {
                            // the following line assumes that PluginFunction has a no-argument constructor
                            PlugTest pf = (PlugTest) c.newInstance();
                            plugins.add(pf);
                             System.out.println("loaded "+pf.getClass()+" from "+dir);
                            continue;
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("File " + files[i] + " does not contain a valid PluginFunction class.");
                }
            }
        }
        return plugins;
    }

}