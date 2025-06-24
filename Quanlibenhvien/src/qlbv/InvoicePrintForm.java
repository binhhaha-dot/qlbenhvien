package qlbv;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InvoicePrintForm extends JPanel implements Printable {

    private JTextField txtTenBN, txtNgayKham, txtDichVu, txtTongTien;
    private JButton btnThem, btnSua, btnXoa, btnIn, btnLuuXML, btnDocXML;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea textArea;

    public InvoicePrintForm() {
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
        loadInvoiceData();

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
        btnThem.addActionListener(e -> themHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnIn.addActionListener(e -> inHoaDon());
        btnLuuXML.addActionListener(e -> luuHoaDonXML_DOM());
        btnDocXML.addActionListener(e -> docHoaDonXML_DOM());



    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/qlphongkham", "root", "");
    }

    private void loadInvoiceData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hoadon ORDER BY id DESC")) {

            tableModel.setRowCount(0);
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

        // In nếu cần
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

    // Interface Printable để in JTextArea
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        textArea.printAll(g);
        return PAGE_EXISTS;
    }
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

            // Ghi ra file
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File("hoadon.xml"));
            transformer.transform(domSource, sr);

            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn XML bằng DOM");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi DOM XML: " + e.getMessage());
        }
    }
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


