package qlbv;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class InvoicePrintForm extends JPanel implements Printable {

    private JTextArea textArea;
    private JButton btnPrint;

    public InvoicePrintForm() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        btnPrint = new JButton("In hóa đơn");
        btnPrint.addActionListener(e -> printInvoice());

        add(scrollPane, BorderLayout.CENTER);
        add(btnPrint, BorderLayout.SOUTH);

        loadInvoiceDataFromDatabase();
    }

    // In hóa đơn ra máy in
    private void printInvoice() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi in: " + e.getMessage());
            }
        }
    }

    // Giao diện máy in
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // In toàn bộ nội dung JTextArea
        textArea.printAll(g2d);
        return PAGE_EXISTS;
    }

    // Lấy hóa đơn gần nhất từ database
    private void loadInvoiceDataFromDatabase() {
        StringBuilder invoiceText = new StringBuilder();

        String sql = "SELECT * FROM hoadon ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlbv", "root", "");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                invoiceText.append("-------- HÓA ĐƠN KHÁM BỆNH --------\n");
                invoiceText.append("Mã HĐ    : ").append(rs.getInt("id")).append("\n");
                invoiceText.append("Bệnh nhân: ").append(rs.getString("tenbenhnhan")).append("\n");

                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date ngayKham = rs.getDate("ngaykham");
                String ngayKhamStr = (ngayKham != null) ? df.format(ngayKham) : "N/A";
                invoiceText.append("Ngày khám: ").append(ngayKhamStr).append("\n");

                invoiceText.append("Dịch vụ : ").append(rs.getString("dichvu")).append("\n");
                invoiceText.append("Tiền    : ").append(rs.getInt("tongtien")).append(" VNĐ\n");
                invoiceText.append("-----------------------------------\n");
                invoiceText.append("Xin cảm ơn quý khách!\n");
            } else {
                invoiceText.append("Không tìm thấy hóa đơn.");
            }

        } catch (SQLException e) {
            invoiceText.append("Lỗi kết nối database: ").append(e.getMessage());
        }

        textArea.setText(invoiceText.toString());
    }
}
