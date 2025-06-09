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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
        View view = context.getInflater().inflate(R.layout.rich_table_layout, null, false);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 32; // Add 32dp margin at the top
        params.bottomMargin = 32; // Add 32dp margin at the bottom
        view.setLayoutParams(params);
        return view;
    }

    @Nullable
    @Override
    public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
        final CDARichTable table = (CDARichTable) node;
        final View result = inflateRichLayout(context, node);
        final TableLayout tableLayout = result.findViewById(R.id.rich_table);

        // Debug logging for table content
        System.out.println("Table content size: " + table.getContent().size());
        for (CDARichNode rowNode : table.getContent()) {
            if (rowNode instanceof CDARichTableRow) {
                CDARichTableRow row = (CDARichTableRow) rowNode;
                System.out.println("Row content size: " + row.getContent().size());
                for (CDARichNode cellNode : row.getContent()) {
                    System.out.println("Cell type: " + cellNode.getClass().getSimpleName());
                    if (cellNode instanceof CDARichBlock) {
                        CDARichBlock block = (CDARichBlock) cellNode;
                        System.out.println("Block content size: " + block.getContent().size());
                    }
                }
            }
        }

        // Find the maximum number of columns in any row
        int maxColumns = 0;
        for (final CDARichNode rowNode : table.getContent()) {
            if (rowNode instanceof CDARichTableRow) {
                maxColumns = Math.max(maxColumns, ((CDARichTableRow) rowNode).getContent().size());
            }
        }
        System.out.println("Max columns: " + maxColumns);

        // Set the weightSum to match the number of columns
        tableLayout.setWeightSum(maxColumns);

        // Get the screen width to calculate available space for each column
        int screenWidth = context.getAndroidContext().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = (screenWidth - (maxColumns + 1) * 16) / maxColumns; // Account for padding and margins

        for (final CDARichNode rowNode : table.getContent()) {
            if (rowNode instanceof CDARichTableRow) {
                final CDARichTableRow row = (CDARichTableRow) rowNode;
                final TableRow tableRow = new TableRow(context.getAndroidContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                ));

                // Get the actual cells in this row
                List<CDARichNode> cells = new ArrayList<>(row.getContent());
                
                // Debug logging
                System.out.println("Processing row with " + cells.size() + " cells");
                for (int i = 0; i < cells.size(); i++) {
                    CDARichNode cell = cells.get(i);
                    System.out.println("Cell " + i + " type: " + (cell != null ? cell.getClass().getSimpleName() : "null"));
                    if (cell instanceof CDARichBlock) {
                        CDARichBlock block = (CDARichBlock) cell;
                        System.out.println("Cell " + i + " block content size: " + block.getContent().size());
                    }
                }
                
                // First pass: create all cells and measure their heights
                List<View> cellViews = new ArrayList<>(maxColumns);
                for (int i = 0; i < maxColumns; i++) {
                    cellViews.add(null);
                }
                int maxHeight = 0;
                
                // Create cells and place them in their original positions
                for (int i = 0; i < cells.size(); i++) {
                    final CDARichNode cellNode = cells.get(i);
                    final View cellContainer = context.getInflater().inflate(R.layout.rich_table_cell_layout, null, false);
                    final TextView cellView = cellContainer.findViewById(R.id.rich_table_cell);
                    SpannableStringBuilder cellContent = new SpannableStringBuilder();
                    
                    // Enable clickable links
                    cellView.setMovementMethod(LinkMovementMethod.getInstance());
                    
                    if (cellNode != null) {
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
                                        final Object data = hyperlink.getData();
                                        final String uri;
                                        
                                        if (data instanceof String) {
                                            uri = (String) data;
                                        } else if (data instanceof Map) {
                                            uri = (String) ((Map<?, ?>) data).get("uri");
                                        } else {
                                            continue;
                                        }
                                        
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
                    }
                    
                    // Set cell properties
                    if (cellNode instanceof CDARichTableHeaderCell) {
                        cellView.setBackgroundResource(R.drawable.table_header_cell_border);
                        cellView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                    } else {
                        cellView.setBackgroundResource(R.drawable.table_cell_border);
                        cellView.setGravity(Gravity.START);
                    }
                    
                    // Always set the text, even if empty
                    cellView.setText(cellContent);
                    cellViews.set(i, cellContainer);

                    // Measure the cell's height with the correct width
                    cellContainer.measure(
                        View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    );
                    maxHeight = Math.max(maxHeight, cellContainer.getMeasuredHeight());
                }

                // Second pass: add all cells to the row with the same height
                for (int i = 0; i < maxColumns; i++) {
                    View cellContainer = cellViews.get(i);
                    if (cellContainer == null) {
                        // Create an empty cell if none exists at this position
                        cellContainer = context.getInflater().inflate(R.layout.rich_table_cell_layout, null, false);
                        final TextView cellView = cellContainer.findViewById(R.id.rich_table_cell);
                        cellView.setBackgroundResource(R.drawable.table_cell_border);
                        cellView.setGravity(Gravity.START);
                        cellView.setText("");
                        cellViews.set(i, cellContainer);
                    }
                    
                    // Create layout params with fixed height
                    TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                        0, // width = 0 to use weight
                        maxHeight, // use the maximum height
                        1.0f // equal weight for all columns
                    );
                    
                    // Set minimum height to ensure consistent cell heights
                    cellContainer.setMinimumHeight(maxHeight);
                    tableRow.addView(cellContainer, cellParams);
                }
                
                tableLayout.addView(tableRow);
            }
        }

        return result;
    }
} 