package lib;

import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.LocalizedResource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ContentfulCreator {
  public static CDAResource mockCDAEntry() {
    final CDAEntry entry = new CDAEntry();
    Field attrs = null;
    for (final Field field : CDAResource.class.getDeclaredFields()) {
      if ("attrs".equals(field.getName())) {
        attrs = field;
      }
    }

    if (attrs != null) {
      attrs.setAccessible(true);
      try {
        Map<String, Object> map = newHashMap();
        map.put("title", newHashMap());
        attrs.set(entry, map);
      } catch (IllegalAccessException e) {
      }
      attrs.setAccessible(false);
    }

    Field fields = null;
    Field defaultLocale = null;
    for (final Field field : LocalizedResource.class.getDeclaredFields()) {
      if ("fields".equals(field.getName())) {
        fields = field;
      } else if ("defaultLocale".equals(field.getName())) {
        defaultLocale = field;
      }
    }

    if (fields != null) {
      fields.setAccessible(true);

      try {
        final HashMap<Object, Object> title = newHashMap();
        title.put("en-US", "Title");

        final HashMap<Object, Object> localized = newHashMap();
        localized.put("title", title);

        final HashMap<Object, Object> file = newHashMap();
        file.put("en-US", newHashMap());
        localized.put("file", file);

        fields.set(entry, localized);
      } catch (IllegalAccessException e) {
      }

      fields.setAccessible(false);
    }

    if (defaultLocale != null) {
      defaultLocale.setAccessible(true);

      try {
        defaultLocale.set(entry, "en-US");
      } catch (IllegalAccessException e) {
      }

      defaultLocale.setAccessible(false);
    }

    entry.attrs().put("id", "fake_id");
    entry.attrs().put("contentType", "fake_type");

    entry.setField("en-US", "title", "Title");
    return entry;
  }
}
