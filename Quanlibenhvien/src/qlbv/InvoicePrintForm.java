package qlbv;

// Thư viện giao diện Swing và AWT, csdl
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;
// Thư viện xử lý XML DOM
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class InvoicePrintForm extends JPanel implements Printable {

    // Các thành phần giao diện chính
    private JTextField txtTenBN, txtNgayKham, txtDichVu, txtTongTien;
    private JButton btnThem, btnSua, btnXoa, btnIn, btnLuuXML, btnDocXML;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea textArea;

    public InvoicePrintForm() {
        setLayout(new BorderLayout());

        // Panel nhập liệu: tên, ngày, dịch vụ, tổng tiền
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

        // Các nút chức năng chính: CRUD + In + XML
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

        // Bảng hiển thị dữ liệu hóa đơn
        tableModel = new DefaultTableModel(new String[]{"ID", "Tên", "Ngày khám", "Dịch vụ", "Tổng tiền"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Khu vực hiển thị hóa đơn trước khi in
        textArea = new JTextArea(10, 40);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane invoiceScroll = new JScrollPane(textArea);
        invoiceScroll.setBorder(BorderFactory.createTitledBorder("Hóa đơn in"));

        // Gộp layout giao diện tổng thể
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(invoiceScroll, BorderLayout.SOUTH);

        // Load dữ liệu từ MySQL khi mở form
        loadInvoiceData();

        // Sự kiện khi click vào bảng: đưa dữ liệu lên TextField
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtTenBN.setText(tableModel.getValueAt(row, 1).toString());
                txtNgayKham.setText(tableModel.getValueAt(row, 2).toString());
                txtDichVu.setText(tableModel.getValueAt(row, 3).toString());
                txtTongTien.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // Bắt sự kiện các nút chức năng
        btnThem.addActionListener(e -> themHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnIn.addActionListener(e -> inHoaDon());
        btnLuuXML.addActionListener(e -> luuHoaDonXML_DOM());
        btnDocXML.addActionListener(e -> docHoaDonXML_DOM());
    }

    // Kết nối tới CSDL MySQL
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/qlbenhvieng", "root", "");
    }

    // Load dữ liệu từ bảng hoadon trong MySQL vào JTable
    private void loadInvoiceData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hoadon ORDER BY id DESC")) {

            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("tenbenhnhan"),
                        rs.getDate("ngaykham"),
                        rs.getString("dichvu"),
                        rs.getInt("tongtien")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi DB: " + e.getMessage());
        }
    }

    // THÊM hóa đơn vào CSDL
    private void themHoaDon() {
        String sql = "INSERT INTO hoadon (tenbenhnhan, ngaykham, dichvu, tongtien) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtTenBN.getText());
            ps.setDate(2, Date.valueOf(txtNgayKham.getText()));
            ps.setString(3, txtDichVu.getText());
            ps.setInt(4, Integer.parseInt(txtTongTien.getText()));

            ps.executeUpdate();
            loadInvoiceData();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    // SỬA hóa đơn được chọn
    private void suaHoaDon() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String sql = "UPDATE hoadon SET tenbenhnhan=?, ngaykham=?, dichvu=?, tongtien=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtTenBN.getText());
            ps.setDate(2, Date.valueOf(txtNgayKham.getText()));
            ps.setString(3, txtDichVu.getText());
            ps.setInt(4, Integer.parseInt(txtTongTien.getText()));
            ps.setInt(5, id);

            ps.executeUpdate();
            loadInvoiceData();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    // XÓA hóa đơn khỏi CSDL
    private void xoaHoaDon() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn ID " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM hoadon WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
            loadInvoiceData();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    // In hóa đơn: hiện nội dung vào textArea + gọi máy in
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

        // In ra máy in
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

    // Cài đặt interface Printable (in textArea)
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        textArea.printAll(g);
        return PAGE_EXISTS;
    }

    // Lưu hóa đơn được chọn vào file XML bằng DOM
    private void luuHoaDonXML_DOM() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để lưu!");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("HoaDon");
            doc.appendChild(root);

            Element id = doc.createElement("ID");
            id.appendChild(doc.createTextNode(tableModel.getValueAt(row, 0).toString()));
            root.appendChild(id);

            Element ten = doc.createElement("TenBenhNhan");
            ten.appendChild(doc.createTextNode(tableModel.getValueAt(row, 1).toString()));
            root.appendChild(ten);

            Element ngay = doc.createElement("NgayKham");
            ngay.appendChild(doc.createTextNode(tableModel.getValueAt(row, 2).toString()));
            root.appendChild(ngay);

            Element dv = doc.createElement("DichVu");
            dv.appendChild(doc.createTextNode(tableModel.getValueAt(row, 3).toString()));
            root.appendChild(dv);

            Element tong = doc.createElement("TongTien");
            tong.appendChild(doc.createTextNode(tableModel.getValueAt(row, 4).toString()));
            root.appendChild(tong);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(new File("hoadon.xml")));

            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn XML bằng DOM");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi DOM XML: " + e.getMessage());
        }
    }

    // Đọc dữ liệu hóa đơn từ file XML vào bảng JTable
    private void docHoaDonXML_DOM() {
        try {
            File xmlFile = new File("hoadon.xml");
            if (!xmlFile.exists()) {
                JOptionPane.showMessageDialog(this, "File hoadon.xml không tồn tại!");
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("HoaDon");
            tableModel.setRowCount(0); // Xóa bảng cũ

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) node;

                    String id = ele.getElementsByTagName("ID").item(0).getTextContent();
                    String ten = ele.getElementsByTagName("TenBenhNhan").item(0).getTextContent();
                    String ngay = ele.getElementsByTagName("NgayKham").item(0).getTextContent();
                    String dv = ele.getElementsByTagName("DichVu").item(0).getTextContent();
                    String tong = ele.getElementsByTagName("TongTien").item(0).getTextContent();

                    tableModel.addRow(new Object[]{Integer.parseInt(id), ten, Date.valueOf(ngay), dv, Integer.parseInt(tong)});
                }
            }

            JOptionPane.showMessageDialog(this, "Đọc XML thành công!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc XML: " + e.getMessage());
        }
    }

}
