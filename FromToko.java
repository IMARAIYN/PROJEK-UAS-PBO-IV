package toko;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FromToko extends JFrame {
    private String[] judul = {"Kode Barang", "Nama Barang", "Harga Barang", "Stok Barang"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblKode = new JLabel("Kode Barang");
    JTextField txKode = new JTextField(10);
    JLabel lblNama = new JLabel("Nama Barang");
    JTextField txNama = new JTextField(20);
    JLabel lblHarga = new JLabel("Harga Barang");
    JTextField txHarga = new JTextField(10);
    JLabel lblStok = new JLabel("Stok Barang");
    JTextField txStok = new JTextField(10);
    JButton btAdd = new JButton("Simpan");
    JButton btNew = new JButton("Baru");
    JButton btDel = new JButton("Hapus");
    JButton btEdit = new JButton("Ubah");

    FromToko() {
        super("Data Barang");
        setSize(600, 300);
        pnl.setLayout(null);
        pnl.add(lblKode);
        lblKode.setBounds(20, 10, 100, 20);
        pnl.add(txKode);
        txKode.setBounds(130, 10, 150, 20);
        pnl.add(lblNama);
        lblNama.setBounds(20, 35, 100, 20);
        pnl.add(txNama);
        txNama.setBounds(130, 35, 200, 20);
        pnl.add(lblHarga);
        lblHarga.setBounds(20, 60, 100, 20);
        pnl.add(txHarga);
        txHarga.setBounds(130, 60, 100, 20);
        pnl.add(lblStok);
        lblStok.setBounds(20, 85, 100, 20);
        pnl.add(txStok);
        txStok.setBounds(130, 85, 100, 20);

        pnl.add(btNew);
        btNew.setBounds(300, 10, 125, 20);
        btNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btNewAksi(e);
            }
        });

        pnl.add(btAdd);
        btAdd.setBounds(300, 35, 125, 20);
        btAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btAddAksi(e);
            }
        });

        pnl.add(btEdit);
        btEdit.setBounds(300, 60, 125, 20);
        btEdit.setEnabled(false);
        btEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btEditAksi(e);
            }
        });

        pnl.add(btDel);
        btDel.setBounds(300, 85, 125, 20);
        btDel.setEnabled(false);
        btDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btDelAksi(e);
            }
        });

        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.getViewport().add(tab);
        tab.setEnabled(true);
        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scp.setBounds(20, 110, 550, 130);
        pnl.add(scp);
        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadData() {
        try {
            Connection cn = new connectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "SELECT * FROM tbl_barang";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String kode, nama;
                int harga, stok;
                kode = rs.getString("kode_barang");
                nama = rs.getString("nama_barang");
                harga = rs.getInt("harga_barang");
                stok = rs.getInt("stok_barang");
                Object[] data = {kode, nama, harga, stok};
                df.addRow(data);
            }
            rs.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void clearTable() {
        int numRow = df.getRowCount();
        for (int i = 0; i < numRow; i++) {
            df.removeRow(0);
        }
    }

    void clearTextField() {
        txKode.setText(null);
        txNama.setText(null);
        txHarga.setText(null);
        txStok.setText(null);
    }

    void simpanData(Barang B) {
        try {
            Connection cn = new connectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang) " +
                    "VALUES ('" + B.getKodeBarang() + "', '" + B.getNamaBarang() + "', " +
                    B.getHargaBarang() + ", " + B.getStokBarang() + ")";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                    "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            Object[] data = {B.getKodeBarang(), B.getNamaBarang(), B.getHargaBarang(), B.getStokBarang()};
            df.addRow(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kode) {
        try {
            Connection cn = new connectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "DELETE FROM tbl_barang WHERE kode_barang = '" + kode + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            df.removeRow(tab.getSelectedRow());
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Barang B, String kode) {
        try {
            Connection cn = new connectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "UPDATE tbl_barang SET kode_barang='" + B.getKodeBarang() + "', " +
                    "nama_barang='" + B.getNamaBarang() + "', harga_barang=" + B.getHargaBarang() + ", " +
                    "stok_barang=" + B.getStokBarang() + " WHERE kode_barang='" + kode + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            clearTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btNewAksi(ActionEvent evt) {
        txKode.setText(null);
        txNama.setText(null);
        txHarga.setText(null);
        txStok.setText(null);
        btEdit.setEnabled(false);
        btDel.setEnabled(false);
        btAdd.setEnabled(true);
    }

    private void btAddAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txKode.getText());
        B.setNamaBarang(txNama.getText());
        B.setHargaBarang(Integer.parseInt(txHarga.getText()));
        B.setStokBarang(Integer.parseInt(txStok.getText()));
        simpanData(B);
        clearTextField();
    }

    private void btEditAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txKode.getText());
        B.setNamaBarang(txNama.getText());
        B.setHargaBarang(Integer.parseInt(txHarga.getText()));
        B.setStokBarang(Integer.parseInt(txStok.getText()));
        ubahData(B, txKode.getText());
    }

    private void btDelAksi(ActionEvent evt) {
        hapusData(txKode.getText());
    }

    private void tabMouseClicked(MouseEvent evt) {
        btAdd.setEnabled(false);
        btEdit.setEnabled(true);
        btDel.setEnabled(true);
        int i = tab.getSelectedRow();
        txKode.setText((String) df.getValueAt(i, 0));
        txNama.setText((String) df.getValueAt(i, 1));
        txHarga.setText(df.getValueAt(i, 2).toString());
        txStok.setText(df.getValueAt(i, 3).toString());
    }

    public static void main(String args[]) {
        new FromToko().loadData();
    }
}
