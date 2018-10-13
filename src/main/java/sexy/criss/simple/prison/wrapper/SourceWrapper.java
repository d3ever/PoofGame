package sexy.criss.simple.prison.wrapper;

import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.HashMap;

abstract class SourceWrapper {

   protected Object instance;
   private static HashMap methods = new HashMap();
   protected static String v;


   static {
      String pkgName = Bukkit.getServer().getClass().getPackage().getName();
      v = "." + pkgName.substring(pkgName.lastIndexOf(46) + 1) + ".";
   }

   public SourceWrapper(Object instance) {
      this.instance = instance;
   }

   protected static Class loadClass(String start, String end) {
      try {
         return Bukkit.class.getClassLoader().loadClass(start + v + end);
      } catch (ClassNotFoundException var3) {
         return null;
      }
   }

   protected static void declareMethod(Class clazz, String name, Class ... parameterTypes) {
      try {
         methods.put(name, clazz.getMethod(name, parameterTypes));
      } catch (SecurityException | NoSuchMethodException ex) {
         ex.printStackTrace();
      }

   }

   protected Object invokeMethod(String name, Object ... args) {
      try {
         return ((Method)methods.get(name)).invoke(this.instance, args);
      } catch (Exception ex) {
         ex.printStackTrace();
         return null;
      }
   }

   protected static Object invokeStaticMethod(String name, Object ... args) {
      try {
         return ((Method)methods.get(name)).invoke(null, args);
      } catch (Exception ex) {
         ex.printStackTrace();
         return null;
      }
   }
}
