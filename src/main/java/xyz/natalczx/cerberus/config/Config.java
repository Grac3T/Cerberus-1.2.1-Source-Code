// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import java.io.IOException;
import java.io.Reader;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.File;
import java.lang.reflect.Field;
import xyz.natalczx.cerberus.CerberusLogger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;

public class Config
{
    public Config() {
        this.save(new ArrayList<String>(), this.getClass(), this, 0);
    }
    
    private void set(final String key, Object value) {
        final String[] split = key.split("\\.");
        final Object instance = this.getInstance(split, this.getClass());
        if (instance != null) {
            final Field field = this.getField(split, instance);
            if (field != null) {
                try {
                    if (field.getAnnotation(Final.class) != null) {
                        return;
                    }
                    if (field.getType() == String.class && !(value instanceof String)) {
                        value += "";
                    }
                    field.set(instance, value);
                    return;
                }
                catch (IllegalAccessException | IllegalArgumentException ex2) {
                    final Exception ex;
                    final Exception e = ex;
                    CerberusLogger.log(Level.WARNING, "Error:", e);
                }
            }
        }
        CerberusLogger.log(Level.WARNING, "Failed to set config option: {0}: {1} | {2} ", key, value, instance);
    }
    
    public boolean load(final File file) {
        if (!file.exists()) {
            return false;
        }
        YamlConfiguration yml;
        try (final InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            yml = YamlConfiguration.loadConfiguration((Reader)reader);
        }
        catch (IOException ex) {
            CerberusLogger.exception("Unable to load config.", ex);
            return false;
        }
        this.set((ConfigurationSection)yml, "");
        return true;
    }
    
    public void set(final ConfigurationSection yml, final String oldPath) {
        for (final String key : yml.getKeys(false)) {
            final Object value = yml.get(key);
            final String newPath = oldPath + (oldPath.isEmpty() ? "" : ".") + key;
            if (value instanceof ConfigurationSection) {
                this.set((ConfigurationSection)value, newPath);
            }
            else {
                this.set(newPath, value);
            }
        }
    }
    
    public void save(final File file) {
        try {
            final File parent = file.getParentFile();
            if (parent != null) {
                file.getParentFile().mkdirs();
            }
            final Path configFile = file.toPath();
            final Path tempCfg = new File(file.getParentFile(), "__tmpcfg").toPath();
            final List<String> lines = new ArrayList<String>();
            this.save(lines, this.getClass(), this, 0);
            Files.write(tempCfg, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            try {
                Files.move(tempCfg, configFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException e2) {
                Files.move(tempCfg, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            CerberusLogger.exception("Error: ", e);
        }
    }
    
    private String toYamlString(final Object value, final String spacing) {
        if (value instanceof List) {
            final Collection<?> listValue = (Collection<?>)value;
            if (listValue.isEmpty()) {
                return "[]";
            }
            final StringBuilder m = new StringBuilder();
            for (final Object obj : listValue) {
                m.append(System.lineSeparator()).append(spacing).append("- ").append(this.toYamlString(obj, spacing));
            }
            return m.toString();
        }
        else {
            if (!(value instanceof String)) {
                return (value != null) ? value.toString() : "null";
            }
            final String stringValue = (String)value;
            if (stringValue.isEmpty()) {
                return "''";
            }
            return "\"" + stringValue + "\"";
        }
    }
    
    private void save(final List<String> lines, final Class clazz, final Object instance, final int indent) {
        try {
            final String spacing = this.repeat(" ", indent);
            for (final Field field : clazz.getFields()) {
                if (field.getAnnotation(Ignore.class) == null) {
                    final Class<?> current = field.getType();
                    if (field.getAnnotation(Ignore.class) == null) {
                        Comment comment = field.getAnnotation(Comment.class);
                        if (comment != null) {
                            for (final String commentLine : comment.value()) {
                                lines.add(spacing + "# " + commentLine);
                            }
                        }
                        final Create create = field.getAnnotation(Create.class);
                        if (create != null) {
                            Object value = field.get(instance);
                            this.setAccessible(field);
                            if (indent == 0) {
                                lines.add("");
                            }
                            comment = current.getAnnotation(Comment.class);
                            if (comment != null) {
                                for (final String commentLine2 : comment.value()) {
                                    lines.add(spacing + "# " + commentLine2);
                                }
                            }
                            lines.add(spacing + this.toNodeName(current.getSimpleName()) + ":");
                            if (value == null) {
                                field.set(instance, value = current.newInstance());
                            }
                            this.save(lines, current, value, indent + 2);
                        }
                        else {
                            lines.add(spacing + this.toNodeName(field.getName() + ": ") + this.toYamlString(field.get(instance), spacing));
                        }
                    }
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | SecurityException ex2) {
            final Exception ex;
            final Exception e = ex;
            CerberusLogger.exception("Error:", e);
        }
    }
    
    private Field getField(final String[] split, final Object instance) {
        try {
            final Field field = instance.getClass().getField(this.toFieldName(split[split.length - 1]));
            this.setAccessible(field);
            return field;
        }
        catch (IllegalAccessException | NoSuchFieldException | SecurityException ex2) {
            final Exception ex;
            final Exception e = ex;
            CerberusLogger.log(Level.WARNING, "Invalid config field: {0} for {1}", String.join(".", (CharSequence[])split), this.toNodeName(instance.getClass().getSimpleName()));
            return null;
        }
    }
    
    private Object getInstance(String[] split, final Class root) {
        try {
            Class<?> clazz = (root == null) ? MethodHandles.lookup().lookupClass() : root;
            Object instance = this;
        Label_0044:
            while (true) {
                Label_0187: {
                    if (split.length <= 0) {
                        break Label_0187;
                    }
                    switch (split.length) {
                        case 1: {
                            break Label_0044;
                        }
                    }
                    try {
                        Class found = null;
                        final Class<?>[] declaredClasses;
                        final Class<?>[] classes = declaredClasses = clazz.getDeclaredClasses();
                        for (final Class current : declaredClasses) {
                            if (current.getSimpleName().equalsIgnoreCase(this.toFieldName(split[0]))) {
                                found = current;
                                break;
                            }
                        }
                        try {
                            final Field instanceField = clazz.getDeclaredField(this.toFieldName(split[0]));
                            this.setAccessible(instanceField);
                            Object value = instanceField.get(instance);
                            if (value == null) {
                                value = found.newInstance();
                                instanceField.set(instance, value);
                            }
                            clazz = (Class<?>)found;
                            instance = value;
                            split = Arrays.copyOfRange(split, 1, split.length);
                            continue;
                        }
                        catch (NoSuchFieldException ex2) {
                            return null;
                        }
                    }
                    catch (IllegalArgumentException | InstantiationException ex3) {
                        final Exception ex;
                        final Exception e = ex;
                        e.printStackTrace();
                    }
                }
            }
            return instance;
        }
        catch (IllegalAccessException ex4) {}
        catch (IllegalArgumentException ex5) {}
        catch (InstantiationException ex6) {}
        catch (SecurityException ex7) {}
        return null;
    }
    
    private String toFieldName(final String node) {
        return node.toUpperCase().replaceAll("-", "_");
    }
    
    private String toNodeName(final String field) {
        return field.toLowerCase().replace("_", "-");
    }
    
    private void setAccessible(final Field field) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
    }
    
    private String repeat(final String s, final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface Ignore {
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface Comment {
        String[] value();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Final {
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Create {
    }
}
