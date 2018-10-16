package chars;

import android.app.Activity;
import android.text.Spannable;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ListTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void unorderedListTest() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1"));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2"));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3"));

    final CDARichUnorderedList list = new CDARichUnorderedList();
    list.getContent().add(item1);
    list.getContent().add(item2);
    list.getContent().add(item3);

    final CharSequence result = processor.process(context, list);

    assertThat(result.toString()).isEqualTo(""
        + "• item1\n"
        + "• item2\n"
        + "• item3"
    );
  }

  @Test
  public void orderedListTest() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1"));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2"));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3"));

    final CDARichOrderedList list = new CDARichOrderedList();
    list.getContent().add(item1);
    list.getContent().add(item2);
    list.getContent().add(item3);

    final CharSequence result = processor.process(context, list);

    assertThat(result.toString()).isEqualTo(""
        + "1. item1\n"
        + "2. item2\n"
        + "3. item3"
    );
  }

  @Test
  public void nestedListsUinO() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1"));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2"));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3"));

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(item1);
    unorderedList.getContent().add(item2);
    unorderedList.getContent().add(item3);

    final CDARichListItem itemWithList = new CDARichListItem();
    itemWithList.getContent().add(new CDARichText("ordered:"));
    itemWithList.getContent().add(unorderedList);

    final CDARichOrderedList list = new CDARichOrderedList();
    list.getContent().add(item1);
    list.getContent().add(item2);
    list.getContent().add(itemWithList);
    list.getContent().add(item3);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(new CDARichText("unordered:"));
    document.getContent().add(list);

    final CharSequence result = processor.process(context, document);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(6);

    assertThat(result.toString()).isEqualTo(""
        + "unordered:1. item1\n"
        + "2. item2\n"
        + "3. ordered:\n"
        + "• item1\n"
        + "• item2\n"
        + "• item3\n"
        + "4. item3"
    );
  }

  @Test
  public void nestedListsOinU() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1"));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2"));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3"));

    final CDARichOrderedList orderedList = new CDARichOrderedList();
    orderedList.getContent().add(item1);
    orderedList.getContent().add(item2);
    orderedList.getContent().add(item3);

    final CDARichListItem ordered = new CDARichListItem();
    ordered.getContent().add(new CDARichText("ordered:"));
    ordered.getContent().add(orderedList);

    final CDARichUnorderedList unordered = new CDARichUnorderedList();
    unordered.getContent().add(item1);
    unordered.getContent().add(item2);
    unordered.getContent().add(ordered);
    unordered.getContent().add(item3);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(new CDARichText("unordered:"));
    document.getContent().add(unordered);

    final CharSequence result = processor.process(context, document);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(6);

    assertThat(result.toString()).isEqualTo(""
        + "unordered:• item1\n"
        + "• item2\n"
        + "• ordered:\n"
        + "1. item1\n"
        + "2. item2\n"
        + "3. item3\n"
        + "• item3"
    );
  }

  @Test
  public void nestedOrderedLists() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichOrderedList romanCaptialLetter = new CDARichOrderedList();
    addListItem(romanCaptialLetter, new CDARichText("NESTING!!1!"));

    final CDARichOrderedList firstLowerCaseLetterList = new CDARichOrderedList();
    addListItem(firstLowerCaseLetterList, romanCaptialLetter);
    addBunchOfItems(firstLowerCaseLetterList);

    final CDARichOrderedList secondLowerCaseLetterList = new CDARichOrderedList();
    addListItem(secondLowerCaseLetterList, firstLowerCaseLetterList);
    addBunchOfItems(secondLowerCaseLetterList);

    final CDARichOrderedList romanLetterList = new CDARichOrderedList();
    addListItem(romanLetterList, secondLowerCaseLetterList);
    addBunchOfItems(romanLetterList);

    final CDARichOrderedList capitalLetterList = new CDARichOrderedList();
    addListItem(capitalLetterList, romanLetterList);
    addBunchOfItems(capitalLetterList);

    final CDARichOrderedList numberedList = new CDARichOrderedList();
    addListItem(numberedList, capitalLetterList);
    addBunchOfItems(numberedList);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(numberedList);

    final CharSequence result = processor.process(context, document);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(133);

    assertThat(result.toString()).isEqualTo("" +
        " . NESTING!!1!\n" +
        "b. item 0\n" +
        "c. item 1\n" +
        "d. item 2\n" +
        "e. item 3\n" +
        "f. item 4\n" +
        "g. item 5\n" +
        "h. item 6\n" +
        "i. item 7\n" +
        "j. item 8\n" +
        "k. item 9\n" +
        "l. item 10\n" +
        "m. item 11\n" +
        "n. item 12\n" +
        "o. item 13\n" +
        "p. item 14\n" +
        "q. item 15\n" +
        "r. item 16\n" +
        "s. item 17\n" +
        "t. item 18\n" +
        "u. item 19\n" +
        "v. item 20\n" +
        "w. item 21\n" +
        "x. item 22\n" +
        "y. item 23\n" +
        "z. item 24\n" +
        "aa. item 25\n" +
        "ab. item 26\n" +
        "ac. item 27\n" +
        "ad. item 28\n" +
        "ae. item 29\n" +
        "b. item 0\n" +
        "c. item 1\n" +
        "d. item 2\n" +
        "e. item 3\n" +
        "f. item 4\n" +
        "g. item 5\n" +
        "h. item 6\n" +
        "i. item 7\n" +
        "j. item 8\n" +
        "k. item 9\n" +
        "l. item 10\n" +
        "m. item 11\n" +
        "n. item 12\n" +
        "o. item 13\n" +
        "p. item 14\n" +
        "q. item 15\n" +
        "r. item 16\n" +
        "s. item 17\n" +
        "t. item 18\n" +
        "u. item 19\n" +
        "v. item 20\n" +
        "w. item 21\n" +
        "x. item 22\n" +
        "y. item 23\n" +
        "z. item 24\n" +
        "aa. item 25\n" +
        "ab. item 26\n" +
        "ac. item 27\n" +
        "ad. item 28\n" +
        "ae. item 29\n" +
        "i. item 0\n" +
        "ii. item 1\n" +
        "iii. item 2\n" +
        "iv. item 3\n" +
        "v. item 4\n" +
        "vi. item 5\n" +
        "vii. item 6\n" +
        "viii. item 7\n" +
        "ix. item 8\n" +
        "x. item 9\n" +
        "xi. item 10\n" +
        "xii. item 11\n" +
        "xiii. item 12\n" +
        "xiv. item 13\n" +
        "xv. item 14\n" +
        "xvi. item 15\n" +
        "xvii. item 16\n" +
        "xviii. item 17\n" +
        "xix. item 18\n" +
        "xx. item 19\n" +
        "xxi. item 20\n" +
        "xxii. item 21\n" +
        "xxiii. item 22\n" +
        "xxiv. item 23\n" +
        "xxv. item 24\n" +
        "xxvi. item 25\n" +
        "xxvii. item 26\n" +
        "xxviii. item 27\n" +
        "xxix. item 28\n" +
        "xxx. item 29\n" +
        "B. item 0\n" +
        "C. item 1\n" +
        "D. item 2\n" +
        "E. item 3\n" +
        "F. item 4\n" +
        "G. item 5\n" +
        "H. item 6\n" +
        "I. item 7\n" +
        "J. item 8\n" +
        "K. item 9\n" +
        "L. item 10\n" +
        "M. item 11\n" +
        "N. item 12\n" +
        "O. item 13\n" +
        "P. item 14\n" +
        "Q. item 15\n" +
        "R. item 16\n" +
        "S. item 17\n" +
        "T. item 18\n" +
        "U. item 19\n" +
        "V. item 20\n" +
        "W. item 21\n" +
        "X. item 22\n" +
        "Y. item 23\n" +
        "Z. item 24\n" +
        "AA. item 25\n" +
        "AB. item 26\n" +
        "AC. item 27\n" +
        "AD. item 28\n" +
        "AE. item 29\n" +
        "2. item 0\n" +
        "3. item 1\n" +
        "4. item 2\n" +
        "5. item 3\n" +
        "6. item 4\n" +
        "7. item 5\n" +
        "8. item 6\n" +
        "9. item 7\n" +
        "10. item 8\n" +
        "11. item 9\n" +
        "12. item 10\n" +
        "13. item 11\n" +
        "14. item 12\n" +
        "15. item 13\n" +
        "16. item 14\n" +
        "17. item 15\n" +
        "18. item 16\n" +
        "19. item 17\n" +
        "20. item 18\n" +
        "21. item 19\n" +
        "22. item 20\n" +
        "23. item 21\n" +
        "24. item 22\n" +
        "25. item 23\n" +
        "26. item 24\n" +
        "27. item 25\n" +
        "28. item 26\n" +
        "29. item 27\n" +
        "30. item 28\n" +
        "31. item 29"
    );
  }

  @Test
  public void nestedUnorderedLists() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichUnorderedList firstLowerCaseLetterList = new CDARichUnorderedList();
    addBunchOfItems(firstLowerCaseLetterList);

    final CDARichUnorderedList secondLowerCaseLetterList = new CDARichUnorderedList();
    addListItem(secondLowerCaseLetterList, firstLowerCaseLetterList);
    addBunchOfItems(secondLowerCaseLetterList);

    final CDARichUnorderedList romanLetterList = new CDARichUnorderedList();
    addListItem(romanLetterList, secondLowerCaseLetterList);
    addBunchOfItems(romanLetterList);

    final CDARichUnorderedList capitalLetterList = new CDARichUnorderedList();
    addListItem(capitalLetterList, romanLetterList);
    addBunchOfItems(capitalLetterList);

    final CDARichUnorderedList numberedList = new CDARichUnorderedList();
    addListItem(numberedList, capitalLetterList);
    addBunchOfItems(numberedList);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(numberedList);

    final CharSequence result = processor.process(context, document);

    assertThat(result.toString()).isEqualTo("" +
        "• item 0\n" +
        "• item 1\n" +
        "• item 2\n" +
        "• item 3\n" +
        "• item 4\n" +
        "• item 5\n" +
        "• item 6\n" +
        "• item 7\n" +
        "• item 8\n" +
        "• item 9\n" +
        "• item 10\n" +
        "• item 11\n" +
        "• item 12\n" +
        "• item 13\n" +
        "• item 14\n" +
        "• item 15\n" +
        "• item 16\n" +
        "• item 17\n" +
        "• item 18\n" +
        "• item 19\n" +
        "• item 20\n" +
        "• item 21\n" +
        "• item 22\n" +
        "• item 23\n" +
        "• item 24\n" +
        "• item 25\n" +
        "• item 26\n" +
        "• item 27\n" +
        "• item 28\n" +
        "• item 29\n" +
        "• item 0\n" +
        "• item 1\n" +
        "• item 2\n" +
        "• item 3\n" +
        "• item 4\n" +
        "• item 5\n" +
        "• item 6\n" +
        "• item 7\n" +
        "• item 8\n" +
        "• item 9\n" +
        "• item 10\n" +
        "• item 11\n" +
        "• item 12\n" +
        "• item 13\n" +
        "• item 14\n" +
        "• item 15\n" +
        "• item 16\n" +
        "• item 17\n" +
        "• item 18\n" +
        "• item 19\n" +
        "• item 20\n" +
        "• item 21\n" +
        "• item 22\n" +
        "• item 23\n" +
        "• item 24\n" +
        "• item 25\n" +
        "• item 26\n" +
        "• item 27\n" +
        "• item 28\n" +
        "• item 29\n" +
        "• item 0\n" +
        "• item 1\n" +
        "• item 2\n" +
        "• item 3\n" +
        "• item 4\n" +
        "• item 5\n" +
        "• item 6\n" +
        "• item 7\n" +
        "• item 8\n" +
        "• item 9\n" +
        "• item 10\n" +
        "• item 11\n" +
        "• item 12\n" +
        "• item 13\n" +
        "• item 14\n" +
        "• item 15\n" +
        "• item 16\n" +
        "• item 17\n" +
        "• item 18\n" +
        "• item 19\n" +
        "• item 20\n" +
        "• item 21\n" +
        "• item 22\n" +
        "• item 23\n" +
        "• item 24\n" +
        "• item 25\n" +
        "• item 26\n" +
        "• item 27\n" +
        "• item 28\n" +
        "• item 29\n" +
        "• item 0\n" +
        "• item 1\n" +
        "• item 2\n" +
        "• item 3\n" +
        "• item 4\n" +
        "• item 5\n" +
        "• item 6\n" +
        "• item 7\n" +
        "• item 8\n" +
        "• item 9\n" +
        "• item 10\n" +
        "• item 11\n" +
        "• item 12\n" +
        "• item 13\n" +
        "• item 14\n" +
        "• item 15\n" +
        "• item 16\n" +
        "• item 17\n" +
        "• item 18\n" +
        "• item 19\n" +
        "• item 20\n" +
        "• item 21\n" +
        "• item 22\n" +
        "• item 23\n" +
        "• item 24\n" +
        "• item 25\n" +
        "• item 26\n" +
        "• item 27\n" +
        "• item 28\n" +
        "• item 29\n" +
        "• item 0\n" +
        "• item 1\n" +
        "• item 2\n" +
        "• item 3\n" +
        "• item 4\n" +
        "• item 5\n" +
        "• item 6\n" +
        "• item 7\n" +
        "• item 8\n" +
        "• item 9\n" +
        "• item 10\n" +
        "• item 11\n" +
        "• item 12\n" +
        "• item 13\n" +
        "• item 14\n" +
        "• item 15\n" +
        "• item 16\n" +
        "• item 17\n" +
        "• item 18\n" +
        "• item 19\n" +
        "• item 20\n" +
        "• item 21\n" +
        "• item 22\n" +
        "• item 23\n" +
        "• item 24\n" +
        "• item 25\n" +
        "• item 26\n" +
        "• item 27\n" +
        "• item 28\n" +
        "• item 29"
    );
  }

  private void addListItem(CDARichBlock list, CDARichNode child) {
    final CDARichListItem item = new CDARichListItem();
    item.getContent().add(child);
    list.getContent().add(item);
  }

  private void addBunchOfItems(CDARichBlock block) {
    for (int i = 0; i < 30; ++i) {
      final CDARichListItem item = new CDARichListItem();
      item.getContent().add(new CDARichText("item " + i));
      block.getContent().add(item);
    }
  }
}
