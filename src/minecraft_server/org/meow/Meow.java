package org.meow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// ==================== ²å¼þÃèÊö ====================
class PluginDescription {
   private String name;
   private String version;
   private String main;
   private List<String> depend;
   private List<String> authors;
   private String description;

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   public String getVersion() { return version; }
   public void setVersion(String version) { this.version = version; }
   public String getMain() { return main; }
   public void setMain(String main) { this.main = main; }
   public List<String> getDepend() { return depend; }
   public void setDepend(List<String> depend) { this.depend = depend; }
   public List<String> getAuthors() { return authors; }
   public void setAuthors(List<String> authors) { this.authors = authors; }
   public String getDescription() { return description; }
   public void setDescription(String description) { this.description = description; }
}

// ==================== ²å¼þÀà¼ÓÔØÆ÷ ====================
class PluginClassLoader extends URLClassLoader {
   private final Map<String, Class<?>> classes;
   private final PluginDescription description;
   private final File file;
   private Plugin plugin;

   public PluginClassLoader(File file, ClassLoader parent) throws Exception {
      super(new URL[]{file.toURI().toURL()}, parent);
      this.classes = new HashMap<String, Class<?>>();
      this.file = file;
      this.description = null;
   }

   public PluginClassLoader(File file, ClassLoader parent, PluginDescription description) throws Exception {
      super(new URL[]{file.toURI().toURL()}, parent);
      this.classes = new HashMap<String, Class<?>>();
      this.file = file;
      this.description = description;
   }

   protected Class<?> findClass(String name) throws ClassNotFoundException {
      Class<?> result = this.classes.get(name);
      if (result == null) {
         result = super.findClass(name);
         this.classes.put(name, result);
      }
      return result;
   }

   public Plugin getPlugin() { return this.plugin; }
   public void setPlugin(Plugin plugin) { this.plugin = plugin; }
   public PluginDescription getDescription() { return this.description; }
   public File getFile() { return this.file; }
   public Map<String, Class<?>> getClasses() { return this.classes; }
}

// ==================== ²å¼þ¼ÓÔØÆ÷ ====================
class PluginLoader {

   private static final Logger LOGGER = LogManager.getLogger(PluginLoader.class);

   private final Object server;
   private final Map<String, Plugin> plugins;
   private final Map<String, PluginClassLoader> loaders;
   private final Map<String, PluginDescription> descriptions;
   private final File pluginDir;
   private final Yaml yaml;

   public PluginLoader(Object server) {
      this.server = server;
      this.plugins = new HashMap<String, Plugin>();
      this.loaders = new HashMap<String, PluginClassLoader>();
      this.descriptions = new HashMap<String, PluginDescription>();
      this.pluginDir = new File("plugins");
      this.yaml = new Yaml();
   }

   public void loadPlugins() {
      if (!this.pluginDir.exists()) {
         return;
      }

      File[] files = this.pluginDir.listFiles();
      if (files == null || files.length == 0) {
         return;
      }

      for (File file : files) {
         if (file.isFile() && file.getName().endsWith(".jar")) {
            try {
               loadJarPlugin(file);
            } catch (Exception e) {
               LOGGER.error("Cannot load plugin: " + file.getName() + " - " + e.getMessage());
               e.printStackTrace();
            }
         }
      }

      if (!this.plugins.isEmpty()) {
         enablePlugins();
      }
   }

   private void loadJarPlugin(File file) throws Exception {
      PluginDescription desc = parsePluginDescription(file);
      if (desc == null) {
         return;
      }

      List<String> dependList = desc.getDepend();
      if (dependList != null) {
         for (String depend : dependList) {
            if (!this.plugins.containsKey(depend) && !this.loaders.containsKey(depend)) {
               LOGGER.error("Cannot load plugin {}: missing dependency '{}' for '{}'",
                       file.getName(), depend, desc.getName());
               return;
            }
         }
      }

      PluginClassLoader loader = new PluginClassLoader(file, Thread.currentThread().getContextClassLoader(), desc);

      Class<?> mainClass = loader.loadClass(desc.getMain());

      boolean implementsPlugin = false;
      for (Class<?> iface : mainClass.getInterfaces()) {
         if (iface.getName().equals("org.meow.Plugin")) {
            implementsPlugin = true;
            break;
         }
      }
      if (!implementsPlugin) {
         LOGGER.error("Cannot load plugin {}: main class does not implement Plugin interface", file.getName());
         return;
      }

      Plugin plugin = (Plugin) mainClass.newInstance();
      plugin.setServer(this.server);
      loader.setPlugin(plugin);

      this.plugins.put(desc.getName(), plugin);
      this.loaders.put(desc.getName(), loader);
      this.descriptions.put(desc.getName(), desc);
   }

   private PluginDescription parsePluginDescription(File file) throws Exception {
      JarFile jar = null;
      try {
         jar = new JarFile(file);
         JarEntry entry = jar.getJarEntry("plugin.yml");
         if (entry == null) {
            entry = jar.getJarEntry("plugin.yaml");
            if (entry == null) {
               entry = jar.getJarEntry("meowmeow.yml");
               if (entry == null) {
                  return null;
               }
            }
         }

         InputStream input = jar.getInputStream(entry);
         Map<String, Object> data = this.yaml.loadAs(input, Map.class);
         input.close();

         PluginDescription desc = new PluginDescription();

         if (data.containsKey("name")) {
            desc.setName(data.get("name").toString());
         }
         if (data.containsKey("version")) {
            desc.setVersion(data.get("version").toString());
         }
         if (data.containsKey("main")) {
            desc.setMain(data.get("main").toString());
         }
         if (data.containsKey("description")) {
            desc.setDescription(data.get("description").toString());
         }

         if (data.containsKey("authors")) {
            Object authorsObj = data.get("authors");
            if (authorsObj instanceof List) {
               List<String> authors = new ArrayList<String>();
               for (Object o : (List<?>) authorsObj) {
                  authors.add(o.toString());
               }
               desc.setAuthors(authors);
            }
         } else if (data.containsKey("author")) {
            List<String> authors = new ArrayList<String>();
            authors.add(data.get("author").toString());
            desc.setAuthors(authors);
         }

         if (data.containsKey("depend")) {
            Object dependObj = data.get("depend");
            if (dependObj instanceof List) {
               List<String> depend = new ArrayList<String>();
               for (Object o : (List<?>) dependObj) {
                  depend.add(o.toString());
               }
               desc.setDepend(depend);
            }
         }

         if (desc.getName() == null || desc.getName().isEmpty()) {
            throw new Exception("Plugin name is required in " + entry.getName());
         }
         if (desc.getVersion() == null || desc.getVersion().isEmpty()) {
            desc.setVersion("1.0.0");
         }
         if (desc.getMain() == null || desc.getMain().isEmpty()) {
            throw new Exception("Main class is required in " + entry.getName());
         }

         return desc;

      } finally {
         if (jar != null) {
            try { jar.close(); } catch (Exception e) {}
         }
      }
   }

   private void enablePlugins() {
      if (this.plugins.isEmpty()) {
         return;
      }

      for (Plugin plugin : sortByDependency()) {
         try {
            plugin.onEnable();
            plugin.setEnabled(true);
         } catch (Exception e) {
            LOGGER.error("Failed to enable plugin: " + plugin.getName());
            e.printStackTrace();
         }
      }
   }

   private List<Plugin> sortByDependency() {
      List<Plugin> result = new ArrayList<Plugin>();
      List<Plugin> remaining = new ArrayList<Plugin>(this.plugins.values());

      while (!remaining.isEmpty()) {
         boolean progress = false;
         for (Plugin plugin : remaining) {
            PluginDescription desc = this.descriptions.get(plugin.getName());
            if (desc == null || desc.getDepend() == null || desc.getDepend().isEmpty()) {
               result.add(plugin);
               remaining.remove(plugin);
               progress = true;
               break;
            }

            boolean depsMet = true;
            for (String dep : desc.getDepend()) {
               if (!this.plugins.containsKey(dep) || !result.contains(this.plugins.get(dep))) {
                  depsMet = false;
                  break;
               }
            }
            if (depsMet) {
               result.add(plugin);
               remaining.remove(plugin);
               progress = true;
               break;
            }
         }
         if (!progress) {
            result.addAll(remaining);
            break;
         }
      }
      return result;
   }

   public void disablePlugins() {
      if (this.plugins.isEmpty()) return;
      for (Plugin plugin : this.plugins.values()) {
         try {
            if (plugin.isEnabled()) {
               plugin.onDisable();
               plugin.setEnabled(false);
            }
         } catch (Exception e) {
            LOGGER.error("Failed to disable plugin: " + plugin.getName());
            e.printStackTrace();
         }
      }
   }

   public Plugin getPlugin(String name) { return this.plugins.get(name); }
   public Map<String, Plugin> getPlugins() { return this.plugins; }
   public Map<String, PluginClassLoader> getLoaders() { return this.loaders; }
   public File getPluginDir() { return this.pluginDir; }
}

public class Meow {

   public static final String SERVER_NAME = "MeowMeow";
   public static final String VERSION = "0.1";
   public static String MC_VERSION = "Unknown";

   private static PluginLoader pluginLoader;
   private static final Logger LOGGER = LogManager.getLogger(Meow.class);

   public static void main(String[] args) {
      LOGGER.info("Starting net.minecraft.server.MinecraftServer");

      startVersionDetector();

      try {
         if (args == null || args.length == 0) {
            args = new String[]{"gui"};
         }
         startServer(args);
      } catch (Exception e) {
         LOGGER.error("Failed to start server: " + e.getMessage());
         e.printStackTrace();
      }
   }

   private static void startServer(String[] args) throws Exception {
      Class<?> serverClass = Class.forName("net.minecraft.server.MinecraftServer");
      Method mainMethod = serverClass.getMethod("main", String[].class);
      mainMethod.invoke(null, new Object[]{args});
   }

   private static void startVersionDetector() {
      Thread versionThread = new Thread(new Runnable() {
         public void run() {
            try {
               int retries = 0;
               Object server = null;

               while (retries < 15 && server == null) {
                  Thread.sleep(1000);
                  retries++;
                  try {
                     Class<?> serverClass = Class.forName("net.minecraft.server.MinecraftServer");
                     Method getServer = serverClass.getMethod("getServer");
                     server = getServer.invoke(null);
                  } catch (Exception e) {}
               }

               if (server != null) {
                  try {
                     Method getVersion = server.getClass().getMethod("getMinecraftVersion");
                     MC_VERSION = (String) getVersion.invoke(server);
                     LOGGER.info("This server is running " + SERVER_NAME + " version " + VERSION +
                             " (MC: " + MC_VERSION + ")");
                  } catch (Exception e) {
                     // ¾²Ä¬Ê§°Ü
                  }

                  pluginLoader = new PluginLoader(server);
                  pluginLoader.loadPlugins();

                  Runtime.getRuntime().addShutdownHook(new Thread() {
                     public void run() {
                        if (pluginLoader != null) {
                           pluginLoader.disablePlugins();
                        }
                     }
                  });
               }
            } catch (Exception e) {
               // ¾²Ä¬Ê§°Ü
            }
         }
      }, "MeowMeow thread");

      versionThread.setDaemon(true);
      versionThread.start();
   }

   public static PluginLoader getPluginLoader() {
      return pluginLoader;
   }
}