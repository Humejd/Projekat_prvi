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

    private TransactionManager menadzer;

    public FinanceTrackerForm() {
        menadzer = new TransactionManager();

        ucitajTabelu();
        azurirajPregled();

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
    }

    private void ucitajTabelu() {
        ArrayList<Transaction> lista = menadzer.dohvatiSveTransakcije();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vrsta");
        model.addColumn("Iznos");
        model.addColumn("Opis");

        for (Transaction t : lista) {
            model.addRow(new Object[]{
                    t.getVrsta(),
                    t.getIznos(),
                    t.getOpis()
            });
        }

        tabelaTransakcija.setModel(model);
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
