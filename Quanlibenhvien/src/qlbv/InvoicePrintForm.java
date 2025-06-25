// File: view/InvoicePanel.java
package qlbv;

import serveice.InvoiceController;
import model.HoaDon;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.List;

public class InvoicePrintForm extends JPanel implements Printable {
    private JTextField txtTenBN, txtNgayKham, txtDichVu, txtTongTien;
    private JButton btnThem, btnSua, btnXoa, btnIn, btnLuuXML, btnDocXML;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea textArea;
    private InvoiceController controller;

    public InvoicePrintForm() {
        controller = new InvoiceController();
        setLayout(new BorderLayout());

        // --- Panel Nhập Dữ Liệu ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        txtTenBN = new JTextField();
        txtNgayKham = new JTextField("2025-05-25");
        txtDichVu = new JTextField();
        txtTongTien = new JTextField();

        inputPanel.add(new JLabel("Tên bệnh nhân:"));
        inputPanel.add(txtTenBN);
        inputPanel.add(new JLabel("Ngày khám (yyyy-MM-dd):"));
        inputPanel.add(txtNgayKham);
        inputPanel.add(new JLabel("Dịch vụ:"));
        inputPanel.add(txtDichVu);
        inputPanel.add(new JLabel("Tổng tiền:"));
        inputPanel.add(txtTongTien);

        // --- Nút chức năng ---
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnIn = new JButton("In hóa đơn");
        btnLuuXML = new JButton("Lưu XML");
        btnDocXML = new JButton("ĐỌC XML");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnIn);
        btnPanel.add(btnLuuXML);
        btnPanel.add(btnDocXML);

        // --- Bảng hóa đơn ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Tên", "Ngày khám", "Dịch vụ", "Tổng tiền"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // --- Khu vực hiển thị hóa đơn in ---
        textArea = new JTextArea(10, 40);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane invoiceScroll = new JScrollPane(textArea);
        invoiceScroll.setBorder(BorderFactory.createTitledBorder("Hóa đơn in"));

        // --- Gộp layout ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(invoiceScroll, BorderLayout.SOUTH);

        // --- Load dữ liệu ---
        loadDataToTable();

        // --- Sự kiện bảng ---
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtTenBN.setText(tableModel.getValueAt(row, 1).toString());
                txtNgayKham.setText(tableModel.getValueAt(row, 2).toString());
                txtDichVu.setText(tableModel.getValueAt(row, 3).toString());
                txtTongTien.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // --- Sự kiện nút ---
        btnThem.addActionListener(e -> {
            controller.themHoaDon(txtTenBN.getText(), txtNgayKham.getText(), txtDichVu.getText(), txtTongTien.getText());
            loadDataToTable();
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                controller.suaHoaDon(id, txtTenBN.getText(), txtNgayKham.getText(), txtDichVu.getText(), txtTongTien.getText());
                loadDataToTable();
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                controller.xoaHoaDon(id);
                loadDataToTable();
            }
        });

        btnIn.addActionListener(e -> inHoaDon());

        btnLuuXML.addActionListener(e -> controller.luuXML(tableModel));
        btnDocXML.addActionListener(e -> {
            List<HoaDon> list = controller.docXML();
            loadListToTable(list);
        });
    }

    private void loadDataToTable() {
        List<HoaDon> danhSach = controller.layTatCaHoaDon();
        loadListToTable(danhSach);
    }

    private void loadListToTable(List<HoaDon> list) {
        tableModel.setRowCount(0);
        for (HoaDon hd : list) {
            tableModel.addRow(new Object[]{hd.getId(), hd.getTenBenhNhan(), hd.getNgayKham(), hd.getDichVu(), hd.getTongTien()});
        }
    }

    private void inHoaDon() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        StringBuilder sb = new StringBuilder();
        sb.append("-------- HÓA ĐƠN KHÁM BỆNH --------\n");
        sb.append("Mã HĐ    : ").append(tableModel.getValueAt(row, 0)).append("\n");
        sb.append("Bệnh nhân: ").append(tableModel.getValueAt(row, 1)).append("\n");
        sb.append("Ngày khám: ").append(tableModel.getValueAt(row, 2)).append("\n");
        sb.append("Dịch vụ  : ").append(tableModel.getValueAt(row, 3)).append("\n");
        sb.append("Tiền     : ").append(tableModel.getValueAt(row, 4)).append(" VNĐ\n");
        sb.append("-----------------------------------\n");
        sb.append("Xin cảm ơn quý khách!\n");

        textArea.setText(sb.toString());

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage());
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        textArea.printAll(g);
        return PAGE_EXISTS;
    }

}



