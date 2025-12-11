package financeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FinanceTrackerForm {

    private JPanel glavniPanel;
    private JTextField poljeIznos;
    private JTextField poljeOpis;
    private JComboBox<String> izborVrste;
    private JButton dugmeDodaj;
    private JTable tabelaTransakcija;
    private JLabel labelaPrihodi;
    private JLabel labelaRashodi;
    private JLabel labelaSaldo;
    private JLabel naslov;
    private JLabel opisPolja;
    private JLabel opisPolja2;
    private JButton dugmeAzuriraj;
    private JButton dugmeBrisanje;
    private JComboBox izborKategorija;
    private JButton dugmeExport;


    private String odabraniId = null;

    private TransactionManager menadzer;

    public FinanceTrackerForm() {
        menadzer = new TransactionManager();

        ucitajTabelu();
        azurirajPregled();

        tabelaTransakcija.getSelectionModel().addListSelectionListener(e -> {
            int red = tabelaTransakcija.getSelectedRow();
            if (red >= 0) {
                odabraniId = (String) tabelaTransakcija.getValueAt(red, 4);
                izborVrste.setSelectedItem(tabelaTransakcija.getValueAt(red, 0));
                poljeIznos.setText(String.valueOf(tabelaTransakcija.getValueAt(red, 1)));
                poljeOpis.setText((String) tabelaTransakcija.getValueAt(red, 2));
                izborKategorija.setSelectedItem(tabelaTransakcija.getValueAt(red, 3));
            }
        });

        dugmeDodaj.addActionListener(e -> {
            try {
                String vrsta = (String) izborVrste.getSelectedItem();
                double iznos = Double.parseDouble(poljeIznos.getText());
                String opis = poljeOpis.getText();
                String kategorija = (String) izborKategorija.getSelectedItem();

                if (opis.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne može biti prazan!");
                    return;
                }

                Transaction t = new Transaction(vrsta, iznos, opis, kategorija);
                menadzer.dodajTransakciju(t);

                ucitajTabelu();
                azurirajPregled();

                poljeIznos.setText("");
                poljeOpis.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Iznos mora biti broj!");
            }
        });

        dugmeAzuriraj.addActionListener(e -> {
            if (odabraniId == null) {
                JOptionPane.showMessageDialog(null, "Niste odabrali transakciju!");
                return;
            }

            try {
                String vrsta = (String) izborVrste.getSelectedItem();
                double iznos = Double.parseDouble(poljeIznos.getText());
                String opis = poljeOpis.getText();
                String kategorija = (String) izborKategorija.getSelectedItem();

                Transaction t = new Transaction(vrsta, iznos, opis, kategorija,odabraniId);

                menadzer.azurirajTransakciju(t);

                ucitajTabelu();
                azurirajPregled();

                JOptionPane.showMessageDialog(null, "Ažurirano!");

                odabraniId = null;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška pri ažuriranju!");
            }
        });
        dugmeBrisanje.addActionListener(e -> {

            if (odabraniId == null) {
                JOptionPane.showMessageDialog(null, "Niste odabrali transakciju!");
                return;
            }

            Object[] opcije = {"Da", "Ne"};

            int odgovor = JOptionPane.showOptionDialog(
                    null,
                    "Jeste li sigurni da želite izbrisati ovu transakciju?",
                    "Potvrda brisanja",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcije,
                    opcije[0]
            );


            if (odgovor == 0) {

                menadzer.obrisiTransakciju(odabraniId);

                JOptionPane.showMessageDialog(null, "Transakcija obrisana!");

                ucitajTabelu();
                azurirajPregled();

                odabraniId = null;
            }
        });
        dugmeExport.addActionListener(e -> {

            try {

                double prihodi = menadzer.ukupniPrihodi();
                double rashodi = menadzer.ukupniRashodi();
                double stanje = prihodi - rashodi;

                // PO KATEGORIJAMA
                double hrana = menadzer.exportujTransakciju("Hrana");
                double prevoz = menadzer.exportujTransakciju("Prevoz");
                double zabava = menadzer.exportujTransakciju("Zabava");
                double racuni = menadzer.exportujTransakciju("Racuni");
                double ostalo = menadzer.exportujTransakciju("Ostalo");

                String tekst =
                        "Ukupni prihod: " + prihodi + "\n" +
                                "Ukupni rashod: " + rashodi + "\n" +
                                "Stanje: " + stanje + "\n\n" +
                                "Rashodi po kategorijama:\n" +
                                "Hrana: " + hrana + "\n" +
                                "Prevoz: " + prevoz + "\n" +
                                "Zabava: " + zabava + "\n" +
                                "Racuni: " + racuni + "\n" +
                                "Ostalo: " + ostalo + "\n";

                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new java.io.File("izvjestaj.txt"));

                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                    java.io.File file = chooser.getSelectedFile();
                    java.nio.file.Files.write(file.toPath(), tekst.getBytes());

                    JOptionPane.showMessageDialog(null, "Uspješno eksportovano!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška pri exportu!");
            }

        });

    }

    private void ucitajTabelu() {
        ArrayList<Transaction> lista = menadzer.dohvatiSveTransakcije();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vrsta");
        model.addColumn("Iznos");
        model.addColumn("Opis");
        model.addColumn("Kategorija");
        model.addColumn("ID");


        for (Transaction t : lista) {
            model.addRow(new Object[]{
                    t.getVrsta(),
                    t.getIznos(),
                    t.getOpis(),
                    t.getKategorija(),
                    t.getId()
            });
        }

        tabelaTransakcija.setModel(model);
        tabelaTransakcija.getColumnModel().getColumn(4).setMinWidth(0);
        tabelaTransakcija.getColumnModel().getColumn(4).setMaxWidth(0);
        tabelaTransakcija.getColumnModel().getColumn(4).setWidth(0);
    }

    private void azurirajPregled() {
        double prihodi = menadzer.ukupniPrihodi();
        double rashodi = menadzer.ukupniRashodi();
        double saldo = prihodi - rashodi;

        labelaPrihodi.setText("Prihod: " + prihodi);
        labelaRashodi.setText("Rashod: " + rashodi);
        labelaSaldo.setText("Saldo: " + saldo);
    }

    public JPanel getGlavniPanel() {
        return glavniPanel;
    }
}



