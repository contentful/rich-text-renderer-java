package com.contentful.rich.android.renderer.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkBold;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkCode;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkCustom;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkItalic;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkUnderline;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichTable;
import com.contentful.java.cda.rich.CDARichTableCell;
import com.contentful.java.cda.rich.CDARichTableHeaderCell;
import com.contentful.java.cda.rich.CDARichTableRow;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TableRenderer extends BlockRenderer {
    public TableRenderer(@Nonnull AndroidProcessor<View> processor) {
        super(processor);
    }

    @Override
    public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
        return node instanceof CDARichTable;
    }

    @Override
    protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
        return context.getInflater().inflate(R.layout.rich_table_layout, null, false);
    }

    @Nullable
    @Override
    public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
        final CDARichTable table = (CDARichTable) node;
        final View result = inflateRichLayout(context, node);
        final TableLayout tableLayout = result.findViewById(R.id.rich_table);

        for (final CDARichNode rowNode : table.getContent()) {
            if (rowNode instanceof CDARichTableRow) {
                final CDARichTableRow row = (CDARichTableRow) rowNode;
                final TableRow tableRow = new TableRow(context.getAndroidContext());
                
                for (final CDARichNode cellNode : row.getContent()) {
                    if (cellNode instanceof CDARichTableCell || cellNode instanceof CDARichTableHeaderCell) {
                        final TextView cellView = new TextView(context.getAndroidContext());
                        SpannableStringBuilder cellContent = new SpannableStringBuilder();
                        
                        // Enable clickable links
                        cellView.setMovementMethod(LinkMovementMethod.getInstance());
                        
                        // Process cell content
                        for (final CDARichNode contentNode : ((CDARichBlock) cellNode).getContent()) {
                            if (contentNode instanceof CDARichParagraph) {
                                for (final CDARichNode paragraphContent : ((CDARichParagraph) contentNode).getContent()) {
                                    if (paragraphContent instanceof CDARichText) {
                                        final CDARichText richText = (CDARichText) paragraphContent;
                                        SpannableString textContent = new SpannableString(richText.getText());
                                        
                                        // Apply marks
                                        for (final CDARichMark mark : richText.getMarks()) {
                                            if (mark instanceof CDARichMarkBold) {
                                                textContent.setSpan(new StyleSpan(Typeface.BOLD), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            if (mark instanceof CDARichMarkItalic) {
                                                textContent.setSpan(new StyleSpan(Typeface.ITALIC), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            if (mark instanceof CDARichMarkUnderline) {
                                                textContent.setSpan(new UnderlineSpan(), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            if (mark instanceof CDARichMarkCode) {
                                                textContent.setSpan(new BackgroundColorSpan(0xFFF5F5F5), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                textContent.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            if (mark instanceof CDARichMarkCustom) {
                                                textContent.setSpan(new BackgroundColorSpan(0x80FFFF00), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                        }
                                        
                                        cellContent.append(textContent);
                                    } else if (paragraphContent instanceof CDARichHyperLink) {
                                        final CDARichHyperLink hyperlink = (CDARichHyperLink) paragraphContent;
                                        final String uri = (String) hyperlink.getData();
                                        
                                        // Process hyperlink content
                                        for (final CDARichNode hyperlinkContent : hyperlink.getContent()) {
                                            if (hyperlinkContent instanceof CDARichText) {
                                                final CDARichText richText = (CDARichText) hyperlinkContent;
                                                SpannableString linkText = new SpannableString(richText.getText());
                                                
                                                // Apply link styling
                                                ClickableSpan clickableSpan = new ClickableSpan() {
                                                    @Override
                                                    public void onClick(@NonNull View widget) {
                                                        String url = uri.startsWith("http") ? uri : "http://" + uri;
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                        context.getAndroidContext().startActivity(intent);
                                                    }
                                                };
                                                linkText.setSpan(clickableSpan, 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                linkText.setSpan(new ForegroundColorSpan(Color.parseColor("#0645AD")), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                linkText.setSpan(new UnderlineSpan(), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                
                                                // Apply any additional marks from the hyperlink text
                                                for (final CDARichMark mark : richText.getMarks()) {
                                                    if (mark instanceof CDARichMarkBold) {
                                                        linkText.setSpan(new StyleSpan(Typeface.BOLD), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    }
                                                    if (mark instanceof CDARichMarkItalic) {
                                                        linkText.setSpan(new StyleSpan(Typeface.ITALIC), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    }
                                                }
                                                
                                                cellContent.append(linkText);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Set cell properties
                        cellView.setPadding(12, 8, 12, 8);
                        
                        if (cellNode instanceof CDARichTableHeaderCell) {
                            cellView.setBackgroundResource(R.drawable.table_header_cell_border);
                        } else {
                            cellView.setBackgroundResource(R.drawable.table_cell_border);
                        }
                        
                        cellView.setText(cellContent);
                        tableRow.addView(cellView);
                    }
                }
                
                tableLayout.addView(tableRow);
            }
        }

        return result;
    }
} 