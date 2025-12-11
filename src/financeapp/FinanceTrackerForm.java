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

    private String odabraniId = null;

    private TransactionManager menadzer;

    public FinanceTrackerForm() {
        menadzer = new TransactionManager();

        ucitajTabelu();
        azurirajPregled();

        tabelaTransakcija.getSelectionModel().addListSelectionListener(e -> {
            int red = tabelaTransakcija.getSelectedRow();
            if (red >= 0) {
                odabraniId = (String) tabelaTransakcija.getValueAt(red, 3);
                izborVrste.setSelectedItem(tabelaTransakcija.getValueAt(red, 0));
                poljeIznos.setText(String.valueOf(tabelaTransakcija.getValueAt(red, 1)));
                poljeOpis.setText((String) tabelaTransakcija.getValueAt(red, 2));
            }
        });

        dugmeDodaj.addActionListener(e -> {
            try {
                String vrsta = (String) izborVrste.getSelectedItem();
                double iznos = Double.parseDouble(poljeIznos.getText());
                String opis = poljeOpis.getText();

                if (opis.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne može biti prazan!");
                    return;
                }

                Transaction t = new Transaction(vrsta, iznos, opis);
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

                Transaction t = new Transaction(vrsta, iznos, opis, odabraniId);

                menadzer.azurirajTransakciju(t);

                ucitajTabelu();
                azurirajPregled();

                JOptionPane.showMessageDialog(null, "Ažurirano!");

                odabraniId = null;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Greška pri ažuriranju!");
            }
        });
    }

    private void ucitajTabelu() {
        ArrayList<Transaction> lista = menadzer.dohvatiSveTransakcije();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vrsta");
        model.addColumn("Iznos");
        model.addColumn("Opis");
        model.addColumn("ID");

        for (Transaction t : lista) {
            model.addRow(new Object[]{
                    t.getVrsta(),
                    t.getIznos(),
                    t.getOpis(),
                    t.getId()
            });
        }

        tabelaTransakcija.setModel(model);
        tabelaTransakcija.getColumnModel().getColumn(3).setMinWidth(0);
        tabelaTransakcija.getColumnModel().getColumn(3).setMaxWidth(0);
        tabelaTransakcija.getColumnModel().getColumn(3).setWidth(0);
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



