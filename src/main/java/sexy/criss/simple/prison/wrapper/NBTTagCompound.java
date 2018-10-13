package sexy.criss.simple.prison.wrapper;

public class NBTTagCompound extends NBTBase {

   private static Class clazz = loadClass("net.minecraft.server", "NBTTagCompound");


   static {
      declareMethod(clazz, "setDouble", String.class, Double.TYPE);
      declareMethod(clazz, "getDouble", String.class);
      declareMethod(clazz, "setInt", String.class, Integer.TYPE);
      declareMethod(clazz, "getInt", String.class);
      declareMethod(clazz, "setString", String.class, String.class);
      declareMethod(clazz, "getString", String.class);
      declareMethod(clazz, "setLong", String.class, Long.TYPE);
      declareMethod(clazz, "getLong", String.class);
      declareMethod(clazz, "set", String.class, loadClass("net.minecraft.server", "NBTBase"));
      declareMethod(clazz, "getList", String.class, Integer.TYPE);
      declareMethod(clazz, "hasKey", String.class);
   }

   public NBTTagCompound() throws InstantiationException, IllegalAccessException {
      super(clazz.newInstance());
   }

   public NBTTagCompound(Object instance) throws InstantiationException, IllegalAccessException {
      super(instance == null?clazz.newInstance():instance);
   }

   public void setDouble(String key, double value) {
      this.invokeMethod("setDouble", key, value);
   }

   public double getDouble(String key) {
      return (Double) this.invokeMethod("getDouble", key);
   }

   public void setInt(String key, int value) {
      this.invokeMethod("setInt", key, value);
   }

   public int getInt(String key) {
      return (Integer) this.invokeMethod("getInt", key);
   }

   public void setString(String key, String value) {
      this.invokeMethod("setString", key, value);
   }

   public String getString(String key) {
      return (String)this.invokeMethod("getString", key);
   }

   public void setLong(String key, long value) {
      this.invokeMethod("setLong", key, value);
   }

   public long getLong(String key) {
      return (Long) this.invokeMethod("getLong", key);
   }

   public void set(String key, NBTBase value) {
      this.invokeMethod("set", key, value.instance);
   }

   public void set(String key, NBTTagList value) {
      this.invokeMethod("set", key, value.instance);
   }

   public NBTTagList getList(String key, int paramInt) {
      try {
         return new NBTTagList(this.invokeMethod("getList", key, paramInt));
      } catch (Exception ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public boolean hasKey(String key) {
      return (Boolean) this.invokeMethod("hasKey", new Object[]{key});
   }
}
