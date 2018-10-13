package sexy.criss.simple.prison.wrapper;

public class NBTTagList extends NBTBase {

   private static Class clazz = loadClass("net.minecraft.server", "NBTTagList");


   static {
      declareMethod(clazz, "size");
      declareMethod(clazz, "get", Integer.TYPE);
      declareMethod(clazz, "add", loadClass("net.minecraft.server", "NBTBase"));
   }

   public NBTTagList() throws InstantiationException, IllegalAccessException {
      super(clazz.newInstance());
   }

   public NBTTagList(Object instance) throws InstantiationException, IllegalAccessException {
      super(instance == null?clazz.newInstance():instance);
   }

   public int size() {
      return (Integer) this.invokeMethod("size");
   }

   public NBTTagCompound get(int index) throws InstantiationException, IllegalAccessException {
      return new NBTTagCompound(this.invokeMethod("get", index));
   }

   public void add(NBTBase base) {
      this.invokeMethod("add", base.instance);
   }
}
