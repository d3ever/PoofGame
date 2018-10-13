package sexy.criss.simple.prison.wrapper;

import org.bukkit.inventory.ItemStack;

public class CraftItemStack extends SourceWrapper {

   private static Class clazz = loadClass("org.bukkit.craftbukkit", "inventory.CraftItemStack");


   static {
      declareMethod(clazz, "asNMSCopy", ItemStack.class);
      declareMethod(clazz, "asCraftMirror", loadClass("net.minecraft.server", "ItemStack"));
   }

   public CraftItemStack() throws InstantiationException, IllegalAccessException {
      super(clazz.newInstance());
   }

   public CraftItemStack(Object instance) {
      super(instance);
   }

   public static MinecraftItemStack asNMSCopy(ItemStack stack) {
      return new MinecraftItemStack(invokeStaticMethod("asNMSCopy", stack));
   }

   public static CraftItemStack asCraftMirror(MinecraftItemStack stack) {
      return new CraftItemStack(invokeStaticMethod("asCraftMirror", stack.instance));
   }

   public ItemStack getStack() {
      return (ItemStack)this.instance;
   }
}
