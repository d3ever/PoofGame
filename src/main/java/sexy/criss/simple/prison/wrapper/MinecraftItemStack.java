package sexy.criss.simple.prison.wrapper;

public class MinecraftItemStack extends SourceWrapper {

   private static Class clazz = loadClass("net.minecraft.server", "ItemStack");


   public MinecraftItemStack() throws InstantiationException, IllegalAccessException {
      super(clazz.newInstance());
   }

   public MinecraftItemStack(Object instance) {
      super(instance);
   }

   public void setTag(NBTTagCompound tag) {
      try {
         clazz.getField("tag").set(this.instance, tag.instance);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

   }

   public NBTTagCompound getTag() {
      try {
         return new NBTTagCompound(clazz.getField("tag").get(this.instance));
      } catch (Exception ex) {
         ex.printStackTrace();
         return null;
      }
   }
}
