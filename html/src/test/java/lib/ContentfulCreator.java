package lib;

import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;

import java.lang.reflect.Field;

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
        attrs.set(entry, newHashMap());
      } catch (IllegalAccessException e) {
      }
      attrs.setAccessible(false);
    }

    entry.attrs().put("id", "fake_id");
    entry.attrs().put("contentType", "fake_type");
    return entry;
  }
}
