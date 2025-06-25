package serveice;

import model.HoaDon;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.sql.Date;

public class InvoiceController {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/qlbenhvien", "root", "");
    }

    public List<HoaDon> layTatCaHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hoadon ORDER BY id DESC")) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getInt("id"),
                        rs.getString("tenbenhnhan"),
                        rs.getDate("ngaykham"),
                        rs.getString("dichvu"),
                        rs.getInt("tongtien")
                );
                list.add(hd);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi DB: " + e.getMessage());
        }
        return list;
    }

    public void themHoaDon(String ten, String ngay, String dv, String tong) {
        String sql = "INSERT INTO hoadon (tenbenhnhan, ngaykham, dichvu, tongtien) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setDate(2, Date.valueOf(ngay));
            ps.setString(3, dv);
            ps.setInt(4, Integer.parseInt(tong));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + e.getMessage());
        }
        
    }

    public void suaHoaDon(int id, String ten, String ngay, String dv, String tong) {
        String sql = "UPDATE hoadon SET tenbenhnhan=?, ngaykham=?, dichvu=?, tongtien=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setDate(2, Date.valueOf(ngay));
            ps.setString(3, dv);
            ps.setInt(4, Integer.parseInt(tong));
            ps.setInt(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cập nhật thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        }
    }

    public void xoaHoaDon(int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Xóa hóa đơn ID " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM hoadon WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xóa thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }
    }

    public void luuXML(DefaultTableModel tableModel) {
        int row = tableModel.getRowCount();
        if (row == 0) {
            JOptionPane.showMessageDialog(null, "Không có hóa đơn để lưu XML!");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("HoaDons");
            doc.appendChild(root);

            for (int i = 0; i < row; i++) {
                Element hd = doc.createElement("HoaDon");

                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(tableModel.getValueAt(i, 0).toString()));
                hd.appendChild(id);

                Element ten = doc.createElement("TenBenhNhan");
                ten.appendChild(doc.createTextNode(tableModel.getValueAt(i, 1).toString()));
                hd.appendChild(ten);

                Element ngay = doc.createElement("NgayKham");
                ngay.appendChild(doc.createTextNode(tableModel.getValueAt(i, 2).toString()));
                hd.appendChild(ngay);

                Element dv = doc.createElement("DichVu");
                dv.appendChild(doc.createTextNode(tableModel.getValueAt(i, 3).toString()));
                hd.appendChild(dv);

                Element tong = doc.createElement("TongTien");
                tong.appendChild(doc.createTextNode(tableModel.getValueAt(i, 4).toString()));
                hd.appendChild(tong);

                root.appendChild(hd);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File("hoadon.xml"));
            transformer.transform(domSource, sr);

            JOptionPane.showMessageDialog(null, "Đã lưu danh sách hóa đơn ra hoadon.xml");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi lưu XML: " + e.getMessage());
        }
    }

    public List<HoaDon> docXML() {
        List<HoaDon> list = new ArrayList<>();
        try {
            File file = new File("hoadon.xml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "File hoadon.xml không tồn tại!");
                return list;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("HoaDon");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) node;

                    int id = Integer.parseInt(ele.getElementsByTagName("ID").item(0).getTextContent());
                    String ten = ele.getElementsByTagName("TenBenhNhan").item(0).getTextContent();
                    Date ngay = Date.valueOf(ele.getElementsByTagName("NgayKham").item(0).getTextContent());
                    String dv = ele.getElementsByTagName("DichVu").item(0).getTextContent();
                    int tong = Integer.parseInt(ele.getElementsByTagName("TongTien").item(0).getTextContent());

                    list.add(new HoaDon(id, ten, ngay, dv, tong));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi đọc XML: " + e.getMessage());
        }
        return list;
    }
}
