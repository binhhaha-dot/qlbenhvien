package model;

import java.sql.Date;

public class HoaDon {
    private int id;
    private String tenBenhNhan;
    private Date ngayKham;
    private String dichVu;
    private int tongTien;

    public HoaDon(int id, String tenBenhNhan, Date ngayKham, String dichVu, int tongTien) {
        this.id = id;
        this.tenBenhNhan = tenBenhNhan;
        this.ngayKham = ngayKham;
        this.dichVu = dichVu;
        this.tongTien = tongTien;
    }

    public HoaDon(String tenBenhNhan, Date ngayKham, String dichVu, int tongTien) {
        this(0, tenBenhNhan, ngayKham, dichVu, tongTien);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenBenhNhan() { return tenBenhNhan; }
    public void setTenBenhNhan(String tenBenhNhan) { this.tenBenhNhan = tenBenhNhan; }

    public Date getNgayKham() { return ngayKham; }
    public void setNgayKham(Date ngayKham) { this.ngayKham = ngayKham; }

    public String getDichVu() { return dichVu; }
    public void setDichVu(String dichVu) { this.dichVu = dichVu; }

    public int getTongTien() { return tongTien; }
    public void setTongTien(int tongTien) { this.tongTien = tongTien; }
}
