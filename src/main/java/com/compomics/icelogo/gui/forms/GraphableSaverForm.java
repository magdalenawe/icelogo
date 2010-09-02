package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.gui.interfaces.Graphable;
import com.compomics.util.sun.SwingWorker;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: niklaas Date: 2-mrt-2009 Time: 14:46:01 To change this template use File | Settings |
 * File Templates.
 */
public class GraphableSaverForm {
    private JPanel jpanContent;
    private JButton saveButton;
    private JPanel savablePanel;

    private Vector<SavableLine> lSavableLines = new Vector<SavableLine>();
    private MainInformationFeeder iInfoFeeder = MainInformationFeeder.getInstance();


    public GraphableSaverForm(Vector<Graphable> aGraphables) {


        savablePanel.setLayout(new BoxLayout(savablePanel, BoxLayout.Y_AXIS));
        savablePanel.add(Box.createVerticalStrut(5));
        for (int i = 0; i < aGraphables.size(); i++) {
            SavableLine lLine = new SavableLine(aGraphables.get(i));
            lSavableLines.add(lLine);
            savablePanel.add(lLine.getContentPane());
            savablePanel.add(Box.createVerticalStrut(5));
        }


        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    public JPanel getContentPane() {
        return this.jpanContent;
    }

    public void save() {

        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(new JFrame());
        String fileLocation = fc.getSelectedFile().getAbsolutePath();
        if (!fileLocation.endsWith(".pdf")) {
            //add .pdf
            fileLocation = fileLocation + ".pdf";
        }
        final String lfileLocation = fileLocation;
        //create a new swing worker
        SwingWorker lPdfSaver = new SwingWorker() {
            public Boolean construct() {
                try {
                    // step 1 create a document
                    Document document = new Document(new Rectangle(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight()));
                    // step 2 create a writer
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(lfileLocation));
                    // step 3 open the document
                    document.open();
                    // step 4 create the PDFContenteByte
                    PdfContentByte cb = writer.getDirectContent();
                    Graphics2D g2;

                    for (int i = 0; i < lSavableLines.size(); i++) {
                        if (lSavableLines.get(i).isSelected()) {
                            if (lSavableLines.get(i).getSavable().isSvg()) {

                                //we will add an svg to the pd
                                UserAgent userAgent = new UserAgentAdapter();
                                DocumentLoader loader = new DocumentLoader(userAgent);
                                BridgeContext ctx = new BridgeContext(userAgent, loader);
                                GVTBuilder builder = new GVTBuilder();
                                ctx.setDynamicState(BridgeContext.DYNAMIC);

                                PdfTemplate map = cb.createTemplate(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                g2 = map.createGraphics(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight(), new DefaultFontMapper());
                                GraphicsNode graphicsToPaint = builder.build(ctx, lSavableLines.get(i).getSavable().getSVG());
                                graphicsToPaint.paint(g2);
                                g2.dispose();
                                cb.addTemplate(map, 0, 0);
                                document.newPage();
                            } /*else if (lSavableLines.get(i).getSavable().isChart()) {

                                PdfTemplate tp = cb.createTemplate(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                g2 = tp.createGraphics(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight(), new DefaultFontMapper());
                                Rectangle2D r2d = new Rectangle2D.Double(0, 0, iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                BarChartForm lChart = (BarChartForm) lSavableLines.get(i).getSavable();
                                lChart.getChart().draw(g2, r2d);
                                cb.addTemplate(tp, 0, 0);
                                g2.dispose();
                                document.newPage();

                            } */
                            else {
                                //we will add a jpanel to the pdf
                                g2 = cb.createGraphicsShapes(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                lSavableLines.get(i).getSavable().getContentPanel().paintAll(g2);
                                g2.dispose();
                                document.newPage();
                            }
                        }
                    }

                    // step 7 close the document
                    document.close();

                } catch (DocumentException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                // step 7.2 dispose the iText components.
                return true;
            }

            public void finished() {
                JOptionPane.showMessageDialog(new JFrame(), "Saving done", "Info", JOptionPane.INFORMATION_MESSAGE);

            }

        };
        lPdfSaver.start();

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder("Save"));
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        savablePanel = new JPanel();
        savablePanel.setLayout(new GridBagLayout());
        scrollPane1.setViewportView(savablePanel);
        saveButton = new JButton();
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(saveButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }


    /**
     * Created by IntelliJ IDEA. User: niklaas Date: 3-mrt-2009 Time: 12:20:22 To change this template use File |
     * Settings | File Templates.
     */
    private class SavableLine {
        private JPanel jpanContent;
        private JCheckBox chbDescription;
        private Graphable iGraphable;


        public SavableLine(Graphable aGraphable) {
            build();
            this.iGraphable = aGraphable;
            chbDescription.setText(aGraphable.getDescription());
        }

        public JPanel getContentPane() {
            return jpanContent;
        }

        public Graphable getSavable() {
            return this.iGraphable;
        }

        public boolean isSelected() {
            return chbDescription.isSelected();
        }

        /**
         * Construct the GUI
         */
        private void build() {
            jpanContent = new JPanel();
            jpanContent.setLayout(new GridBagLayout());
            chbDescription = new JCheckBox();
            chbDescription.setText("Description");
            chbDescription.setSelected(true);
            GridBagConstraints gbc;
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            jpanContent.add(chbDescription, gbc);
            gbc = new GridBagConstraints();
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            final JPanel spacer1 = new JPanel();
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            jpanContent.add(spacer1, gbc);
        }

        public JComponent getRootComponent() {
            return jpanContent;
        }
    }

}
