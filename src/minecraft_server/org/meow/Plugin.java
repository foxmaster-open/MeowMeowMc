package org.meow;

public interface Plugin {
   void onEnable();
   void onDisable();
   String getName();
   String getVersion();
   void setEnabled(boolean enabled);
   boolean isEnabled();
   void setServer(Object server);
   Object getServer();
}
